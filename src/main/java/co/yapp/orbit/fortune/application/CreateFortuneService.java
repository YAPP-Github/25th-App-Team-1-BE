package co.yapp.orbit.fortune.application;

import co.yapp.orbit.fortune.adapter.out.request.CreateFortuneRequest;
import co.yapp.orbit.fortune.application.exception.FortuneCreateInvalidUserException;
import co.yapp.orbit.fortune.application.port.in.CreateFortuneCommand;
import co.yapp.orbit.fortune.application.port.in.CreateFortuneUseCase;
import co.yapp.orbit.fortune.application.port.out.FortuneGenerationPort;
import co.yapp.orbit.fortune.application.port.out.SaveFortunePort;
import co.yapp.orbit.fortune.domain.Fortune;
import co.yapp.orbit.user.adapter.out.response.UserInfoResponse;
import co.yapp.orbit.user.application.exception.UserNotFoundException;
import co.yapp.orbit.user.application.port.out.UserApiPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@Slf4j
public class CreateFortuneService implements CreateFortuneUseCase {

    private final FortuneGenerationPort fortuneGenerationPort;
    private final SaveFortunePort saveFortunePort;
    private final UserApiPort userApiPort;
    private final ExternalFortuneDataService externalFortuneDataService;
    
    @Value("${fortune.external-data.enabled:false}")
    private boolean externalDataEnabled;

    public CreateFortuneService(FortuneGenerationPort fortuneGenerationPort,
                               SaveFortunePort saveFortunePort, 
                               UserApiPort userApiPort,
                               ExternalFortuneDataService externalFortuneDataService) {
        this.fortuneGenerationPort = fortuneGenerationPort;
        this.saveFortunePort = saveFortunePort;
        this.userApiPort = userApiPort;
        this.externalFortuneDataService = externalFortuneDataService;
    }

    @Override
    @Transactional
    public Fortune createFortune(CreateFortuneCommand command) {
        CreateFortuneRequest request = null;

        try {
            UserInfoResponse userInfo = userApiPort.getUserInfo(command.getUserId());
            
            if (externalDataEnabled) {
                // 외부 운세 데이터를 포함한 요청 생성
                request = createEnhancedFortuneRequest(userInfo);
            } else {
                // 기존 방식으로 요청 생성 (호환성 유지)
                request = new CreateFortuneRequest(
                    userInfo.getName(),
                    userInfo.getBirthDate(),
                    userInfo.getBirthTime(),
                    userInfo.getCalendarType(),
                    userInfo.getGender());
            }

        } catch (UserNotFoundException e) {
            log.error("운세 생성 중 UserNotFoundException 발생: {}", command.getUserId(), e);
            throw new FortuneCreateInvalidUserException("존재하지 않는 사용자입니다.");
        }

        Fortune fortune = fortuneGenerationPort.loadFortune(request);

        Long fortuneId = saveFortunePort.save(fortune);

        return Fortune.create(
            fortuneId,
            fortune.getDailyFortuneTitle(),
            fortune.getDailyFortuneDescription(),
            fortune.getStudyCareerFortune(),
            fortune.getWealthFortune(),
            fortune.getHealthFortune(),
            fortune.getLoveFortune(),
            fortune.getLuckyOutfitTop(),
            fortune.getLuckyOutfitBottom(),
            fortune.getLuckyOutfitShoes(),
            fortune.getLuckyOutfitAccessory(),
            fortune.getUnluckyColor(),
            fortune.getLuckyColor(),
            fortune.getLuckyFood()
        );
    }
    
    /**
     * 외부 운세 데이터를 포함한 운세 요청을 생성합니다.
     */
    private CreateFortuneRequest createEnhancedFortuneRequest(UserInfoResponse userInfo) {
        try {
            LocalDate birthDate = LocalDate.parse(userInfo.getBirthDate());
            ExternalFortuneDataService.ExternalFortuneData externalData = 
                externalFortuneDataService.getAllExternalFortuneData(birthDate);
            
            String constellationFortune = null;
            String zodiacFortune = null; 
            String yearFortune = null;
            
            if (externalData.getConstellationFortune() != null) {
                constellationFortune = formatConstellationFortune(externalData.getConstellationFortune());
            }
            
            if (externalData.getZodiacFortune() != null) {
                zodiacFortune = formatZodiacFortune(externalData.getZodiacFortune());
            }
            
            if (externalData.getYearFortune() != null) {
                yearFortune = formatYearFortune(externalData.getYearFortune());
            }
            
            return new CreateFortuneRequest(
                userInfo.getName(),
                userInfo.getBirthDate(),
                userInfo.getBirthTime(),
                userInfo.getCalendarType(),
                userInfo.getGender(),
                constellationFortune,
                zodiacFortune,
                yearFortune
            );
            
        } catch (Exception e) {
            log.warn("외부 운세 데이터 조회 실패, 기본 요청으로 대체: {}", userInfo.getName(), e);
            // 외부 데이터 조회 실패 시 기본 요청으로 대체
            return new CreateFortuneRequest(
                userInfo.getName(),
                userInfo.getBirthDate(),
                userInfo.getBirthTime(),
                userInfo.getCalendarType(),
                userInfo.getGender());
        }
    }
    
    private String formatConstellationFortune(co.yapp.orbit.fortune.domain.ConstellationFortune fortune) {
        return String.format("[%s 운세] %s", fortune.getConstellationName(), fortune.getFortune());
    }
    
    private String formatZodiacFortune(co.yapp.orbit.fortune.domain.ZodiacFortune fortune) {
        return String.format("[%s 운세] %s", fortune.getZodiacName(), fortune.getGeneralFortune());
    }
    
    private String formatYearFortune(co.yapp.orbit.fortune.domain.YearFortune fortune) {
        return String.format("[%s년생 운세] %s", fortune.getBirthYear(), fortune.getYearFortune());
    }
}
