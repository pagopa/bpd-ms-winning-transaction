package it.gov.pagopa.bpd.winning_transaction.command;

import it.gov.pagopa.bpd.winning_transaction.model.entity.WinningTransaction;

import java.util.List;

public interface WinningTransactionService {

    WinningTransaction create(WinningTransaction winningTransaction);

    List<WinningTransaction> getWinningTransactions(String hpan, Long awardPeriodId);

}
