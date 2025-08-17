package co.yapp.orbit.fortune.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface ConstellationRepository extends JpaRepository<ConstellationEntity, Integer> {
    
    /**
     * 별자리 영어명과 크롤링 날짜로 운세 조회
     */
    Optional<ConstellationEntity> findByConstellationEnglishAndCrawledDate(String constellationEnglish, LocalDate crawledDate);
    
    /**
     * 별자리 영어명으로 가장 최근 운세 조회 (크롤링 날짜 기준 내림차순)
     */
    Optional<ConstellationEntity> findTopByConstellationEnglishOrderByCrawledDateDesc(String constellationEnglish);
} 