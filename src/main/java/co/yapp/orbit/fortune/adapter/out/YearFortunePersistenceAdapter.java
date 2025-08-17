package co.yapp.orbit.fortune.adapter.out;

import co.yapp.orbit.fortune.application.port.out.LoadYearFortunePort;
import co.yapp.orbit.fortune.domain.YearFortune;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class YearFortunePersistenceAdapter implements LoadYearFortunePort {
    
    private final YearFortunesRepository yearFortunesRepository;
    
    public YearFortunePersistenceAdapter(YearFortunesRepository yearFortunesRepository) {
        this.yearFortunesRepository = yearFortunesRepository;
    }
    
    @Override
    public Optional<YearFortune> findByFortuneIdAndBirthYear(Integer fortuneId, String birthYear) {
        return yearFortunesRepository.findByFortuneIdAndBirthYear(fortuneId, birthYear)
                .map(this::mapToDomain);
    }
    
    private YearFortune mapToDomain(YearFortunesEntity entity) {
        return new YearFortune(
                entity.getId(),
                entity.getFortuneId(),
                entity.getBirthYear(),
                entity.getYearFortune()
        );
    }
} 