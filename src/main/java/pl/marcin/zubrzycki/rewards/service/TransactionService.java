package pl.marcin.zubrzycki.rewards.service;

import pl.marcin.zubrzycki.rewards.api.dto.EditTransactionDto;
import pl.marcin.zubrzycki.rewards.api.dto.NewTransactionDto;
import pl.marcin.zubrzycki.rewards.api.dto.TransactionDto;

public interface TransactionService {
    TransactionDto createTransaction(NewTransactionDto newTransactionDto);

    TransactionDto editTransaction(Long transactionId, EditTransactionDto editTransactionDto);
}
