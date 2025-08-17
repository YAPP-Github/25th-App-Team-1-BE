package co.yapp.orbit.fortune.application;

import co.yapp.orbit.fortune.application.port.out.LoadConstellationFortunePort;
import co.yapp.orbit.fortune.application.port.out.LoadYearFortunePort;
import co.yapp.orbit.fortune.application.port.out.LoadZodiacFortunePort;
import co.yapp.orbit.fortune.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class ExternalFortuneDataService {
    
    private final LoadConstellationFortunePort loadConstellationFortunePort;
    private final LoadZodiacFortunePort loadZodiacFortunePort;
    private final LoadYearFortunePort loadYearFortunePort;
    
    public ExternalFortuneDataService(LoadConstellationFortunePort loadConstellationFortunePort,
                                     LoadZodiacFortunePort loadZodiacFortunePort,
                                     LoadYearFortunePort loadYearFortunePort) {
        this.loadConstellationFortunePort = loadConstellationFortunePort;
        this.loadZodiacFortunePort = loadZodiacFortunePort;
        this.loadYearFortunePort = loadYearFortunePort;
    }
    
    /**
     * 사용자 생년월일로부터 별자리 운세를 조회합니다.
     */
    public Optional<ConstellationFortune> getConstellationFortune(LocalDate birthDate) {
        try {
            Constellation constellation = Constellation.fromBirthDate(birthDate);
            String constellationEnglish = constellation.getEnglishName();
            
            // 오늘 날짜 운세를 우선 조회
            LocalDate today = LocalDate.now();
            Optional<ConstellationFortune> todayFortune = 
                loadConstellationFortunePort.findByConstellationAndDate(constellationEnglish, today);
            
            if (todayFortune.isPresent()) {
                return todayFortune;
            }
            
            // 오늘 데이터가 없으면 가장 최근 데이터 조회
            return loadConstellationFortunePort.findLatestByConstellation(constellationEnglish);
            
        } catch (Exception e) {
            log.error("별자리 운세 조회 중 오류 발생: birthDate={}", birthDate, e);
            return Optional.empty();
        }
    }
    
    /**
     * 사용자 생년으로부터 띠별 운세를 조회합니다.
     */
    public Optional<ZodiacFortune> getZodiacFortune(int birthYear) {
        try {
            ChineseZodiac zodiac = ChineseZodiac.fromBirthYear(birthYear);
            String zodiacEnglish = zodiac.getEnglishName();
            
            // 오늘 날짜 운세를 우선 조회
            LocalDate today = LocalDate.now();
            Optional<ZodiacFortune> todayFortune = 
                loadZodiacFortunePort.findByZodiacAndDate(zodiacEnglish, today);
            
            if (todayFortune.isPresent()) {
                return todayFortune;
            }
            
            // 오늘 데이터가 없으면 가장 최근 데이터 조회
            return loadZodiacFortunePort.findLatestByZodiac(zodiacEnglish);
            
        } catch (Exception e) {
            log.error("띠별 운세 조회 중 오류 발생: birthYear={}", birthYear, e);
            return Optional.empty();
        }
    }
    
    /**
     * 띠별 운세 ID와 출생년도로부터 연도별 운세를 조회합니다.
     */
    public Optional<YearFortune> getYearFortune(Integer fortuneId, String birthYear) {
        try {
            return loadYearFortunePort.findByFortuneIdAndBirthYear(fortuneId, birthYear);
        } catch (Exception e) {
            log.error("연도별 운세 조회 중 오류 발생: fortuneId={}, birthYear={}", fortuneId, birthYear, e);
            return Optional.empty();
        }
    }
    
    /**
     * 사용자 정보로부터 모든 외부 운세 데이터를 조회합니다.
     */
    public ExternalFortuneData getAllExternalFortuneData(LocalDate birthDate) {
        ConstellationFortune constellationFortune = getConstellationFortune(birthDate).orElse(null);
        
        int birthYear = birthDate.getYear();
        ZodiacFortune zodiacFortune = getZodiacFortune(birthYear).orElse(null);
        
        YearFortune yearFortune = null;
        if (zodiacFortune != null) {
            yearFortune = getYearFortune(zodiacFortune.getId(), String.valueOf(birthYear)).orElse(null);
        }
        
        return new ExternalFortuneData(constellationFortune, zodiacFortune, yearFortune);
    }
    
    /**
     * 외부 운세 데이터를 담는 DTO 클래스
     */
    public static class ExternalFortuneData {
        private final ConstellationFortune constellationFortune;
        private final ZodiacFortune zodiacFortune;
        private final YearFortune yearFortune;
        
        public ExternalFortuneData(ConstellationFortune constellationFortune, 
                                  ZodiacFortune zodiacFortune, 
                                  YearFortune yearFortune) {
            this.constellationFortune = constellationFortune;
            this.zodiacFortune = zodiacFortune;
            this.yearFortune = yearFortune;
        }
        
        public ConstellationFortune getConstellationFortune() {
            return constellationFortune;
        }
        
        public ZodiacFortune getZodiacFortune() {
            return zodiacFortune;
        }
        
        public YearFortune getYearFortune() {
            return yearFortune;
        }
        
        public boolean hasAnyData() {
            return constellationFortune != null || zodiacFortune != null || yearFortune != null;
        }
    }
} 