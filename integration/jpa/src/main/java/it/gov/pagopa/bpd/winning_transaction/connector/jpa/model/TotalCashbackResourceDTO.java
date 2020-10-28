package it.gov.pagopa.bpd.winning_transaction.connector.jpa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TotalCashbackResourceDTO {

    @JsonProperty(required = true)
    @NotNull
    @NotBlank
    private BigDecimal totalCashback;

    @JsonProperty(required = true)
    @NotNull
    @NotBlank
    private Long transactionNumber;
}
