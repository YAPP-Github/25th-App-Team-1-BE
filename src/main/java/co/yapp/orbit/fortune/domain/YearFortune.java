package co.yapp.orbit.fortune.domain;

public class YearFortune {
    
    private final Integer id;
    private final Integer fortuneId;
    private final String birthYear;
    private final String yearFortune;

    public YearFortune(Integer id, Integer fortuneId, String birthYear, String yearFortune) {
        this.id = id;
        this.fortuneId = fortuneId;
        this.birthYear = birthYear;
        this.yearFortune = yearFortune;
    }

    public Integer getId() {
        return id;
    }

    public Integer getFortuneId() {
        return fortuneId;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public String getYearFortune() {
        return yearFortune;
    }
} 