package pl.marcin.zubrzycki.rewards.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import pl.marcin.zubrzycki.rewards.api.dto.MonthlyRewardDto;
import pl.marcin.zubrzycki.rewards.api.dto.RewardDto;
import pl.marcin.zubrzycki.rewards.entity.Transaction;
import pl.marcin.zubrzycki.rewards.error.UserNotFound;
import pl.marcin.zubrzycki.rewards.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class RewardServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PointsCalculator pointsCalculator;

    @InjectMocks
    private RewardServiceImpl systemUnderTest;

    @Test
    void willCalculateReward() {
        // GIVEN
        Transaction transaction = new Transaction(BigDecimal.valueOf(102.2), LocalDate.of(2023, 1, 1), "userId");
        List<Transaction> transactions = List.of(transaction);
        List<MonthlyRewardDto> rewardsByMonth = List.of(
                MonthlyRewardDto.builder()
                        .month("NOVEMBER")
                        .points(0)
                        .build(),
                MonthlyRewardDto.builder()
                        .month("DECEMBER")
                        .points(0)
                        .build(),
                MonthlyRewardDto.builder()
                        .month("JANUARY")
                        .points(54)
                        .build());

        when(transactionRepository.findAllByUserId("userId")).thenReturn(transactions);
        when(pointsCalculator.calculatePoints(BigDecimal.valueOf(102.2))).thenReturn(54);
        when(pointsCalculator.calculatePointsByMonth(anyList(), any())).thenReturn(rewardsByMonth);

        // WHEN
        RewardDto result = systemUnderTest.calculateReward("userId");

        // THEN
        assertAll(
                () -> assertNotNull(result),
                () -> verify(transactionRepository).findAllByUserId("userId"),
                () -> assertEquals(54, result.getTotalPoints()),
                () -> assertIterableEquals(rewardsByMonth, result.getRewardsByMonth())
        );
    }

    @Test
    void willThrowExceptionWhenUserNotFound() {
        // GIVEN
        when(transactionRepository.findAllByUserId(anyString())).thenReturn(Collections.emptyList());

        // WHEN THEN
        assertThrows(UserNotFound.class,
                () -> systemUnderTest.calculateReward("userId"),
                "User with ID: userId not found.");
    }
}
