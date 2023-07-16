package pl.marcin.zubrzycki.rewards.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.marcin.zubrzycki.rewards.api.dto.MonthlyRewardDto;
import pl.marcin.zubrzycki.rewards.api.dto.RewardDto;
import pl.marcin.zubrzycki.rewards.entity.Transaction;
import pl.marcin.zubrzycki.rewards.error.UserNotFound;
import pl.marcin.zubrzycki.rewards.repository.TransactionRepository;

import java.util.List;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final TransactionRepository transactionRepository;
    private final PointsCalculator pointsCalculator;


    @Override
    public RewardDto calculateReward(String userId) {
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);
        if (transactions.isEmpty()) {
            throw new UserNotFound(userId);
        }

        BiFunction<Integer, Transaction, Integer> reducer = (partialPointsResult, transaction) -> partialPointsResult + pointsCalculator.calculatePoints(transaction.getAmount());

        int totalPoints = transactions.stream()
                .reduce(0, reducer, Integer::sum);

        List<MonthlyRewardDto> rewardsByMonth = pointsCalculator.calculatePointsByMonth(transactions, reducer);

        return RewardDto.builder()
                .totalPoints(totalPoints)
                .rewardsByMonth(rewardsByMonth)
                .build();
    }
}
