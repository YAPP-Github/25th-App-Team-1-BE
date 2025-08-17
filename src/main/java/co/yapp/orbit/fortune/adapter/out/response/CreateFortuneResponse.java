package co.yapp.orbit.fortune.adapter.out.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateFortuneResponse {

    private String dailyFortuneTitle;
    private String dailyFortuneDescription;
    private Map<String, FortuneItemResponse> fortune; // "study_career", "wealth", "health", "love"
    private LuckyOutfitResponse luckyOutfit;
    private String unluckyColor;
    private String luckyColor;
    private String luckyFood;

    public CreateFortuneResponse(String dailyFortuneTitle, String dailyFortuneDescription, Map<String, FortuneItemResponse> fortune, LuckyOutfitResponse luckyOutfit,
        String unluckyColor, String luckyColor, String luckyFood) {
        this.dailyFortuneTitle = dailyFortuneTitle;
        this.dailyFortuneDescription = dailyFortuneDescription;
        this.fortune = fortune;
        this.luckyOutfit = luckyOutfit;
        this.unluckyColor = unluckyColor;
        this.luckyColor = luckyColor;
        this.luckyFood = luckyFood;
    }

    @JsonProperty("daily_fortune_title")
    public String getDailyFortuneTitle() {
        return dailyFortuneTitle;
    }

    @JsonProperty("daily_fortune_description")
    public String getDailyFortuneDescription() {
        return dailyFortuneDescription;
    }

    @JsonProperty("fortune")
    public Map<String, FortuneItemResponse> getFortune() {
        return fortune;
    }

    @JsonProperty("lucky_outfit")
    public LuckyOutfitResponse getLuckyOutfit() {
        return luckyOutfit;
    }

    @JsonProperty("unlucky_color")
    public String getUnluckyColor() {
        return unluckyColor;
    }

    @JsonProperty("lucky_color")
    public String getLuckyColor() {
        return luckyColor;
    }

    @JsonProperty("lucky_food")
    public String getLuckyFood() {
        return luckyFood;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FortuneItemResponse {
        private int score;
        private String title;
        private String description;

        public FortuneItemResponse(int score, String title, String description) {
            this.score = score;
            this.title = title;
            this.description = description;
        }

        @JsonProperty("score")
        public int getScore() {
            return score;
        }

        @JsonProperty("title")
        public String getTitle() {
            return title;
        }

        @JsonProperty("description")
        public String getDescription() {
            return description;
        }
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LuckyOutfitResponse {
        private String top;
        private String bottom;
        private String shoes;
        private String accessory;

        public LuckyOutfitResponse(String top, String bottom, String shoes, String accessory) {
            this.top = top;
            this.bottom = bottom;
            this.shoes = shoes;
            this.accessory = accessory;
        }

        @JsonProperty("top")
        public String getTop() {
            return top;
        }

        @JsonProperty("bottom")
        public String getBottom() {
            return bottom;
        }

        @JsonProperty("shoes")
        public String getShoes() {
            return shoes;
        }

        @JsonProperty("accessory")
        public String getAccessory() {
            return accessory;
        }
    }
}
