package pl.marcin.zubrzycki.rewards.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import pl.marcin.zubrzycki.rewards.api.dto.EditTransactionDto;
import pl.marcin.zubrzycki.rewards.api.dto.NewTransactionDto;
import pl.marcin.zubrzycki.rewards.api.dto.TransactionDto;
import pl.marcin.zubrzycki.rewards.entity.Transaction;
import pl.marcin.zubrzycki.rewards.error.TransactionNotFound;
import pl.marcin.zubrzycki.rewards.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl systemUnderTest;

    @Test
    void willCreateTransaction() {
        // GIVEN
        NewTransactionDto newTransactionDto = NewTransactionDto.builder()
                .amount(BigDecimal.valueOf(102.4))
                .date(LocalDate.of(2023, 1, 1))
                .userId("userId")
                .build();
        Transaction transactionToBeCreated = new Transaction(BigDecimal.valueOf(102.4), LocalDate.of(2023, 1, 1), "userId");
        TransactionDto expectedResult = TransactionDto.builder()
                .amount(BigDecimal.valueOf(102.4))
                .date(LocalDate.of(2023, 1, 1))
                .userId("userId")
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionToBeCreated);

        // WHEN
        TransactionDto result = systemUnderTest.createTransaction(newTransactionDto);

        // THEN
        assertAll(
                () -> verify(transactionRepository).save(any(Transaction.class)),
                () -> assertNotNull(result),
                () -> assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult)
        );
    }

    @Test
    void willEditTransaction() {
        // GIVEN
        Transaction transaction = new Transaction(BigDecimal.valueOf(102.4), LocalDate.of(2023, 1, 1), "userId");
        EditTransactionDto editTransactionDto = EditTransactionDto.builder()
                .amount(BigDecimal.valueOf(205.5))
                .build();
        Transaction updatedTransaction = new Transaction(BigDecimal.valueOf(205.5), LocalDate.of(2023, 1, 1), "userId");
        TransactionDto expectedResult = TransactionDto.builder()
                .amount(BigDecimal.valueOf(205.5))
                .date(LocalDate.of(2023, 1, 1))
                .userId("userId")
                .build();

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(transaction)).thenReturn(updatedTransaction);

        // WHEN
        TransactionDto result = systemUnderTest.editTransaction(1L, editTransactionDto);

        // THEN
        assertAll(
                () -> assertNotNull(result),
                () -> verify(transactionRepository).save(any(Transaction.class)),
                () -> assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult)
        );
    }

    @Test
    void willThrowExceptionWhenTransactionNotFound() {
        // GIVEN
        EditTransactionDto editTransactionDto = EditTransactionDto.builder()
                .amount(BigDecimal.valueOf(102.4))
                .build();

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // WHEN THEN
        assertThrows(TransactionNotFound.class,
                () -> systemUnderTest.editTransaction(1L, editTransactionDto),
                "Transaction with ID: 1 not found.");

    }
}
