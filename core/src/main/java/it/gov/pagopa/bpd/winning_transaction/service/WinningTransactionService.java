package it.gov.pagopa.bpd.winning_transaction.service;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.TrxCountByDay;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionMilestone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * A service to manage the Business Logic related to WinningTransaction
 */
public interface WinningTransactionService {

    WinningTransaction create(WinningTransaction winningTransaction);

    List<WinningTransaction> getWinningTransactions(String hpan, Long awardPeriodId, String fiscalCode);

    Page<WinningTransactionMilestone> getWinningTransactionsMilestonePage(String hpan, Long awardPeriodId, String fiscalCode, Pageable pageable);

    List<TrxCountByDay> getWinningTransactionByDateCount(String hpan, Long awardPeriodId, String fiscalCode);

    void deleteByFiscalCode(String fiscalCode);

    void deleteByFiscalCodeIfNotUpdated(String fiscalCode, OffsetDateTime updateTime);

    void reactivateForRollback(String fiscalCode, OffsetDateTime requestTimestamp);
}
