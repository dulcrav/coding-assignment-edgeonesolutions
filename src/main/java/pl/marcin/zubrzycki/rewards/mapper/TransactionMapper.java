package pl.marcin.zubrzycki.rewards.mapper;

import pl.marcin.zubrzycki.rewards.api.dto.NewTransactionDto;
import pl.marcin.zubrzycki.rewards.api.dto.TransactionDto;
import pl.marcin.zubrzycki.rewards.entity.Transaction;

public class TransactionMapper {
    public static TransactionDto mapFrom(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .date(transaction.getDate())
                .userId(transaction.getUserId())
                .build();
    }

    public static Transaction mapFrom(NewTransactionDto newTransactionDto) {
        return new Transaction(newTransactionDto.getAmount(),
                newTransactionDto.getDate(),
                newTransactionDto.getUserId());
    }
}
