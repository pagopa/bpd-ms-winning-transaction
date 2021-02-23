package it.gov.pagopa.bpd.winning_transaction.resource.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"trxDate", "idTrxAcquirer", "idTrx"}, callSuper = false)
public class WinningTransactionMilestoneResource {

    @ApiModelProperty(value = "${swagger.winningTransaction.hashPan}", required = true)
    @JsonProperty(required = true)
    private String hashPan;

    @ApiModelProperty(value = "${swagger.winningTransaction.idTrxAcquirer}", required = true)
    @JsonProperty(required = true)
    private String idTrxAcquirer;

    @ApiModelProperty(value = "${swagger.winningTransaction.idTrxIssuer}", required = true)
    @JsonProperty(required = true)
    private String idTrxIssuer;

    @ApiModelProperty(value = "${swagger.winningTransaction.amount}", required = true)
    @JsonProperty(required = true)
    private BigDecimal amount;

    @ApiModelProperty(value = "${swagger.winningTransaction.trxDate}", required = true)
    @JsonProperty(required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime trxDate;

    @ApiModelProperty(value = "${swagger.winningTransaction.cashback}", required = true)
    @JsonProperty(required = true)
    private BigDecimal cashback;

    @ApiModelProperty(value = "${swagger.winningTransaction.circuitType}", required = true)
    @JsonProperty(required = true)
    private String circuitType;

    @ApiModelProperty(value = "${swagger.winningTransaction.awardPeriodId}", required = true)
    @JsonProperty(required = true)
    private Long awardPeriodId;

    @ApiModelProperty(value = "${swagger.winningTransaction.idTrx}", required = true)
    private String idTrx;
}
