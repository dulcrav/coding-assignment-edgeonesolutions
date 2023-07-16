package pl.marcin.zubrzycki.rewards.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditTransactionDto {

    @DecimalMin(value = "0.0")
    @NotNull
    private BigDecimal amount;
}
