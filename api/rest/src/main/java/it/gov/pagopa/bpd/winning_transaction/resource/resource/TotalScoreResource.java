package it.gov.pagopa.bpd.winning_transaction.resource.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"totalScore"}, callSuper = false)
public class TotalScoreResource {

    @ApiModelProperty(value = "${swagger.winningTransaction.totalScore}", required = true)
    @JsonProperty(required = true)
    private BigDecimal totalScore;

    @ApiModelProperty(value = "${swagger.winningTransaction.transactionNumber}", required = true)
    @JsonProperty(required = true)
    private Long transactionNumber;
}
