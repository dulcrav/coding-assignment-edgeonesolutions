package pl.marcin.zubrzycki.rewards.service;

import pl.marcin.zubrzycki.rewards.api.dto.RewardDto;

public interface RewardService {
    RewardDto calculateReward(String userId);
}
