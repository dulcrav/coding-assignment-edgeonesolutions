package pl.marcin.zubrzycki.rewards.service;

import org.springframework.stereotype.Component;
import pl.marcin.zubrzycki.rewards.api.dto.MonthlyRewardDto;
import pl.marcin.zubrzycki.rewards.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

@Component
class PointsCalculator {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100.0);
    private static final BigDecimal FIFTY = BigDecimal.valueOf(50.0);
    private static final int FIFTY_POINTS = 50;

    Integer calculatePoints(BigDecimal transactionAmount) {
        if (transactionAmount.compareTo(BigDecimal.ZERO) < 0) {
            return 0;
        }

        int points = 0;
        if (transactionAmount.compareTo(HUNDRED) > 0) {
            points += FIFTY_POINTS;
            BigDecimal additionalPointsToAdd = transactionAmount.subtract(HUNDRED);
            points += additionalPointsToAdd.intValue() * 2;
        } else if (transactionAmount.compareTo(FIFTY) > 0) {
            BigDecimal pointsToAdd = transactionAmount.subtract(FIFTY);
            points += pointsToAdd.intValue();
        }
        return points;
    }

    List<MonthlyRewardDto> calculatePointsByMonth(List<Transaction> allTransactions,
                                                  BiFunction<Integer, Transaction, Integer> reducer) {
        LocalDate maxDate = allTransactions.stream().map(Transaction::getDate).max(LocalDate::compareTo).get();
        LocalDate minDate = maxDate.minusMonths(2);

        List<MonthlyRewardDto> rewardsByMonth = new ArrayList<>();
        for (LocalDate date = minDate; !date.isAfter(maxDate); date = date.plusMonths(1)) {
            Month month = date.getMonth();
            int points = allTransactions.stream()
                    .filter(transaction -> transaction.getDate().getMonth() == month)
                    .reduce(0, reducer, Integer::sum);

            rewardsByMonth.add(createMonthlyReward(month.name(), points));
        }
        return rewardsByMonth;
    }

    private MonthlyRewardDto createMonthlyReward(String monthName, int points) {
        return MonthlyRewardDto
                .builder()
                .month(monthName)
                .points(points)
                .build();
    }
}
