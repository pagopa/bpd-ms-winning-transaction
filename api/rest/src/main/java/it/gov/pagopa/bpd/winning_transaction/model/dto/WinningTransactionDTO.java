package it.gov.pagopa.bpd.winning_transaction.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"idTrxAcquirer", "acquirerCode", "trxDate"}, callSuper = false)
public class WinningTransactionDTO {

    @ApiModelProperty(value = "${swagger.winningTransaction.idTrxAcquirer}", required = true)
    @JsonProperty(required = true)
    @NotNull
    String idTrxAcquirer;

    @ApiModelProperty(value = "${swagger.winningTransaction.acquirerCode}", required = true)
    @JsonProperty(required = true)
    @NotNull
    @NotBlank
    @Size(max = 20)
    String acquirerCode;

    @ApiModelProperty(value = "${swagger.winningTransaction.trxDate}", required = true)
    @JsonProperty(required = true)
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    OffsetDateTime trxDate;

    @ApiModelProperty(value = "${swagger.winningTransaction.hpan}", required = true)
    @JsonProperty(required = true)
    @Size(max = 64)
    String hpan;

    @ApiModelProperty(value = "${swagger.winningTransaction.operationType}", required = true)
    @JsonProperty(required = true)
    @Size(max = 5)
    String operationType;

    @Size(max = 5)
    String circuitType;

    @ApiModelProperty(value = "${swagger.winningTransaction.idTrxIssuer}")
    @JsonProperty
    String idTrxIssuer;

    @ApiModelProperty(value = "${swagger.winningTransaction.correlationId}", required = true)
    @JsonProperty(required = true)
    String correlationId;

    @ApiModelProperty(value = "${swagger.winningTransaction.amount}", required = true)
    @JsonProperty(required = true)
    BigDecimal amount;

    @Size(max = 3)
    String amountCurrency;

    @ApiModelProperty(value = "${swagger.winningTransaction.mcc}", required = true)
    @JsonProperty(required = true)
    @Size(max = 5)
    String mcc;

    @Size(max = 40)
    String mccDescription;

    @ApiModelProperty(value = "${swagger.winningTransaction.score}", required = true)
    @JsonProperty(required = true)
    BigDecimal score;

    @ApiModelProperty(value = "${swagger.winningTransaction.awardPeriodId}", required = true)
    @JsonProperty(required = true)
    Long awardPeriodId;

    @ApiModelProperty(value = "${swagger.winningTransaction.acquirerId}", required = true)
    @JsonProperty(required = true)
    String acquirerId;

    @ApiModelProperty(value = "${swagger.winningTransaction.merchantId}", required = true)
    @JsonProperty(required = true)
    String merchantId;

    @NotNull
    @NotBlank
    @ApiModelProperty(value = "${swagger.winningTransaction.terminalId}", required = true)
    @JsonProperty(required = true)
    String terminalId;

    @NotNull
    @NotBlank
    @Pattern(regexp = "([0-9]{6}|[0-9]{8})")
    @ApiModelProperty(value = "${swagger.winningTransaction.bin}", required = true)
    @JsonProperty(required = true)
    String bin;

}
