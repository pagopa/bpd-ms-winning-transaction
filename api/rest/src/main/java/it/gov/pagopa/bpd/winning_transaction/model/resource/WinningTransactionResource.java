package it.gov.pagopa.bpd.winning_transaction.model.resource;

import it.gov.pagopa.bpd.winning_transaction.model.enums.OperationType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"trxDate", "idTrxAcquirer", "acquirerCode"},
        callSuper = false)
public class WinningTransactionResource {

    private String hashPan;
    private Number idTrxAcquirer;
    private String idTrxIssuer;
    private BigDecimal score;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime trxDate;
    private String mcc;
    private OperationType operationType;
    private String correlationId;
    private String acquirerCode;
    private Long awardPeriodId;

}
