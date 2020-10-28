package it.gov.pagopa.bpd.winning_transaction.service;

import it.gov.pagopa.bpd.winning_transaction.connector.CitizenRestClient;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.CitizenTransactionDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.WinningTransactionDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionId;
import it.gov.pagopa.bpd.winning_transaction.connector.model.CitizenResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final CitizenTransactionDAO citizenTransactionDAO;
    private final CitizenRestClient citizenRestClient;

    @Autowired
    public WinningTransactionServiceImpl(
            WinningTransactionDAO winningTransactionDAO,
            CitizenTransactionDAO citizenTransactionDAO,
            CitizenRestClient citizenRestClient) {
        this.winningTransactionDAO = winningTransactionDAO;
        this.citizenTransactionDAO = citizenTransactionDAO;
        this.citizenRestClient = citizenRestClient;
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
        //TODO: Risistemare a termine UAT
//        if (winningTransactionDAO.existsById(id)) {
//            throw new WinningTransactionExistsException(id);
//        }
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
    public CitizenResource getCashback(String hpan, Long awardPeriodId, String fiscalCode) {
        if (log.isDebugEnabled()) {
            log.debug("WinningTransactionServiceImpl.getTotalScore");
            log.debug("fiscalCode = [" + fiscalCode + "], awardPeriodId = [" + awardPeriodId + "]");
        }

        return citizenRestClient.getTotalCashback(hpan, fiscalCode, awardPeriodId);
    }


}
