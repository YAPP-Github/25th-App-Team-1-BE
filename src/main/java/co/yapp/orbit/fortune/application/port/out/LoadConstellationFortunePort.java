package co.yapp.orbit.fortune.application.port.out;

import co.yapp.orbit.fortune.domain.ConstellationFortune;
import java.time.LocalDate;
import java.util.Optional;

public interface LoadConstellationFortunePort {
    
    /**
     * 특정 날짜의 별자리 운세 조회
     * 
     * @param constellationEnglish 별자리 영어명 (예: "aquarius")
     * @param date 조회할 날짜
     * @return 별자리 운세 정보
     */
    Optional<ConstellationFortune> findByConstellationAndDate(String constellationEnglish, LocalDate date);
    
    /**
     * 별자리의 가장 최근 운세 조회
     * 
     * @param constellationEnglish 별자리 영어명 (예: "aquarius")
     * @return 가장 최근 별자리 운세 정보
     */
    Optional<ConstellationFortune> findLatestByConstellation(String constellationEnglish);
} 