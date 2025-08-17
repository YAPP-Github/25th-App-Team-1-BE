package co.yapp.orbit.fortune.domain;

import java.time.LocalDate;

public class ConstellationFortune {
    
    private final String constellationName;
    private final String constellationEnglish;
    private final String period;
    private final String fortune;
    private final LocalDate crawledDate;

    public ConstellationFortune(String constellationName, String constellationEnglish, 
                               String period, String fortune, LocalDate crawledDate) {
        this.constellationName = constellationName;
        this.constellationEnglish = constellationEnglish;
        this.period = period;
        this.fortune = fortune;
        this.crawledDate = crawledDate;
    }

    public String getConstellationName() {
        return constellationName;
    }

    public String getConstellationEnglish() {
        return constellationEnglish;
    }

    public String getPeriod() {
        return period;
    }

    public String getFortune() {
        return fortune;
    }

    public LocalDate getCrawledDate() {
        return crawledDate;
    }
} 