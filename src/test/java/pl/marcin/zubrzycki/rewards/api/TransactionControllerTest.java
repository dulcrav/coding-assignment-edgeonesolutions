package pl.marcin.zubrzycki.rewards.api;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import pl.marcin.zubrzycki.rewards.api.dto.EditTransactionDto;
import pl.marcin.zubrzycki.rewards.api.dto.NewTransactionDto;
import pl.marcin.zubrzycki.rewards.api.dto.TransactionDto;
import pl.marcin.zubrzycki.rewards.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController systemUnderTest;

    @Test
    void willCreateTransaction() {
        // GIVEN
        NewTransactionDto newTransactionDto = NewTransactionDto
                .builder()
                .amount(BigDecimal.valueOf(101.4))
                .date(LocalDate.of(2023, 1, 1))
                .userId("userId")
                .build();
        TransactionDto transactionDto = TransactionDto.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(101.4))
                .date(LocalDate.of(2023, 1, 1))
                .userId("userId")
                .build();
        when(transactionService.createTransaction(newTransactionDto)).thenReturn(transactionDto);

        // WHEN
        ResponseEntity<TransactionDto> response = systemUnderTest.createTransaction(newTransactionDto);

        // THEN
        assertAll(
                () -> assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode()),
                () -> assertEquals(transactionDto, response.getBody()),
                () -> verify(transactionService).createTransaction(newTransactionDto)
        );
    }

    @Test
    void willUpdateTransaction() {
        // GIVEN
        TransactionDto transactionDto = TransactionDto.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(105.4))
                .date(LocalDate.of(2023, 1, 1))
                .userId("userId")
                .build();
        EditTransactionDto editTransactionDto = EditTransactionDto.builder()
                .amount(BigDecimal.valueOf(105.4))
                .build();

        when(transactionService.editTransaction(1L, editTransactionDto)).thenReturn(transactionDto);

        // WHEN
        ResponseEntity<TransactionDto> response = systemUnderTest.updateTransaction(1L, editTransactionDto);

        // THEN
        assertAll(
                () -> assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode()),
                () -> assertEquals(transactionDto, response.getBody()),
                () -> verify(transactionService).editTransaction(1L, editTransactionDto)
        );
    }

}
