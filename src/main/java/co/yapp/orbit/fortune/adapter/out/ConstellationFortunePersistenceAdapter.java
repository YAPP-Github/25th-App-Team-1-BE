package co.yapp.orbit.fortune.adapter.out;

import co.yapp.orbit.fortune.application.port.out.LoadConstellationFortunePort;
import co.yapp.orbit.fortune.domain.ConstellationFortune;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class ConstellationFortunePersistenceAdapter implements LoadConstellationFortunePort {
    
    private final ConstellationRepository constellationRepository;
    
    public ConstellationFortunePersistenceAdapter(ConstellationRepository constellationRepository) {
        this.constellationRepository = constellationRepository;
    }
    
    @Override
    public Optional<ConstellationFortune> findByConstellationAndDate(String constellationEnglish, LocalDate date) {
        return constellationRepository.findByConstellationEnglishAndCrawledDate(constellationEnglish, date)
                .map(this::mapToDomain);
    }
    
    @Override
    public Optional<ConstellationFortune> findLatestByConstellation(String constellationEnglish) {
        return constellationRepository.findTopByConstellationEnglishOrderByCrawledDateDesc(constellationEnglish)
                .map(this::mapToDomain);
    }
    
    private ConstellationFortune mapToDomain(ConstellationEntity entity) {
        return new ConstellationFortune(
                entity.getConstellationName(),
                entity.getConstellationEnglish(),
                entity.getPeriod(),
                entity.getConstellationFortune(),
                entity.getCrawledDate()
        );
    }
} 