package it.gov.pagopa.bpd.winning_transaction.command;

import it.gov.pagopa.bpd.winning_transaction.WinningTransactionDAO;
import it.gov.pagopa.bpd.winning_transaction.model.entity.WinningTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WinningTransactionServiceImpl implements WinningTransactionService {

    private final WinningTransactionDAO winningTransactionDAO;

    @Autowired
    public WinningTransactionServiceImpl(WinningTransactionDAO winningTransactionDAO) {
        this.winningTransactionDAO = winningTransactionDAO;
    }

    @Override
    public WinningTransaction create(WinningTransaction winningTransaction) {
        return winningTransactionDAO.save(winningTransaction);
    }

    @Override
    public List<WinningTransaction> getWinningTransactions(String hpan, Long awardPeriodId) {
        return winningTransactionDAO
                .findByHpanAndAwardPeriodIdAndAwardedTransaction(
                        hpan, awardPeriodId, true);
    }

}
