package co.yapp.orbit.fortune.adapter.out;

import co.yapp.orbit.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "constellations")
public class ConstellationEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "constellation_name", nullable = false, length = 20)
    private String constellationName;

    @Column(name = "constellation_english", nullable = false, length = 20)
    private String constellationEnglish;

    @Column(name = "period", nullable = false, length = 50)
    private String period;

    @Column(name = "constellation_fortune", nullable = false, columnDefinition = "TEXT")
    private String constellationFortune;

    @Column(name = "crawled_date", nullable = false)
    private LocalDate crawledDate;

    protected ConstellationEntity() {
    }

    public ConstellationEntity(String constellationName, String constellationEnglish, 
                              String period, String constellationFortune, LocalDate crawledDate) {
        this.constellationName = constellationName;
        this.constellationEnglish = constellationEnglish;
        this.period = period;
        this.constellationFortune = constellationFortune;
        this.crawledDate = crawledDate;
    }

    public Integer getId() {
        return id;
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

    public String getConstellationFortune() {
        return constellationFortune;
    }

    public LocalDate getCrawledDate() {
        return crawledDate;
    }
} 