package co.yapp.orbit.fortune.application.port.out;

import co.yapp.orbit.fortune.domain.YearFortune;
import java.util.Optional;

public interface LoadYearFortunePort {
    
    /**
     * fortune_id와 출생년도로 연도별 운세 조회
     * 
     * @param fortuneId 띠별 운세 ID
     * @param birthYear 출생년도 (예: "2000")
     * @return 연도별 운세 정보
     */
    Optional<YearFortune> findByFortuneIdAndBirthYear(Integer fortuneId, String birthYear);
} 