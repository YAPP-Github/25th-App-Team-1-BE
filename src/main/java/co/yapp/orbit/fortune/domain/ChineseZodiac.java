package co.yapp.orbit.fortune.domain;

public enum ChineseZodiac {
    RAT("쥐", "rat", 0),
    OX("소", "ox", 1),
    TIGER("호랑이", "tiger", 2),
    RABBIT("토끼", "rabbit", 3),
    DRAGON("용", "dragon", 4),
    SNAKE("뱀", "snake", 5),
    HORSE("말", "horse", 6),
    GOAT("양", "goat", 7),
    MONKEY("원숭이", "monkey", 8),
    ROOSTER("닭", "rooster", 9),
    DOG("개", "dog", 10),
    PIG("돼지", "pig", 11);

    private final String koreanName;
    private final String englishName;
    private final int order;

    ChineseZodiac(String koreanName, String englishName, int order) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.order = order;
    }

    public String getKoreanName() {
        return koreanName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public int getOrder() {
        return order;
    }

    /**
     * 생년으로부터 띠를 계산합니다.
     * 쥐띠 기준년도: 1984년 (12년 주기)
     * 
     * @param birthYear 생년 (예: 1990)
     * @return 해당 띠
     */
    public static ChineseZodiac fromBirthYear(int birthYear) {
        // 1984년이 쥐띠 (갑자년)
        int baseYear = 1984;
        int offset = (birthYear - baseYear) % 12;
        
        // 음수가 나올 경우 처리
        if (offset < 0) {
            offset += 12;
        }
        
        for (ChineseZodiac zodiac : values()) {
            if (zodiac.getOrder() == offset) {
                return zodiac;
            }
        }
        
        // 이 경우는 발생하지 않아야 하지만, 기본값으로 쥐띠 반환
        return RAT;
    }

    /**
     * 띠명 + "띠" 형태로 반환합니다.
     * 
     * @return 예: "용띠", "쥐띠"
     */
    public String getKoreanNameWithSuffix() {
        return koreanName + "띠";
    }
} 