package it.gov.pagopa.bpd.winning_transaction.connector.jpa.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface WinningTransactionMilestone {
    String getIdTrxAcquirer();
    OffsetDateTime getTrxDate();
    String getAcquirerCode();
    String getAcquirerId();
    String getOperationType();
    String getHashPan();
    String getCircuitType();
    BigDecimal getAmount();
    BigDecimal getCashback();
    Long getAwardPeriodId();
    String getIdTrxIssuer();
    Boolean getValid();

}
