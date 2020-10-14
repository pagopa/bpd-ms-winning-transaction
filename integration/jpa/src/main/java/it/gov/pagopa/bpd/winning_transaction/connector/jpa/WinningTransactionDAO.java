package it.gov.pagopa.bpd.winning_transaction.connector.jpa;

import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data Access Object to manage all CRUD operations to the database
 */
@Repository
public interface WinningTransactionDAO extends CrudJpaDAO<WinningTransaction, WinningTransactionId> {

    List<WinningTransaction> findByHpanAndAwardPeriodId(String hpan, Long awardPeriodId);

    @Query("SELECT SUM(score) FROM WinningTransaction WHERE hpan = ?1 AND awardPeriodId = ?2")
    Long calculateTotalScore(
            String hpan, Long awardPeriodId);

}
