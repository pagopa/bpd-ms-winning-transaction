package it.gov.pagopa.bpd.winning_transaction.service;

import it.gov.pagopa.bpd.winning_transaction.WinningTransactionDAO;
import it.gov.pagopa.bpd.winning_transaction.model.entity.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.model.entity.WinningTransactionId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;

@Service
@Slf4j
public class WinningTransactionServiceImpl implements WinningTransactionService {

    private final WinningTransactionDAO winningTransactionDAO;

    @Autowired
    public WinningTransactionServiceImpl(WinningTransactionDAO winningTransactionDAO) {
        this.winningTransactionDAO = winningTransactionDAO;
    }

    @Override
    public WinningTransaction create(WinningTransaction winningTransaction) {
        if (log.isDebugEnabled()) {
            log.debug("#### WinningTransactionServiceImpl - create ####");
            log.debug(winningTransaction.toString());
        }
        if (!winningTransactionDAO.existsById(WinningTransactionId.builder()
                .idTrxAcquirer(winningTransaction.getIdTrxAcquirer())
                .acquirerCode(winningTransaction.getAcquirerCode())
                .trxDate(winningTransaction.getTrxDate())
                .build())) {
            return winningTransactionDAO.save(winningTransaction);
        }
        throw new EntityExistsException("WinningTransaction with id:" +
                winningTransaction.getIdTrxAcquirer() + "," +
                winningTransaction.getAcquirerCode() + "," +
                winningTransaction.getTrxDate() +" already exists");
    }

    @Override
    public List<WinningTransaction> getWinningTransactions(String hpan, Long awardPeriodId) {
        if (log.isDebugEnabled()) {
            log.debug("#### WinningTransactionServiceImpl - getWinningTransactions ####");
            log.debug("hpan: " + hpan);
            log.debug("awardPeriodId: " + awardPeriodId);
        }
        return winningTransactionDAO
                .findByHpanAndAwardPeriodIdAndAwardedTransaction(
                        hpan, awardPeriodId, true);
    }


    @Override
    public Long getTotalScore(String hpan, Long awardPeriodId) {
        if (log.isDebugEnabled()) {
            log.debug("#### WinningTransactionServiceImpl - getTotalScore ####");
            log.debug("hpan: " + hpan);
            log.debug("awardPeriodId: " + awardPeriodId);
        }
        return winningTransactionDAO.calculateTotalScore(hpan, awardPeriodId);
    }

}
