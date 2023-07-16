package pl.marcin.zubrzycki.rewards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransactionNotFound extends RuntimeException {
    public TransactionNotFound(Long transactionId) {
        super("Transaction with ID: " + transactionId + " not found.");
    }
}
