package it.gov.pagopa.bpd.winning_transaction.service;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.WinningTransactionDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.WinningTransactionReplicaDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.WinningTransactionTransferDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.TrxCountByDay;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionMilestone;
import it.gov.pagopa.bpd.winning_transaction.exception.WinningTransactionExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final WinningTransactionReplicaDAO winningTransactionReplicaDAO;
    private final WinningTransactionTransferDAO winningTransactionTransferDAO;

    @Autowired
    public WinningTransactionServiceImpl(
            ObjectProvider<WinningTransactionDAO> winningTransactionDAO,
            ObjectProvider<WinningTransactionReplicaDAO> winningTransactionReplicaDAO,
            ObjectProvider<WinningTransactionTransferDAO> winningTransactionTransferDAO) {
        this.winningTransactionDAO = winningTransactionDAO.getIfAvailable();
        this.winningTransactionReplicaDAO = winningTransactionReplicaDAO.getIfAvailable();
        this.winningTransactionTransferDAO = winningTransactionTransferDAO.getIfAvailable();
    }

    @Override
    public WinningTransaction create(WinningTransaction winningTransaction) {
        if (log.isDebugEnabled()) {
            log.debug("WinningTransactionServiceImpl.create");
            log.debug("winningTransaction = [" + winningTransaction + "]");
        }
        try {
            return winningTransactionDAO.save(winningTransaction);
        } catch (DataIntegrityViolationException e) {
            throw new WinningTransactionExistsException(winningTransaction.getId());
        }

    }

    @Override
    public List<WinningTransaction> getWinningTransactions(String hpan, Long awardPeriodId, String fiscalCode) {
        if (log.isDebugEnabled()) {
            log.debug("WinningTransactionServiceImpl.getWinningTransactions");
            log.debug("hpan = [" + hpan + "], awardPeriodId = [" + awardPeriodId + "]");
        }

        List<WinningTransaction> winningTransactions = new ArrayList<>();
        winningTransactions = hpan != null ? winningTransactionReplicaDAO.findCitizenTransactionsByHpan(fiscalCode, awardPeriodId, hpan)
                : winningTransactionReplicaDAO.findCitizenTransactions(fiscalCode, awardPeriodId);

        return winningTransactions;
    }

    @Override
    public Page<WinningTransactionMilestone> getWinningTransactionsMilestonePage(String hpan, Long awardPeriodId, String fiscalCode, Pageable pageable) {
        if (log.isDebugEnabled()) {
            log.debug("WinningTransactionServiceImpl.getWinningTransactionsMilestonePage");
            log.debug("hpan = [" + hpan + "], awardPeriodId = [" + awardPeriodId + "]," +
                    " fiscalCode = [" + fiscalCode + "], page = [" + pageable.getPageNumber() + "], size = [" + pageable.getPageSize() + "]");
        }

        return hpan != null ? winningTransactionReplicaDAO.findCitizenTransactionsMilestoneByHpanPage(fiscalCode, awardPeriodId, hpan, pageable)
                : winningTransactionReplicaDAO.findCitizenTransactionsMilestonePage(fiscalCode, awardPeriodId, pageable);
    }

    @Override
    public List<TrxCountByDay> getWinningTransactionByDateCount(String hpan, Long awardPeriodId, String fiscalCode) {
        if (log.isDebugEnabled()) {
            log.debug("WinningTransactionServiceImpl.getWinningTransactionByDateCount");
        }
        return hpan != null ? winningTransactionReplicaDAO.findCitizenTransactionsByDateCountHpan(fiscalCode, awardPeriodId, hpan)
                : winningTransactionReplicaDAO.findCitizenTransactionsByDateCount(fiscalCode, awardPeriodId);
    }

    @Override
    @Transactional("transactionManagerPrimary")
    public void deleteByFiscalCode(String fiscalCode) {
        if (log.isDebugEnabled()) {
            log.debug("WinningTransactionServiceImpl.deleteByFiscalCode");
            log.debug("fiscalCode = [" + fiscalCode + "]");
        }
        winningTransactionDAO.deactivateCitizenTransactions(fiscalCode, OffsetDateTime.now());
        winningTransactionTransferDAO.deleteByFiscalCode(fiscalCode);
    }

    @Override
    public void deleteByFiscalCodeIfNotUpdated(String fiscalCode, OffsetDateTime updateTime) {
        if (log.isDebugEnabled()) {
            log.debug("WinningTransactionServiceImpl.deleteByFiscalCodeIfNotUpdated");
            log.debug("fiscalCode = [" + fiscalCode + "]");
        }
        winningTransactionDAO.deactivateCitizenTransactionsIfNotUpdated(fiscalCode, updateTime, OffsetDateTime.now());
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
