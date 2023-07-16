package pl.marcin.zubrzycki.rewards.repository;

import org.springframework.data.repository.CrudRepository;
import pl.marcin.zubrzycki.rewards.model.Transaction;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findAllByUserId(String userId);
    List<Transaction> findAll();
}
