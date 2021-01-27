package it.gov.pagopa.bpd.winning_transaction.resource.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WinningTransactionsOfTheDay {
    @ApiModelProperty(value = "${swagger.winningTransaction.date}", required = true)
    @JsonProperty(required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @ApiModelProperty(value = "${swagger.winningTransaction.count}", required = true)
    @JsonProperty(required = true)
    private Integer count;

    @ApiModelProperty(value = "${swagger.winningTransaction.transactions}", required = true)
    @JsonProperty(required = true)
    List<FindWinningTransactionResource> transactions;
}
