package it.gov.pagopa.bpd.winning_transaction.resource.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WinningTransactionsOfTheDay {
    @ApiModelProperty(value = "${swagger.winningTransaction.date}", required = true)
    @JsonProperty(required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime date;

    @ApiModelProperty(value = "${swagger.winningTransaction.count}", required = true)
    @JsonProperty(required = true)
    private Integer count;

    @ApiModelProperty(value = "${swagger.winningTransaction.transactions}", required = true)
    @JsonProperty(required = true)
    List<FindWinningTransactionResource> transactions;
}
