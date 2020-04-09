package it.gov.pagopa.bpd.winning_transaction.model.resource;

import it.gov.pagopa.bpd.winning_transaction.model.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"trxDate", "idTrxAcquirer", "acquirerCode"},
        callSuper = false)
public class WinningTransactionResource {

    private String hashPan;
    private Number idTrxAcquirer;
    private String idTrxIssuer;
    private BigDecimal score;
    private ZonedDateTime trxDate;
    private String mcc;
    private OperationType operationType;
    private String correlationId;
    private String acquirerCode;
    private Long awardPeriodId;

}
