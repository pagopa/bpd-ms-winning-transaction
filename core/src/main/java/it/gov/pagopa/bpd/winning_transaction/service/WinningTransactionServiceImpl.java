package it.gov.pagopa.bpd.winning_transaction.service;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.CitizenTransactionDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.WinningTransactionDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.CitizenWinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.TotalScoreResourceDTO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionId;
import it.gov.pagopa.bpd.winning_transaction.exception.WinningTransactionExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @See WinningTransactionService
 */
@Service
@Slf4j
public class WinningTransactionServiceImpl implements WinningTransactionService {

    private final WinningTransactionDAO winningTransactionDAO;
    private final CitizenTransactionDAO citizenTransactionDAO;

    @Autowired
    public WinningTransactionServiceImpl(
            WinningTransactionDAO winningTransactionDAO,
            CitizenTransactionDAO citizenTransactionDAO) {
        this.winningTransactionDAO = winningTransactionDAO;
        this.citizenTransactionDAO = citizenTransactionDAO;
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
        if (winningTransactionDAO.existsById(id)) {
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

        if( hpan!=null&& !hpan.isEmpty()){
            winningTransactions = winningTransactionDAO.findByHpanAndAwardPeriodId(hpan, awardPeriodId);
        } else {
            winningTransactions = winningTransactionDAO.findByAwardPeriodId(awardPeriodId);
        }

        return winningTransactions;
    }


    @Override
    public TotalScoreResourceDTO getTotalScore(String hpan, Long awardPeriodId, String fiscalCode) {
        if (log.isDebugEnabled()) {
            log.debug("WinningTransactionServiceImpl.getTotalScore");
            log.debug("fiscalCode = [" + fiscalCode + "], awardPeriodId = [" + awardPeriodId + "]");
        }

        TotalScoreResourceDTO resource = new TotalScoreResourceDTO();

        if (hpan != null) {
            BigDecimal totalScore = new BigDecimal(winningTransactionDAO.calculateTotalScore(hpan, awardPeriodId));
            resource.setTotalScore(totalScore);
            resource.setTransactionNumber(winningTransactionDAO.calculateTotalTransaction(hpan, awardPeriodId));
        } else {
            List<CitizenWinningTransaction> transactions = citizenTransactionDAO.findTransactionByFiscalCode(fiscalCode, awardPeriodId);
            resource.setTotalScore(transactions.stream()
                    .map(CitizenWinningTransaction::getScore).reduce(BigDecimal.ZERO, BigDecimal::add));
            resource.setTransactionNumber((long) transactions.size());
        }
        return resource;
    }

}
