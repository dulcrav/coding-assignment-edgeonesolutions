package pl.marcin.zubrzycki.rewards.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.marcin.zubrzycki.rewards.api.dto.MonthlyRewardDto;
import pl.marcin.zubrzycki.rewards.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PointsCalculatorTest {

    private final PointsCalculator systemUnderTest = new PointsCalculator();

    @ParameterizedTest
    @MethodSource("calculatePointsArguments")
    void willCalculatePoints(BigDecimal amount, Integer expectedPoints) {
        // WHEN
        Integer result = systemUnderTest.calculatePoints(amount);

        // THEN
        assertEquals(expectedPoints, result);
    }

    @Test
    void willCalculatePointsByMonth() {
        // GIVEN
        List<Transaction> transactions = List.of(
                new Transaction(BigDecimal.valueOf(102.4), LocalDate.of(2023, 1, 1), "userId"),
                new Transaction(BigDecimal.valueOf(54.2), LocalDate.of(2023, 2, 1), "userId"),
                new Transaction(BigDecimal.valueOf(67.8), LocalDate.of(2023, 3, 1), "userId")
        );
        List<MonthlyRewardDto> expectedResult = List.of(
                MonthlyRewardDto.builder()
                        .points(54)
                        .month("JANUARY")
                        .build(),
                MonthlyRewardDto.builder()
                        .points(4)
                        .month("FEBRUARY")
                        .build(),
                MonthlyRewardDto.builder()
                        .points(17)
                        .month("MARCH")
                        .build()
        );

        BiFunction<Integer, Transaction, Integer> reducer = (partialPointsResult, transaction) -> partialPointsResult + systemUnderTest.calculatePoints(transaction.getAmount());

        // WHEN
        List<MonthlyRewardDto> result = systemUnderTest.calculatePointsByMonth(transactions, reducer);

        // THEN
        assertAll(
                () -> assertNotNull(result),
                () -> assertIterableEquals(expectedResult, result)
        );
    }

    private static Stream<Arguments> calculatePointsArguments() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-123.3), 0),
                Arguments.of(BigDecimal.valueOf(0), 0),
                Arguments.of(BigDecimal.valueOf(45.5), 0),
                Arguments.of(BigDecimal.valueOf(50.0), 0),
                Arguments.of(BigDecimal.valueOf(52.6), 2),
                Arguments.of(BigDecimal.valueOf(92.9), 42),
                Arguments.of(BigDecimal.valueOf(100.0), 50),
                Arguments.of(BigDecimal.valueOf(115.6), 80)
        );
    }
}
