package pl.marcin.zubrzycki.rewards.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.marcin.zubrzycki.rewards.api.dto.MonthlyRewardDto;
import pl.marcin.zubrzycki.rewards.api.dto.RewardDto;
import pl.marcin.zubrzycki.rewards.entity.Transaction;
import pl.marcin.zubrzycki.rewards.error.UserNotFound;
import pl.marcin.zubrzycki.rewards.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        BiFunction<Integer, Transaction, Integer> reducer = (partialPointsResult, transaction) -> partialPointsResult + pointsCalculator.calculatePoints(transaction);

        int totalPoints = transactions.stream()
                .reduce(0, reducer, Integer::sum);

        LocalDate maxDate = transactions.stream().map(Transaction::getDate).max(LocalDate::compareTo).get();
        LocalDate minDate = maxDate.minusMonths(2);

        List<MonthlyRewardDto> rewardsByMonth = pointsCalculator.calculatePointsByMonth(minDate, maxDate, transactions, reducer);

        return RewardDto.builder()
                .totalPoints(totalPoints)
                .rewardsByMonth(rewardsByMonth)
                .build();
    }
}
