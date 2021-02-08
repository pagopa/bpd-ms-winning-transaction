package it.gov.pagopa.bpd.winning_transaction.connector.jpa.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface WinningTransactionMilestone {
    Long getIdTrx();
    BigDecimal getCashbackNorm();
    Boolean getIsPivot();
    String getHashPan();
    String getIdTrxAcquirer();
    Timestamp getTrxDate();
    String getCircuitType();
    String getIdTrxIssuer();
    BigDecimal getAmount();
    BigDecimal getCashback();
    Long getAwardPeriodId();
}
