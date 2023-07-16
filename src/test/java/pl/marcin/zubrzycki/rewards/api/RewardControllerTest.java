package pl.marcin.zubrzycki.rewards.api;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import pl.marcin.zubrzycki.rewards.api.dto.MonthlyRewardDto;
import pl.marcin.zubrzycki.rewards.api.dto.RewardDto;
import pl.marcin.zubrzycki.rewards.service.RewardService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class RewardControllerTest {
    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController systemUnderTest;

    @Test
    void willCalculateReward() {
        // GIVEN
        List<MonthlyRewardDto> rewardsByMonth = List.of(MonthlyRewardDto.builder().month("AUGUST").points(100).build());
        RewardDto rewardDto = RewardDto.builder()
                .totalPoints(100)
                .rewardsByMonth(rewardsByMonth)
                .build();
        when(rewardService.calculateReward(anyString())).thenReturn(rewardDto);

        // WHEN
        ResponseEntity<RewardDto> response = systemUnderTest.calculateReward("userId");

        // THEN
        assertAll(
                () -> assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode()),
                () -> assertEquals(rewardDto, response.getBody()),
                () -> verify(rewardService).calculateReward(anyString())
        );
    }
}
