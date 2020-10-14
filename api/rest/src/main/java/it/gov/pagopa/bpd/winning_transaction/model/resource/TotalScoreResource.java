package it.gov.pagopa.bpd.winning_transaction.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"totalScore"}, callSuper = false)
public class TotalScoreResource {

    @ApiModelProperty(value = "${swagger.winningTransaction.totalScore}", required = true)
    @JsonProperty(required = true)
    private Long totalScore;

}
