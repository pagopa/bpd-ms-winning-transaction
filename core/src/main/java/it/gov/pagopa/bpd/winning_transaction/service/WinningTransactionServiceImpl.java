package it.gov.pagopa.bpd.winning_transaction.service;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.WinningTransactionDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionId;
import it.gov.pagopa.bpd.winning_transaction.exception.WinningTransactionExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
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
    public List<WinningTransaction> getWinningTransactions(String hpan, Long awardPeriodId, String fiscalCode) {
        if (log.isDebugEnabled()) {
            log.debug("WinningTransactionServiceImpl.getWinningTransactions");
            log.debug("hpan = [" + hpan + "], awardPeriodId = [" + awardPeriodId + "]");
        }

        List<WinningTransaction> winningTransactions = new ArrayList<>();
        winningTransactions = hpan != null? winningTransactionDAO.findCitizenTransactionsByHpan(fiscalCode,awardPeriodId,hpan)
        : winningTransactionDAO.findCitizenTransactions(fiscalCode, awardPeriodId);

        return winningTransactions;
    }

    @Override
    public void deleteByFiscalCode(String fiscalCode) {
        if (log.isDebugEnabled()) {
            log.debug("WinningTransactionServiceImpl.deleteByFiscalCode");
            log.debug("fiscalCode = [" + fiscalCode + "]");
        }
        winningTransactionDAO.deactivateCitizenTransactions(fiscalCode, OffsetDateTime.now());
    }

    @Override
    public void reactivateForRollback(String fiscalCode, OffsetDateTime requestTimestamp) {
        if (log.isDebugEnabled()) {
            log.debug("WinningTransactionServiceImpl.reactivateForRollback");
            log.debug("fiscalCode = [" + fiscalCode + "], requestTimestamp = [" + requestTimestamp + "]");
        }
        winningTransactionDAO.reactivateForRollback(fiscalCode, requestTimestamp, OffsetDateTime.now());
    }

}
