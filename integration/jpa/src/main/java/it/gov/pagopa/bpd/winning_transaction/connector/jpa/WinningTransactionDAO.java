package it.gov.pagopa.bpd.winning_transaction.connector.jpa;

import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
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
@Repository
public interface WinningTransactionDAO extends CrudJpaDAO<WinningTransaction, WinningTransactionId> {


    @Query(value = "SELECT wt FROM WinningTransaction wt " +
            "WHERE wt.fiscalCode= :fiscalCode AND wt.awardPeriodId= :awardPeriodId " +
            "AND wt.elabRanking=true AND (:hpan IS NULL OR wt.hpan= :hpan)")
    List<WinningTransaction> findCitizenTransactions(@Param("fiscalCode") String fiscalCode,
                                                     @Param("awardPeriodId") Long awardPeriodId,
                                                     @Param("hpan") String hpan);

    @Modifying
    @Query("update WinningTransaction wt " +
            "set wt.enabled = false," +
            "updateDate = :updateDate, " +
            "updateUser = :fiscalCode " +
            " where wt.fiscalCode = :fiscalCode " +
            "and wt.enabled = true")
    void deactivateCitizenTransactions(@Param("fiscalCode") String fiscalCode,
                                       @Param("updateDate") OffsetDateTime updateDate);

    @Modifying
    @Query("update WinningTransaction " +
            "set enabled = true," +
            "updateDate = :updateDate," +
            "updateUser = 'rollback_recesso' " +
            "where updateDate >= :requestTimestamp " +
            "and fiscal_code_s = :fiscalCode")
    void reactivateForRollback(@Param("fiscalCode") String fiscalCode,
                               @Param("requestTimestamp") OffsetDateTime requestTimestamp,
                               @Param("updateDate") OffsetDateTime updateDate);


}
