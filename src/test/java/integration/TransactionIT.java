package integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import pl.marcin.zubrzycki.rewards.api.dto.EditTransactionDto;
import pl.marcin.zubrzycki.rewards.api.dto.NewTransactionDto;
import pl.marcin.zubrzycki.rewards.entity.Transaction;
import pl.marcin.zubrzycki.rewards.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TransactionIT extends BaseIT {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @SqlGroup({
            @Sql(value = "classpath:data/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void willCreateTransaction() {
        // GIVEN
        NewTransactionDto newTransactionDto = NewTransactionDto.builder()
                .amount(BigDecimal.valueOf(102.4))
                .date(LocalDate.of(2023, 1, 1))
                .userId("userId")
                .build();


        // WHEN
        webTestClient.post()
                .uri("/api/v1/transaction")
                .bodyValue(newTransactionDto)
                .exchange()
                .expectStatus().isCreated();


        List<Transaction> transactions = transactionRepository.findAll();

        // THEN
        assertAll(
                () -> assertNotNull(transactions),
                () -> assertEquals(1, transactions.size()),
                () -> assertEquals(1L, transactions.get(0).getId()),
                () -> assertThat(BigDecimal.valueOf(102.4)).isEqualByComparingTo(transactions.get(0).getAmount()),
                () -> assertEquals(LocalDate.of(2023, 1, 1), transactions.get(0).getDate()),
                () -> assertEquals("userId", transactions.get(0).getUserId())
        );
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:data/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:data/transactions.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void willUpdateExistingTransaction() {
        // GIVEN
        EditTransactionDto editTransactionDto = EditTransactionDto.builder()
                .amount(BigDecimal.valueOf(254.5))
                .build();


        // WHEN
        webTestClient.patch()
                .uri("/api/v1/transaction/1")
                .bodyValue(editTransactionDto)
                .exchange()
                .expectStatus().isOk();

        List<Transaction> transactions = transactionRepository.findAll();

        // THEN
        assertAll(
                () -> assertNotNull(transactions),
                () -> assertEquals(1, transactions.size()),
                () -> assertEquals(1L, transactions.get(0).getId()),
                () -> assertThat(BigDecimal.valueOf(254.5)).isEqualByComparingTo(transactions.get(0).getAmount()),
                () -> assertEquals(LocalDate.of(2023, 1, 1), transactions.get(0).getDate()),
                () -> assertEquals("1", transactions.get(0).getUserId())
        );
    }

    @ParameterizedTest
    @MethodSource("invalidBodyForCreatingTransactionArguments")
    void willThrowExceptionCreatingTransactionWhenRequiredFieldsEmpty(NewTransactionDto newTransactionDto) {
        // GIVEN WHEN THEN
        webTestClient.post()
                .uri("/api/v1/transaction")
                .bodyValue(newTransactionDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @ParameterizedTest
    @MethodSource("invalidBodyForUpdatingTransactionArguments")
    void willThrowExceptionUpdatingTransactionWhenRequiredFieldsEmpty(EditTransactionDto editTransactionDto) {
        // GIVEN WHEN THEN
        webTestClient.patch()
                .uri("/api/v1/transaction/1")
                .bodyValue(editTransactionDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    private static Stream<Arguments> invalidBodyForCreatingTransactionArguments() {
        return Stream.of(
                Arguments.of(NewTransactionDto.builder()
                        .amount(BigDecimal.valueOf(-102.4))
                        .date(LocalDate.of(2023, 1, 1))
                        .userId("userId")
                        .build()),
                Arguments.of(NewTransactionDto.builder()
                        .date(LocalDate.of(2023, 1, 1))
                        .userId("userId")
                        .build()),
                Arguments.of(NewTransactionDto.builder()
                        .amount(BigDecimal.valueOf(102.4))
                        .userId("userId")
                        .build()),
                Arguments.of(NewTransactionDto.builder()
                        .amount(BigDecimal.valueOf(102.4))
                        .date(LocalDate.of(2023, 1, 1))
                        .build()),
                Arguments.of(NewTransactionDto.builder().build())
        );
    }

    private static Stream<Arguments> invalidBodyForUpdatingTransactionArguments() {
        return Stream.of(
                Arguments.of(EditTransactionDto.builder()
                        .amount(BigDecimal.valueOf(-102.4))
                        .build()),
                Arguments.of(EditTransactionDto.builder()
                        .build())
        );
    }
}
