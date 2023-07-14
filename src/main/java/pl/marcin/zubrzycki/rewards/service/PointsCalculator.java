package pl.marcin.zubrzycki.rewards.service;

import org.springframework.stereotype.Component;
import pl.marcin.zubrzycki.rewards.api.dto.MonthlyRewardDto;
import pl.marcin.zubrzycki.rewards.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

@Component
public class PointsCalculator {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100.0);
    private static final BigDecimal FIFTY = BigDecimal.valueOf(50.0);
    private static final int FIFTY_POINTS = 50;

    Integer calculatePoints(Transaction transaction) {
        int points = 0;
        BigDecimal amount = transaction.getAmount();
        if (amount.compareTo(HUNDRED) > 0) {
            points += FIFTY_POINTS;
            BigDecimal additionalPointsToAdd = amount.subtract(HUNDRED);
            points += additionalPointsToAdd.intValue() * 2;
        } else if (amount.compareTo(FIFTY) > 0) {
            BigDecimal pointsToAdd = amount.subtract(FIFTY);
            points += pointsToAdd.intValue();
        }
        return points;
    }

    List<MonthlyRewardDto> calculatePointsByMonth(LocalDate minDate, LocalDate maxDate, List<Transaction> allTransactions,
                                                  BiFunction<Integer, Transaction, Integer> reducer) {
        List<MonthlyRewardDto> rewardsByMonth = new ArrayList<>();
        IntStream.range(minDate.getMonthValue(), maxDate.getMonthValue() + 1).forEach(monthIndex -> {
            Month month = Month.of(monthIndex);
            int points = allTransactions.stream()
                    .filter(transaction -> transaction.getDate().getMonth() == month)
                    .reduce(0, reducer, Integer::sum);
            MonthlyRewardDto monthlyReward = MonthlyRewardDto
                    .builder()
                    .month(month.name())
                    .points(points)
                    .build();
            rewardsByMonth.add(monthlyReward);
        });
        return rewardsByMonth;
    }
}
