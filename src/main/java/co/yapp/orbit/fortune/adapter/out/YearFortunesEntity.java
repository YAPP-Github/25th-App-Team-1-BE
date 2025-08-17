package co.yapp.orbit.fortune.adapter.out;

import co.yapp.orbit.global.domain.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "year_fortunes")
public class YearFortunesEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fortune_id", nullable = false)
    private Integer fortuneId;

    @Column(name = "birth_year", nullable = false, length = 10)
    private String birthYear;

    @Column(name = "year_fortune", nullable = false, columnDefinition = "TEXT")
    private String yearFortune;

    protected YearFortunesEntity() {
    }

    public YearFortunesEntity(Integer fortuneId, String birthYear, String yearFortune) {
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