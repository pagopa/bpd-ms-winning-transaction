package it.gov.pagopa.bpd.winning_transaction.connector.jpa;

import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(nativeQuery = true, value = "SELECT * FROM get_citizen_transactions( :fiscalCode, :awardPeriodId, :hpan)")
    List<WinningTransaction> findCitizenTransactionsByHpan(@Param("fiscalCode") String fiscalCode,
                                                     @Param("awardPeriodId") Long awardPeriodId,
                                                     @Param("hpan") String hpan);

    @Query(nativeQuery = true, value = "SELECT * FROM get_citizen_transactions( :fiscalCode, :awardPeriodId)")
    List<WinningTransaction> findCitizenTransactions(@Param("fiscalCode") String fiscalCode,
                                                     @Param("awardPeriodId") Long awardPeriodId);

    @Query(nativeQuery = true, value = "SELECT * FROM get_citizen_transactions( :fiscalCode, :awardPeriodId, :hpan)")
    Page<WinningTransaction> findCitizenTransactionsByHpanPaged(@Param("fiscalCode") String fiscalCode,
                                                                @Param("awardPeriodId") Long awardPeriodId,
                                                                @Param("hpan") String hpan,
                                                                Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM get_citizen_transactions( :fiscalCode, :awardPeriodId)")
    Page<WinningTransaction> findCitizenTransactionsPaged(@Param("fiscalCode") String fiscalCode,
                                                     @Param("awardPeriodId") Long awardPeriodId,
                                                     Pageable pageable);

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
