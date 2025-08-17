package co.yapp.orbit.fortune.adapter.out.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class CreateFortuneRequest {

    private String name;
    private String birthDate;
    private String birthTime;
    private String calendarType;
    private String gender;
    
    // 외부 운세 데이터 (선택적 필드)
    private String constellationFortune;
    private String zodiacFortune;
    private String yearFortune;

    // 기존 생성자 (호환성 유지)
    public CreateFortuneRequest(String name, String birthDate, String birthTime,
        String calendarType,
        String gender) {
        this.name = name;
        this.birthDate = birthDate;
        this.birthTime = birthTime;
        this.calendarType = calendarType;
        this.gender = gender;
    }
    
    // 외부 운세 데이터를 포함한 새로운 생성자
    public CreateFortuneRequest(String name, String birthDate, String birthTime,
                               String calendarType, String gender,
                               String constellationFortune, String zodiacFortune, String yearFortune) {
        this.name = name;
        this.birthDate = birthDate;
        this.birthTime = birthTime;
        this.calendarType = calendarType;
        this.gender = gender;
        this.constellationFortune = constellationFortune;
        this.zodiacFortune = zodiacFortune;
        this.yearFortune = yearFortune;
    }

    public String getConstellationFortune() {
        return constellationFortune;
    }

    public String getZodiacFortune() {
        return zodiacFortune;
    }

    public String getYearFortune() {
        return yearFortune;
    }

    @Override
    public String toString() {
        return "CreateFortuneRequest{" +
            "name='" + name + '\'' +
            ", birthDate='" + birthDate + '\'' +
            ", birthTime='" + birthTime + '\'' +
            ", calendarType='" + calendarType + '\'' +
            ", gender='" + gender + '\'' +
            ", constellationFortune='" + constellationFortune + '\'' +
            ", zodiacFortune='" + zodiacFortune + '\'' +
            ", yearFortune='" + yearFortune + '\'' +
            '}';
    }
}
