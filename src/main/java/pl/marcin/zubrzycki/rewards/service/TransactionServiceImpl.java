package pl.marcin.zubrzycki.rewards.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.marcin.zubrzycki.rewards.api.dto.EditTransactionDto;
import pl.marcin.zubrzycki.rewards.api.dto.NewTransactionDto;
import pl.marcin.zubrzycki.rewards.api.dto.TransactionDto;
import pl.marcin.zubrzycki.rewards.model.Transaction;
import pl.marcin.zubrzycki.rewards.exception.TransactionNotFound;
import pl.marcin.zubrzycki.rewards.mapper.TransactionMapper;
import pl.marcin.zubrzycki.rewards.repository.TransactionRepository;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public TransactionDto createTransaction(NewTransactionDto newTransactionDto) {
        Transaction transaction = TransactionMapper.mapFrom(newTransactionDto);
        Transaction savedTransaction = transactionRepository.save(transaction);

        return TransactionMapper.mapFrom(savedTransaction);
    }

    @Override
    public TransactionDto editTransaction(Long transactionId, EditTransactionDto editTransactionDto) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFound(transactionId));

        transaction.setAmount(editTransactionDto.getAmount());
        Transaction updatedTransaction = transactionRepository.save(transaction);

        return TransactionMapper.mapFrom(updatedTransaction);
    }
}
