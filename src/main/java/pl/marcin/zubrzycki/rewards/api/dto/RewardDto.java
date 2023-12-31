package pl.marcin.zubrzycki.rewards.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardDto {
    private Integer totalPoints;
    private List<MonthlyRewardDto> rewardsByMonth;
}
