package co.yapp.orbit.fortune.application.port.out;

import co.yapp.orbit.fortune.domain.ZodiacFortune;
import java.time.LocalDate;
import java.util.Optional;

public interface LoadZodiacFortunePort {
    
    /**
     * 특정 날짜의 띠별 운세 조회
     * 
     * @param zodiacEnglish 띠 영어명 (예: "dragon")
     * @param date 조회할 날짜
     * @return 띠별 운세 정보
     */
    Optional<ZodiacFortune> findByZodiacAndDate(String zodiacEnglish, LocalDate date);
    
    /**
     * 띠의 가장 최근 운세 조회
     * 
     * @param zodiacEnglish 띠 영어명 (예: "dragon")
     * @return 가장 최근 띠별 운세 정보
     */
    Optional<ZodiacFortune> findLatestByZodiac(String zodiacEnglish);
} 