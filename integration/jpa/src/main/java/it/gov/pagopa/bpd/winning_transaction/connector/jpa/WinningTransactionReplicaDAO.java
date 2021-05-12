package it.gov.pagopa.bpd.winning_transaction.connector.jpa;

import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.common.connector.jpa.ReadOnlyRepository;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.TrxCountByDay;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
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

    @Query(value = "SELECT w " +
            " FROM WinningTransaction w " +
            " WHERE fiscalCode = :fiscalCode " +
            " AND awardPeriodId = :awardPeriodId " +
            " AND elabRanking is TRUE " +
            " AND hpan = :hpan " +
            " AND (valid is TRUE " +
            " OR valid is null) ")
    List<WinningTransaction> findCitizenTransactionsByHpan(@Param("fiscalCode") String fiscalCode,
                                                     @Param("awardPeriodId") Long awardPeriodId,
                                                     @Param("hpan") String hpan);

    @Query(value = "SELECT w " +
            " FROM WinningTransaction w " +
            " WHERE fiscalCode = :fiscalCode " +
            " AND awardPeriodId = :awardPeriodId " +
            " AND elabRanking is TRUE " +
            " AND (valid is TRUE " +
            " OR valid is null) ")
    List<WinningTransaction> findCitizenTransactions(@Param("fiscalCode") String fiscalCode,
                                                     @Param("awardPeriodId") Long awardPeriodId);

    @Query(value = "SELECT idTrxAcquirer as idTrxAcquirer, "+
                        "trxDate as trxDate, " +
                        "acquirerCode as acquirerCode, " +
                        "acquirerId as acquirerId, " +
                        "operationType as operationType, " +
            "hpan as hashPan, " +
            "circuitType as circuitType, " +
            "amount as amount, " +
            "score as cashback, " +
            "awardPeriodId as awardPeriodId, " +
            "idTrxIssuer as idTrxIssuer " +
            "FROM WinningTransaction w " +
            "WHERE fiscalCode = :fiscalCode " +
            "AND awardPeriodId = :awardPeriodId " +
            "AND hpan = :hpan " +
            "AND elabRanking = true " +
            " AND (valid is TRUE " +
            " OR valid is null) ")
    Page<WinningTransactionMilestone> findCitizenTransactionsMilestoneByHpanPage(@Param("fiscalCode") String fiscalCode,
                                                                                 @Param("awardPeriodId") Long awardPeriodId,
                                                                                 @Param("hpan") String hpan,
                                                                                 Pageable pageable);

    @Query(value = "SELECT idTrxAcquirer as idTrxAcquirer, " +
            "trxDate as trxDate, " +
            "acquirerCode as acquirerCode, " +
            "acquirerId as acquirerId, " +
            "operationType as operationType, " +
            "hpan as hashPan, " +
            "circuitType as circuitType, " +
            "amount as amount, " +
            "score as cashback, " +
            "awardPeriodId as awardPeriodId, " +
            "idTrxIssuer as idTrxIssuerx " +
            "FROM WinningTransaction w " +
            "WHERE fiscalCode = :fiscalCode " +
            "AND awardPeriodId = :awardPeriodId " +
            "AND elabRanking = true " +
            " AND (valid is TRUE " +
            " OR valid is null) ")
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
            "AND elab_ranking_b = true " +
            "AND hpan_s = :hpan " +
            " AND (valid_b is TRUE " +
            " OR valid_b is null) " +
            "GROUP BY date_trunc('day', trx_timestamp_t) ", nativeQuery = true)
    List<TrxCountByDay> findCitizenTransactionsByDateCountHpan(@Param("fiscalCode") String fiscalCode,
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
            " AND (valid_b is TRUE " +
            " OR valid_b is null) " +
            "GROUP BY date_trunc('day', trx_timestamp_t)", nativeQuery = true)
    List<TrxCountByDay> findCitizenTransactionsByDateCount(@Param("fiscalCode") String fiscalCode,
                                                           @Param("awardPeriodId") Long awardPeriodId);

}
