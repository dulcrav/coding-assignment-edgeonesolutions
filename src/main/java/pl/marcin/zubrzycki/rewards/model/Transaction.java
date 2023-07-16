package pl.marcin.zubrzycki.rewards.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private BigDecimal amount;

    private LocalDate date;

    private String userId;

    public Transaction(BigDecimal amount, LocalDate date, String userId) {
        this.amount = amount;
        this.date = date;
        this.userId = userId;
    }
}
