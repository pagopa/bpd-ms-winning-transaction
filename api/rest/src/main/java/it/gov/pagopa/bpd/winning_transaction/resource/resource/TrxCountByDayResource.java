package it.gov.pagopa.bpd.winning_transaction.resource.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrxCountByDayResource {

    @ApiModelProperty(value = "${swagger.winningTransaction.count}", required = true)
    @JsonProperty(required = true)
    private Long count;

    @ApiModelProperty(value = "${swagger.winningTransaction.date}", required = true)
    @JsonProperty(value = "trxDate", required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate trxDate;
}
