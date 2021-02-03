package it.gov.pagopa.bpd.winning_transaction.resource.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WinningTransactionPage<T> {
    @ApiModelProperty(value = "${swagger.winningTransaction.nextCursor}", required = true)
    @JsonProperty(value = "next_cursor")
    private Integer nextCursor;

    @ApiModelProperty(value = "${swagger.winningTransaction.WinningTransactionsOfTheDayList}", name = "WinningTransactionsOfTheDayList", required = true)
    @JsonProperty(required = true)
    List<T> transactions;
}
