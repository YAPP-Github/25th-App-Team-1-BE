package co.yapp.orbit.fortune.domain;

import java.time.LocalDate;
import java.time.MonthDay;

public enum Constellation {
    AQUARIUS("물병자리", "aquarius", MonthDay.of(1, 20), MonthDay.of(2, 18)),
    PISCES("물고기자리", "pisces", MonthDay.of(2, 19), MonthDay.of(3, 20)),
    ARIES("양자리", "aries", MonthDay.of(3, 21), MonthDay.of(4, 19)),
    TAURUS("황소자리", "taurus", MonthDay.of(4, 20), MonthDay.of(5, 20)),
    GEMINI("쌍둥이자리", "gemini", MonthDay.of(5, 21), MonthDay.of(6, 21)),
    CANCER("게자리", "cancer", MonthDay.of(6, 22), MonthDay.of(7, 22)),
    LEO("사자자리", "leo", MonthDay.of(7, 23), MonthDay.of(8, 22)),
    VIRGO("처녀자리", "virgo", MonthDay.of(8, 23), MonthDay.of(9, 22)),
    LIBRA("천칭자리", "libra", MonthDay.of(9, 23), MonthDay.of(10, 22)),
    SCORPIO("전갈자리", "scorpio", MonthDay.of(10, 23), MonthDay.of(11, 21)),
    SAGITTARIUS("사수자리", "sagittarius", MonthDay.of(11, 22), MonthDay.of(12, 21)),
    CAPRICORN("염소자리", "capricorn", MonthDay.of(12, 22), MonthDay.of(1, 19));

    private final String koreanName;
    private final String englishName;
    private final MonthDay startDate;
    private final MonthDay endDate;

    Constellation(String koreanName, String englishName, MonthDay startDate, MonthDay endDate) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getKoreanName() {
        return koreanName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public MonthDay getStartDate() {
        return startDate;
    }

    public MonthDay getEndDate() {
        return endDate;
    }

    public static Constellation fromBirthDate(LocalDate birthDate) {
        MonthDay birth = MonthDay.from(birthDate);
        
        for (Constellation constellation : values()) {
            if (isDateInRange(birth, constellation)) {
                return constellation;
            }
        }
        
        // 이 경우는 발생하지 않아야 하지만, 기본값으로 물병자리 반환
        return AQUARIUS;
    }

    private static boolean isDateInRange(MonthDay birth, Constellation constellation) {
        MonthDay start = constellation.getStartDate();
        MonthDay end = constellation.getEndDate();
        
        // 연도를 넘나드는 경우 처리 (염소자리: 12/22 ~ 1/19)
        if (start.isAfter(end)) {
            return !birth.isBefore(start) || !birth.isAfter(end);
        } else {
            return !birth.isBefore(start) && !birth.isAfter(end);
        }
    }
} 