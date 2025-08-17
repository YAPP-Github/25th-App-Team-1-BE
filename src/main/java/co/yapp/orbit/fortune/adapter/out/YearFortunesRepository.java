package co.yapp.orbit.fortune.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface YearFortunesRepository extends JpaRepository<YearFortunesEntity, Integer> {
    
    /**
     * fortune_id와 birth_year로 연도별 운세 조회
     */
    Optional<YearFortunesEntity> findByFortuneIdAndBirthYear(Integer fortuneId, String birthYear);
} 