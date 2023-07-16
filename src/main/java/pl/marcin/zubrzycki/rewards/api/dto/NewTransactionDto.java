package pl.marcin.zubrzycki.rewards.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewTransactionDto {

    @DecimalMin("0.0")
    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDate date;

    @NotBlank
    private String userId;
}
