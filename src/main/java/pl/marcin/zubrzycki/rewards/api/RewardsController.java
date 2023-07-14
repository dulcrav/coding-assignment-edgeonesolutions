package pl.marcin.zubrzycki.rewards.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marcin.zubrzycki.rewards.api.dto.RewardDto;
import pl.marcin.zubrzycki.rewards.service.RewardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reward")
public class RewardsController {

    private final RewardService rewardService;

    @GetMapping("/{userId}")
    ResponseEntity<RewardDto> calculateReward(@PathVariable String userId) {
        return new ResponseEntity<>(rewardService.calculateReward(userId), HttpStatus.OK);
    }
}
