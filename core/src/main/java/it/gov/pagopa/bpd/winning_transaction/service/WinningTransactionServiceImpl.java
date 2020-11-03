package it.gov.pagopa.bpd.winning_transaction.service;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.WinningTransactionDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @See WinningTransactionService
 */
@Service
@Slf4j
public class WinningTransactionServiceImpl implements WinningTransactionService {

    private final WinningTransactionDAO winningTransactionDAO;

    @Value(value = "${winningTransaction.core.checkExists.enable}")
    private boolean checkExists;

    @Autowired
    public WinningTransactionServiceImpl(
            WinningTransactionDAO winningTransactionDAO) {
        this.winningTransactionDAO = winningTransactionDAO;
    }

    @Override
    public WinningTransaction create(WinningTransaction winningTransaction) {
        if (log.isDebugEnabled()) {
            log.debug("WinningTransactionServiceImpl.create");
            log.debug("winningTransaction = [" + winningTransaction + "]");
        }
        final WinningTransactionId id = WinningTransactionId.builder()
                .idTrxAcquirer(winningTransaction.getIdTrxAcquirer())
                .acquirerCode(winningTransaction.getAcquirerCode())
                .trxDate(winningTransaction.getTrxDate())
                .build();
        if (checkExists && winningTransactionDAO.existsById(id)) {
            throw new WinningTransactionExistsException(id);
        }
        return winningTransactionDAO.save(winningTransaction);

    }

    @Override
    public List<WinningTransaction> getWinningTransactions(String hpan, Long awardPeriodId) {
        if (log.isDebugEnabled()) {
            log.debug("WinningTransactionServiceImpl.getWinningTransactions");
            log.debug("hpan = [" + hpan + "], awardPeriodId = [" + awardPeriodId + "]");
        }

        List<WinningTransaction> winningTransactions = new ArrayList<>();

        if (hpan != null && !hpan.isEmpty()) {
            winningTransactions = winningTransactionDAO.findByHpanAndAwardPeriodId(hpan, awardPeriodId);
        } else {
            winningTransactions = winningTransactionDAO.findByAwardPeriodId(awardPeriodId);
        }

        return winningTransactions;
    }

}
