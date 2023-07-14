package pl.marcin.zubrzycki.rewards.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.marcin.zubrzycki.rewards.api.dto.EditTransactionDto;
import pl.marcin.zubrzycki.rewards.api.dto.NewTransactionDto;
import pl.marcin.zubrzycki.rewards.api.dto.TransactionDto;
import pl.marcin.zubrzycki.rewards.service.TransactionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    ResponseEntity<TransactionDto> createTransaction(@RequestBody NewTransactionDto newTransactionDto) {
        return new ResponseEntity<>(transactionService.createTransaction(newTransactionDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{transactionId}")
    ResponseEntity<TransactionDto> updateTransaction(@PathVariable Long transactionId, @RequestBody EditTransactionDto editTransactionDto) {
        return new ResponseEntity<>(transactionService.editTransaction(transactionId, editTransactionDto), HttpStatus.OK);
    }

}
