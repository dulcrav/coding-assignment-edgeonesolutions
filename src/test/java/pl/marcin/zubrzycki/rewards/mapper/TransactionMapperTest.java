package pl.marcin.zubrzycki.rewards.mapper;

import org.junit.jupiter.api.Test;
import pl.marcin.zubrzycki.rewards.api.dto.NewTransactionDto;
import pl.marcin.zubrzycki.rewards.api.dto.TransactionDto;
import pl.marcin.zubrzycki.rewards.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTest {

    @Test
    void willMapFromTransaction() {
        // GIVEN
        Transaction transaction = new Transaction(BigDecimal.valueOf(104.1), LocalDate.of(2023, 1, 1), "userId");
        TransactionDto expectedResult = TransactionDto.builder()
                .id(null)
                .amount(BigDecimal.valueOf(104.1))
                .date(LocalDate.of(2023, 1, 1))
                .userId("userId")
                .build();

        // WHEN
        TransactionDto result = TransactionMapper.mapFrom(transaction);

        // THEN
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    void willMapFromNewTransationDto() {
        // GIVEN
        NewTransactionDto newTransactionDto = NewTransactionDto.builder()
                .amount(BigDecimal.valueOf(104.1))
                .date(LocalDate.of(2023, 1, 1))
                .userId("userId")
                .build();
        Transaction expectedResult = new Transaction(BigDecimal.valueOf(104.1), LocalDate.of(2023, 1, 1), "userId");

        // WHEN
        Transaction result = TransactionMapper.mapFrom(newTransactionDto);

        // THEN
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
    }
}
