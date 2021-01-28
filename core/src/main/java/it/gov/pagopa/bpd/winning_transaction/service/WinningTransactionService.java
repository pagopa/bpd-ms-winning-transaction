package it.gov.pagopa.bpd.winning_transaction.service;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
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

    Page<WinningTransaction> getWinningTransactionsPage(String hpan, Long awardPeriodId, String fiscalCode, Pageable pageable);

    void deleteByFiscalCode(String fiscalCode);

    void reactivateForRollback(String fiscalCode, OffsetDateTime requestTimestamp);
}
