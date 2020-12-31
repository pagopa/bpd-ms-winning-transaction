package it.gov.pagopa.bpd.winning_transaction.connector.jpa;

import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.common.connector.jpa.ReadOnlyRepository;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Data Access Object to manage all CRUD operations to the database
 */
@ReadOnlyRepository
public interface WinningTransactionReplicaDAO extends CrudJpaDAO<WinningTransaction, WinningTransactionId> {

    @Query(nativeQuery = true, value = "SELECT * FROM get_citizen_transactions( :fiscalCode, :awardPeriodId, :hpan)")
    List<WinningTransaction> findCitizenTransactionsByHpan(@Param("fiscalCode") String fiscalCode,
                                                     @Param("awardPeriodId") Long awardPeriodId,
                                                     @Param("hpan") String hpan);

    @Query(nativeQuery = true, value = "SELECT * FROM get_citizen_transactions( :fiscalCode, :awardPeriodId)")
    List<WinningTransaction> findCitizenTransactions(@Param("fiscalCode") String fiscalCode,
                                                     @Param("awardPeriodId") Long awardPeriodId);

}