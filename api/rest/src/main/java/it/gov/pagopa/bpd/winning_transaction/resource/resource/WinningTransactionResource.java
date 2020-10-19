package it.gov.pagopa.bpd.winning_transaction.resource.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import it.gov.pagopa.bpd.winning_transaction.resource.enums.OperationType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"trxDate", "idTrxAcquirer", "acquirerCode"}, callSuper = false)
public class WinningTransactionResource {

    @ApiModelProperty(value = "${swagger.winningTransaction.hashPan}", required = true)
    @JsonProperty(required = true)
    private String hashPan;

    @ApiModelProperty(value = "${swagger.winningTransaction.idTrxAcquirer}", required = true)
    @JsonProperty(required = true)
    private String idTrxAcquirer;

    @ApiModelProperty(value = "${swagger.winningTransaction.idTrxIssuer}", required = true)
    @JsonProperty(required = true)
    private String idTrxIssuer;

    @ApiModelProperty(value = "${swagger.winningTransaction.score}", required = true)
    @JsonProperty(required = true)
    private BigDecimal score;

    @ApiModelProperty(value = "${swagger.winningTransaction.trxDate}", required = true)
    @JsonProperty(required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime trxDate;

    @ApiModelProperty(value = "${swagger.winningTransaction.mcc}", required = true)
    @JsonProperty(required = true)
    private String mcc;

    @ApiModelProperty(value = "${swagger.winningTransaction.operationType}", required = true)
    @JsonProperty(required = true)
    private OperationType operationType;

    @ApiModelProperty(value = "${swagger.winningTransaction.correlationId}", required = true)
    @JsonProperty(required = true)
    private String correlationId;

    @ApiModelProperty(value = "${swagger.winningTransaction.acquirerCode}", required = true)
    @JsonProperty(required = true)
    private String acquirerCode;

    @ApiModelProperty(value = "${swagger.winningTransaction.awardPeriodId}", required = true)
    @JsonProperty(required = true)
    private Long awardPeriodId;

}
