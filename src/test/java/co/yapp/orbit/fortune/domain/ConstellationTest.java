package co.yapp.orbit.fortune.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("별자리 계산 테스트")
class ConstellationTest {

    @Test
    @DisplayName("생년월일로 별자리를 정확히 계산한다")
    void calculateConstellationFromBirthDate() {
        // given & when & then
        assertThat(Constellation.fromBirthDate(LocalDate.of(1990, 3, 15)))
            .isEqualTo(Constellation.PISCES);
        
        assertThat(Constellation.fromBirthDate(LocalDate.of(1990, 4, 25)))
            .isEqualTo(Constellation.TAURUS);
        
        assertThat(Constellation.fromBirthDate(LocalDate.of(1990, 8, 10)))
            .isEqualTo(Constellation.LEO);
        
        assertThat(Constellation.fromBirthDate(LocalDate.of(1990, 12, 25)))
            .isEqualTo(Constellation.CAPRICORN);
    }

    @Test
    @DisplayName("경계일에 별자리를 정확히 계산한다")
    void calculateConstellationOnBoundaryDates() {
        // given & when & then
        assertThat(Constellation.fromBirthDate(LocalDate.of(1990, 3, 21)))
            .isEqualTo(Constellation.ARIES);
        
        assertThat(Constellation.fromBirthDate(LocalDate.of(1990, 3, 20)))
            .isEqualTo(Constellation.PISCES);
        
        // 연도를 넘나드는 염소자리 테스트
        assertThat(Constellation.fromBirthDate(LocalDate.of(1990, 12, 22)))
            .isEqualTo(Constellation.CAPRICORN);
        
        assertThat(Constellation.fromBirthDate(LocalDate.of(1990, 1, 19)))
            .isEqualTo(Constellation.CAPRICORN);
        
        assertThat(Constellation.fromBirthDate(LocalDate.of(1990, 1, 20)))
            .isEqualTo(Constellation.AQUARIUS);
    }

    @Test
    @DisplayName("별자리 정보를 올바르게 반환한다")
    void getConstellationInfo() {
        // given
        Constellation aquarius = Constellation.AQUARIUS;
        
        // when & then
        assertThat(aquarius.getKoreanName()).isEqualTo("물병자리");
        assertThat(aquarius.getEnglishName()).isEqualTo("aquarius");
    }
} 