package co.yapp.orbit.fortune.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.yapp.orbit.fortune.application.port.in.CreateFortuneCommand;
import co.yapp.orbit.fortune.application.port.out.FortuneGenerationPort;
import co.yapp.orbit.fortune.application.port.out.SaveFortunePort;
import co.yapp.orbit.fortune.domain.Fortune;
import co.yapp.orbit.fortune.domain.FortuneItem;
import co.yapp.orbit.user.adapter.out.response.UserInfoResponse;
import co.yapp.orbit.user.application.port.out.UserApiPort;
import co.yapp.orbit.user.domain.CalendarType;
import co.yapp.orbit.user.domain.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CreateFortuneServiceTest {

    private CreateFortuneService createFortuneService;
    private FortuneGenerationPort fortuneGenerationPort;
    private UserApiPort userApiPort;
    private SaveFortunePort saveFortunePort;
    private ExternalFortuneDataService externalFortuneDataService;

    @BeforeEach
    void setUp() {
        fortuneGenerationPort = Mockito.mock(FortuneGenerationPort.class);
        saveFortunePort = Mockito.mock(SaveFortunePort.class);
        userApiPort = Mockito.mock(UserApiPort.class);
        externalFortuneDataService = Mockito.mock(ExternalFortuneDataService.class);
        createFortuneService = new CreateFortuneService(fortuneGenerationPort, saveFortunePort, userApiPort, externalFortuneDataService);
    }

    @Test
    @DisplayName("운세 생성 요청이 들어오면 운세가 정상적으로 저장된다.")
    void createFortune_thenSave() {
        // given
        CreateFortuneCommand command = new CreateFortuneCommand("1");
        Fortune fortune = Fortune.create(
            1L,
            "윤다혜, 오늘은 기분 좋은 일들이 많을 거야!",
            "긍정적인 마음으로 하루를 시작하면 좋은 결과가 있을 거 같아. 주변 사람들과의 관계도 원만하게 유지하면서 행복한 하루 보내길 바라!",
            new FortuneItem(90, "title1", "Success in studies"),
            new FortuneItem(90, "title2", "Prosperity in finance"),
            new FortuneItem(90, "title3", "Strong physical health"),
            new FortuneItem(90, "title4", "Harmonious love life"),
            "Red Shirt",
            "Black Pants",
            "White Sneakers",
            "Gold Necklace",
            "Green",
            "Blue",
            "Pizza"
        );
        UserInfoResponse userInfo = new UserInfoResponse(
            "name",
            "2025-02-09",
            "08:30:00",
            CalendarType.SOLAR.name(),
            Gender.MALE.name()
        );

        when(userApiPort.getUserInfo(1L)).thenReturn(userInfo);
        when(fortuneGenerationPort.loadFortune(any())).thenReturn(fortune);
        when(saveFortunePort.save(any(Fortune.class))).thenReturn(1L);

        // when
        Fortune fortuneResponse = createFortuneService.createFortune(command);

        // then
        verify(fortuneGenerationPort, times(1)).loadFortune(any());
        verify(saveFortunePort, times(1)).save(any(Fortune.class));

        assertThat(fortuneResponse.getId()).isEqualTo(1L);
    }
}