package co.yapp.orbit.fortune.domain;

import java.time.LocalDate;

public class ZodiacFortune {
    
    private final Integer id;
    private final String zodiacName;
    private final String zodiacEnglish;
    private final String generalFortune;
    private final LocalDate crawledDate;

    public ZodiacFortune(Integer id, String zodiacName, String zodiacEnglish, 
                        String generalFortune, LocalDate crawledDate) {
        this.id = id;
        this.zodiacName = zodiacName;
        this.zodiacEnglish = zodiacEnglish;
        this.generalFortune = generalFortune;
        this.crawledDate = crawledDate;
    }

    public Integer getId() {
        return id;
    }

    public String getZodiacName() {
        return zodiacName;
    }

    public String getZodiacEnglish() {
        return zodiacEnglish;
    }

    public String getGeneralFortune() {
        return generalFortune;
    }

    public LocalDate getCrawledDate() {
        return crawledDate;
    }
} 