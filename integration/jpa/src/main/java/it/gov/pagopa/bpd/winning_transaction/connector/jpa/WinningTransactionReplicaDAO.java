package it.gov.pagopa.bpd.winning_transaction.connector.jpa;

import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.common.connector.jpa.ReadOnlyRepository;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionByDateCount;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionId;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionMilestone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query(nativeQuery = true, value = "SELECT * FROM get_citizen_transactions( :fiscalCode, :awardPeriodId, :hpan)")
    Page<WinningTransaction> findCitizenTransactionsByHpanPage(@Param("fiscalCode") String fiscalCode,
                                                           @Param("awardPeriodId") Long awardPeriodId,
                                                           @Param("hpan") String hpan,
                                                               Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM get_citizen_transactions( :fiscalCode, :awardPeriodId)")
    List<WinningTransaction> findCitizenTransactions(@Param("fiscalCode") String fiscalCode,
                                                     @Param("awardPeriodId") Long awardPeriodId);

    @Query(nativeQuery = true, value = "SELECT * FROM get_citizen_transactions( :fiscalCode, :awardPeriodId)")
    Page<WinningTransaction> findCitizenTransactionsPage(@Param("fiscalCode") String fiscalCode,
                                                     @Param("awardPeriodId") Long awardPeriodId,
                                                     Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT id_trx as idTrx, " +
                                            "cashback_norm as cashbackNorm, " +
                                            "is_pivot as isPivot, " +
                                            "hash_pan as hashPan, " +
                                            "id_trx_acquirer as idTrxAcquirer, " +
                                            "trx_date as trxDate, " +
                                            "circuit_type as circuitType, " +
                                            "id_trx_issuer as idTrxIssuer, " +
                                            "amount as amount, " +
                                            "cashback as cashback, " +
                                            "award_period_id as awardPeriodId " +
                                        "FROM get_citizen_transaction_with_milestones( :fiscalCode, :awardPeriodId, :hpan)")
    Page<WinningTransactionMilestone> findCitizenTransactionsMilestoneByHpanPage(@Param("fiscalCode") String fiscalCode,
                                                                                 @Param("awardPeriodId") Long awardPeriodId,
                                                                                 @Param("hpan") String hpan,
                                                                                 Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT id_trx as idTrx, " +
            "cashback_norm as cashbackNorm, " +
            "is_pivot as isPivot, " +
            "hash_pan as hashPan, " +
            "id_trx_acquirer as idTrxAcquirer, " +
            "trx_date as trxDate, " +
            "circuit_type as circuitType, " +
            "id_trx_issuer as idTrxIssuer, " +
            "amount as amount, " +
            "cashback as cashback, " +
            "award_period_id as awardPeriodId " +
            "FROM get_citizen_transaction_with_milestones( :fiscalCode, :awardPeriodId)")
    Page<WinningTransactionMilestone> findCitizenTransactionsMilestonePage(@Param("fiscalCode") String fiscalCode,
                                                                                 @Param("awardPeriodId") Long awardPeriodId,
                                                                                 Pageable pageable);

    @Query(value = "SELECT " +
                        "date_trunc('day', trx_timestamp_t) as trxDate, " +
                        "count(*) as count " +
                    "FROM bpd_winning_transaction  " +
                    "WHERE fiscal_code_s = :fiscalCode " +
                        "AND award_period_id_n = :awardPeriodId " +
                        "AND enabled_b " +
                        "AND (:hpan is null or :hpan=hpan_s ) " +
                        "AND elab_ranking_b = true " +
                    "GROUP BY date_trunc('day', trx_timestamp_t)", nativeQuery = true)
    List<WinningTransactionByDateCount> findCitizenTransactionsByDateCountHpan(@Param("fiscalCode") String fiscalCode,
                                                                               @Param("awardPeriodId") Long awardPeriodId,
                                                                               @Param("hpan") String hpan);

    @Query(value = "SELECT " +
            "date_trunc('day', trx_timestamp_t) as trxDate, " +
            "count(*) as count " +
            "FROM bpd_winning_transaction  " +
            "WHERE fiscal_code_s = :fiscalCode " +
            "AND award_period_id_n = :awardPeriodId " +
            "AND enabled_b " +
            "AND elab_ranking_b = true " +
            "GROUP BY date_trunc('day', trx_timestamp_t)", nativeQuery = true)
    List<WinningTransactionByDateCount> findCitizenTransactionsByDateCount(@Param("fiscalCode") String fiscalCode,
                                                                               @Param("awardPeriodId") Long awardPeriodId);

}
