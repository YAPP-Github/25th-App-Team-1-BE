package co.yapp.orbit.fortune.adapter.out;

import co.yapp.orbit.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fortunes")
public class FortunesEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "zodiac_name", nullable = false, length = 20)
    private String zodiacName;

    @Column(name = "zodiac_english", nullable = false, length = 20)
    private String zodiacEnglish;

    @Column(name = "general_fortune", nullable = false, columnDefinition = "TEXT")
    private String generalFortune;

    @Column(name = "crawled_date", nullable = false)
    private LocalDate crawledDate;

    protected FortunesEntity() {
    }

    public FortunesEntity(String zodiacName, String zodiacEnglish, 
                         String generalFortune, LocalDate crawledDate) {
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