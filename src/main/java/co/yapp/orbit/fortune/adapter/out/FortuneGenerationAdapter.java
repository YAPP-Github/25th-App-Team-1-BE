package co.yapp.orbit.fortune.adapter.out;

import co.yapp.orbit.fortune.adapter.out.exception.FortuneParsingException;
import co.yapp.orbit.fortune.adapter.out.exception.WebClientFetchException;
import co.yapp.orbit.fortune.adapter.out.exception.FortunePromptLoadException;
import co.yapp.orbit.fortune.adapter.out.request.CreateFortuneRequest;
import co.yapp.orbit.fortune.adapter.out.request.FortuneGenerationRequest;
import co.yapp.orbit.fortune.adapter.out.response.CreateFortuneResponse;
import co.yapp.orbit.fortune.adapter.out.response.CreateFortuneResponse.FortuneItemResponse;
import co.yapp.orbit.fortune.adapter.out.response.FortuneGenerationResponse;
import co.yapp.orbit.fortune.application.port.out.FortuneGenerationPort;
import co.yapp.orbit.fortune.domain.Fortune;
import co.yapp.orbit.fortune.domain.FortuneItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class FortuneGenerationAdapter implements FortuneGenerationPort {

    private final WebClient webClient;
    private final String geminiFullUrl;
    private final ObjectMapper objectMapper;
    private final JsonNode promptTemplate;
    
    @Value("${fortune.prompt.version:v2}")
    private String promptVersion;
    
    @Value("${fortune.external-data.enabled:false}")
    private boolean externalDataEnabled;

    // 프롬프트 파일 경로 상수
    private static final String PROMPT_FILE_PATH_V2 = "templates/prompts/fortune_prompt_v2.json";
    private static final String PROMPT_FILE_PATH_V3 = "templates/prompts/fortune_prompt_v3.json";

    public FortuneGenerationAdapter(
        @Value("${gemini.api.url}") String geminiApiUrl,
        @Value("${gemini.api.key}") String geminiApiKey,
        @Value("${fortune.prompt.version:v2}") String promptVersion,
        ObjectMapper objectMapper
    ) {
        this.webClient = WebClient.create();
        this.geminiFullUrl = geminiApiUrl + "?key=" + geminiApiKey;
        this.objectMapper = objectMapper;
        this.promptVersion = promptVersion;
        this.promptTemplate = loadPromptTemplate();
        
        // 🔍 실제 사용되는 API 설정 로깅
        log.info("=== Gemini API 설정 확인 ===");
        log.info("API URL: {}", geminiApiUrl);
        log.info("API Key (마지막 4자리): ...{}", geminiApiKey.length() > 4 ? geminiApiKey.substring(geminiApiKey.length() - 4) : "짧음");
        log.info("Full URL: {}", geminiFullUrl.replaceAll("key=[^&]*", "key=***"));
        log.info("Prompt Version: {}", promptVersion);
        log.info("=== 설정 확인 끝 ===");
    }

    private JsonNode loadPromptTemplate() {
        String promptFilePath = getPromptFilePath();
        try (InputStream inputStream = new ClassPathResource(promptFilePath).getInputStream()) {
            return objectMapper.readTree(inputStream);
        } catch (IOException e) {
            log.error("프롬프트 템플릿 로드 오류: {}", promptFilePath, e);
            // v3 로드 실패 시 v2로 폴백
            if ("v3".equals(promptVersion)) {
                log.warn("v3 프롬프트 로드 실패, v2로 폴백");
                try (InputStream inputStream = new ClassPathResource(PROMPT_FILE_PATH_V2).getInputStream()) {
                    return objectMapper.readTree(inputStream);
                } catch (IOException fallbackException) {
                    log.error("v2 프롬프트 폴백도 실패: {}", PROMPT_FILE_PATH_V2, fallbackException);
                    throw new FortunePromptLoadException("프롬프트 템플릿을 불러오는 중 오류가 발생했습니다.");
                }
            }
            throw new FortunePromptLoadException("프롬프트 템플릿을 불러오는 중 오류가 발생했습니다.");
        }
    }
    
    private String getPromptFilePath() {
        return "v3".equals(promptVersion) ? PROMPT_FILE_PATH_V3 : PROMPT_FILE_PATH_V2;
    }

    @Override
    public Fortune loadFortune(CreateFortuneRequest request) {
        String prompt = generatePrompt(request);
        String response = callAi(prompt, request.getName());

        if (response == null || response.trim().isEmpty()) {
            log.error("WebClient 요청 오류: {}", request);
            throw new WebClientFetchException("운세 데이터를 불러오는 데 실패했습니다.");
        }

        return parseStringToFortune(response);
    }

    public String callAi(String prompt, String userName) {
        try {
            long seed = (LocalDate.now().toString() + userName).hashCode();

            FortuneGenerationResponse response = webClient.post()
                .uri(geminiFullUrl)
                .bodyValue(new FortuneGenerationRequest(prompt, seed))
                .retrieve()
                .bodyToMono(FortuneGenerationResponse.class)
                .block();

            return getFirstContentText(response);
        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            // Gemini API에서 오는 구체적인 에러 메시지 로깅
            log.error("=== Gemini API 에러 상세 정보 ===");
            log.error("HTTP 상태: {} {}", e.getStatusCode().value(), e.getStatusText());
            log.error("요청 URL: {}", geminiFullUrl);
            log.error("사용자: {}", userName);
            log.error("프롬프트 길이: {}", prompt.length());
            log.error("Gemini API 응답 바디: {}", e.getResponseBodyAsString());
            log.error("=== 에러 정보 끝 ===");
            throw new WebClientFetchException("Gemini API 오류: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (RuntimeException e) {
            log.error("WebClient 기타 오류 - 사용자: {}, 프롬프트 길이: {}, 오류: {}", userName, prompt.length(), e.getMessage(), e);
            throw new WebClientFetchException("운세 데이터 요청 중 오류가 발생했습니다.");
        }
    }

    private String getFirstContentText(FortuneGenerationResponse response) {
        return response.getCandidates().get(0).getContent().getParts().get(0).getText();
    }

    public String generatePrompt(CreateFortuneRequest request) {
        try {
            // 기본 사용자 정보 설정
            ObjectNode userInfoJson = objectMapper.createObjectNode();
            userInfoJson.put("name", request.getName());
            userInfoJson.put("birth_date", request.getBirthDate());
            userInfoJson.put("birth_time", request.getBirthTime());
            userInfoJson.put("calendar_type", request.getCalendarType());
            userInfoJson.put("gender", request.getGender());

            ObjectNode copiedPrompt = promptTemplate.deepCopy();
            copiedPrompt.set("user_info", userInfoJson);

            // 오늘 날짜 설정
            ObjectNode todayDateJson = objectMapper.createObjectNode();
            todayDateJson.put("today_date", LocalDate.now().toString());
            copiedPrompt.set("today_date", todayDateJson);

            // v3 프롬프트이고 외부 데이터가 활성화된 경우 참고 운세 데이터 추가
            if ("v3".equals(promptVersion) && externalDataEnabled && hasExternalFortuneData(request)) {
                ObjectNode referenceFortunesJson = objectMapper.createObjectNode();
                
                if (request.getConstellationFortune() != null) {
                    referenceFortunesJson.put("constellation_fortune", request.getConstellationFortune());
                }
                
                if (request.getZodiacFortune() != null) {
                    referenceFortunesJson.put("zodiac_fortune", request.getZodiacFortune());
                }
                
                if (request.getYearFortune() != null) {
                    referenceFortunesJson.put("year_fortune", request.getYearFortune());
                }
                
                copiedPrompt.set("reference_fortunes", referenceFortunesJson);
                log.debug("외부 운세 데이터가 프롬프트에 포함되었습니다: {}", request.getName());
            }

            // v3 프롬프트의 경우 JSON 구조를 텍스트 프롬프트로 변환
            if ("v3".equals(promptVersion)) {
                return convertV3JsonToTextPrompt(copiedPrompt);
            }
            
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(copiedPrompt);
        } catch (JsonProcessingException e) {
            log.error("프롬프트 템플릿 작성 오류: {}", request, e);
            throw new FortunePromptLoadException("프롬프트를 작성하는 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * v3 JSON 프롬프트를 간결한 텍스트 프롬프트로 변환
     */
    private String convertV3JsonToTextPrompt(ObjectNode promptJson) {
        StringBuilder prompt = new StringBuilder();
        
        // 페르소나 정보
        JsonNode persona = promptJson.get("persona");
        if (persona != null) {
            prompt.append("# 역할\n");
            prompt.append(persona.get("description").asText()).append("\n");
            prompt.append("이름: ").append(persona.get("name").asText()).append("\n\n");
        }
        
        // 핵심 임무
        JsonNode coreMission = promptJson.get("core_mission");
        if (coreMission != null) {
            prompt.append("# 핵심 임무\n");
            prompt.append(coreMission.get("description").asText()).append("\n\n");
        }
        
        // 사용자 정보
        JsonNode userInfo = promptJson.get("user_info");
        if (userInfo != null) {
            prompt.append("# 사용자 정보\n");
            prompt.append("이름: ").append(userInfo.get("name").asText()).append("\n");
            prompt.append("생년월일: ").append(userInfo.get("birth_date").asText()).append("\n");
            if (userInfo.has("birth_time") && !userInfo.get("birth_time").isNull()) {
                prompt.append("출생시간: ").append(userInfo.get("birth_time").asText()).append("\n");
            }
            prompt.append("성별: ").append(userInfo.get("gender").asText()).append("\n");
            prompt.append("달력: ").append(userInfo.get("calendar_type").asText()).append("\n\n");
        }
        
        // 오늘 날짜
        JsonNode todayDate = promptJson.get("today_date");
        if (todayDate != null) {
            prompt.append("# 오늘 날짜\n");
            prompt.append(todayDate.get("today_date").asText()).append("\n\n");
        }
        
        // 참고 운세 데이터 (v3의 핵심)
        JsonNode referenceFortunes = promptJson.get("reference_fortunes");
        if (referenceFortunes != null) {
            prompt.append("# 참고 운세 데이터\n");
            prompt.append("다음 운세 데이터를 참고하여 개인화된 운세를 생성하세요:\n\n");
            
            if (referenceFortunes.has("constellation_fortune")) {
                prompt.append("별자리 운세: ").append(referenceFortunes.get("constellation_fortune").asText()).append("\n\n");
            }
            if (referenceFortunes.has("zodiac_fortune")) {
                prompt.append("띠별 운세: ").append(referenceFortunes.get("zodiac_fortune").asText()).append("\n\n");
            }
            if (referenceFortunes.has("year_fortune")) {
                prompt.append("연간 운세: ").append(referenceFortunes.get("year_fortune").asText()).append("\n\n");
            }
        }
        
        // 중요 지시사항
        JsonNode overallInstructions = promptJson.get("overall_instructions");
        if (overallInstructions != null) {
            prompt.append("# 중요 지시사항\n");
            JsonNode rules = overallInstructions.get("rules");
            if (rules != null && rules.isArray()) {
                for (JsonNode rule : rules) {
                    prompt.append("- ").append(rule.asText()).append("\n");
                }
            }
            prompt.append("\n");
        }
        
        // 출력 형식
        JsonNode outputFormat = promptJson.get("output_format");
        if (outputFormat != null) {
            prompt.append("# 출력 형식\n");
            prompt.append("반드시 다음 JSON 형식으로만 응답하세요:\n\n");
            try {
                prompt.append("```json\n");
                prompt.append(objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(outputFormat));
                prompt.append("\n```\n");
            } catch (JsonProcessingException e) {
                log.warn("출력 형식 변환 실패", e);
            }
        }
        
        return prompt.toString();
    }
    
    /**
     * 외부 운세 데이터가 포함되어 있는지 확인
     */
    private boolean hasExternalFortuneData(CreateFortuneRequest request) {
        return request.getConstellationFortune() != null 
            || request.getZodiacFortune() != null 
            || request.getYearFortune() != null;
    }

    private Fortune parseStringToFortune(String response) {
        try {
            response = response
                .replaceAll("\\s+", " ")
                .replaceAll("\\n+", "\n")
                .replaceAll("\\\\+", "\\\\")
                .replaceAll("```", "")
                .replaceAll("json", "")
                .trim();

            CreateFortuneResponse fortuneResponse = objectMapper.readValue(response, CreateFortuneResponse.class);

            FortuneItemResponse studyCareer = fortuneResponse.getFortune().get("study_career");
            FortuneItemResponse wealth = fortuneResponse.getFortune().get("wealth");
            FortuneItemResponse health = fortuneResponse.getFortune().get("health");
            FortuneItemResponse love = fortuneResponse.getFortune().get("love");

            return Fortune.create(
                null,
                fortuneResponse.getDailyFortuneTitle(),
                fortuneResponse.getDailyFortuneDescription(),
                new FortuneItem(studyCareer.getScore(), studyCareer.getTitle(), studyCareer.getDescription()),
                new FortuneItem(wealth.getScore(), wealth.getTitle(), wealth.getDescription()),
                new FortuneItem(health.getScore(), health.getTitle(), health.getDescription()),
                new FortuneItem(love.getScore(), love.getTitle(), love.getDescription()),
                fortuneResponse.getLuckyOutfit().getTop(),
                fortuneResponse.getLuckyOutfit().getBottom(),
                fortuneResponse.getLuckyOutfit().getShoes(),
                fortuneResponse.getLuckyOutfit().getAccessory(),
                fortuneResponse.getUnluckyColor(),
                fortuneResponse.getLuckyColor(),
                fortuneResponse.getLuckyFood()
            );

        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 오류: {}", response, e);
            throw new FortuneParsingException("운세 데이터를 처리하는 과정에서 오류가 발생했습니다.");
        }
    }
}
