package co.yapp.orbit.fortune.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("띠 계산 테스트")
class ChineseZodiacTest {

    @Test
    @DisplayName("생년으로 띠를 정확히 계산한다")
    void calculateZodiacFromBirthYear() {
        // given & when & then
        assertThat(ChineseZodiac.fromBirthYear(1984)).isEqualTo(ChineseZodiac.RAT);
        assertThat(ChineseZodiac.fromBirthYear(1985)).isEqualTo(ChineseZodiac.OX);
        assertThat(ChineseZodiac.fromBirthYear(1990)).isEqualTo(ChineseZodiac.HORSE);
        assertThat(ChineseZodiac.fromBirthYear(2000)).isEqualTo(ChineseZodiac.DRAGON);
        assertThat(ChineseZodiac.fromBirthYear(2024)).isEqualTo(ChineseZodiac.DRAGON);
    }

    @Test
    @DisplayName("12년 주기로 동일한 띠가 반복된다")
    void zodiacRepeatsEvery12Years() {
        // given & when & then
        assertThat(ChineseZodiac.fromBirthYear(1984)).isEqualTo(ChineseZodiac.fromBirthYear(1996));
        assertThat(ChineseZodiac.fromBirthYear(1990)).isEqualTo(ChineseZodiac.fromBirthYear(2002));
        assertThat(ChineseZodiac.fromBirthYear(2000)).isEqualTo(ChineseZodiac.fromBirthYear(2012));
    }

    @Test
    @DisplayName("1984년 이전 연도도 정확히 계산한다")
    void calculateZodiacBeforeBaseYear() {
        // given & when & then
        assertThat(ChineseZodiac.fromBirthYear(1983)).isEqualTo(ChineseZodiac.PIG);
        assertThat(ChineseZodiac.fromBirthYear(1972)).isEqualTo(ChineseZodiac.RAT);
        assertThat(ChineseZodiac.fromBirthYear(1960)).isEqualTo(ChineseZodiac.RAT);
    }

    @Test
    @DisplayName("띠 정보를 올바르게 반환한다")
    void getZodiacInfo() {
        // given
        ChineseZodiac dragon = ChineseZodiac.DRAGON;
        
        // when & then
        assertThat(dragon.getKoreanName()).isEqualTo("용");
        assertThat(dragon.getEnglishName()).isEqualTo("dragon");
        assertThat(dragon.getKoreanNameWithSuffix()).isEqualTo("용띠");
    }
} 