package co.yapp.orbit.fortune.adapter.out;

import co.yapp.orbit.fortune.application.port.out.LoadZodiacFortunePort;
import co.yapp.orbit.fortune.domain.ZodiacFortune;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class ZodiacFortunePersistenceAdapter implements LoadZodiacFortunePort {
    
    private final FortunesRepository fortunesRepository;
    
    public ZodiacFortunePersistenceAdapter(FortunesRepository fortunesRepository) {
        this.fortunesRepository = fortunesRepository;
    }
    
    @Override
    public Optional<ZodiacFortune> findByZodiacAndDate(String zodiacEnglish, LocalDate date) {
        return fortunesRepository.findByZodiacEnglishAndCrawledDate(zodiacEnglish, date)
                .map(this::mapToDomain);
    }
    
    @Override
    public Optional<ZodiacFortune> findLatestByZodiac(String zodiacEnglish) {
        return fortunesRepository.findTopByZodiacEnglishOrderByCrawledDateDesc(zodiacEnglish)
                .map(this::mapToDomain);
    }
    
    private ZodiacFortune mapToDomain(FortunesEntity entity) {
        return new ZodiacFortune(
                entity.getId(),
                entity.getZodiacName(),
                entity.getZodiacEnglish(),
                entity.getGeneralFortune(),
                entity.getCrawledDate()
        );
    }
} 