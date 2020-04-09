package it.gov.pagopa.bpd.winning_transaction;

import eu.sia.meda.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.winning_transaction.model.entity.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.model.entity.WinningTransactionId;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WinningTransactionDAO extends CrudJpaDAO<WinningTransaction, WinningTransactionId> {

    List<WinningTransaction> findByHpanAndAwardPeriodIdAndAwardedTransaction(
            String hpan, Long awardPeriodId, Boolean awardedTransaction);

}
