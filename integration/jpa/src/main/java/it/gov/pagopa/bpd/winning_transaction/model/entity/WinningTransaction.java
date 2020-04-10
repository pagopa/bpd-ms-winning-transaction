package it.gov.pagopa.bpd.winning_transaction.model.entity;

import it.gov.pagopa.bpd.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"idTrxAcquirer", "acquirerCode", "trxDate"}, callSuper = false)
@IdClass(WinningTransactionId.class)
@Table(name = "bpd_winning_transaction")
public class WinningTransaction extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id_trx_acquirer_n")
    Integer idTrxAcquirer;

    @Id
    @Column(name = "acquirer_c")
    String acquirerCode;

    @Id
    @Column(name = "trx_timestamp_t")
    ZonedDateTime trxDate;

    @Column(name="hpan_s")
    String hpan;

    @Column(name = "operation_type_c")
    String operationType;

    @Column(name = "circuit_type_c")
    String circuitType;

    @Column(name="id_trx_issuer_n")
    Integer idTrxIssuer;

    @Column(name="correlation_id_n")
    Integer correlationId;

    @Column(name="amount_i")
    BigDecimal amount;

    @Column(name="amount_currency_c")
    String amountCurrency;

    @Column(name="mcc_c")
    String mcc;

    @Column(name="mcc_descr_s")
    String mccDescription;

    @Column(name="score_n")
    BigDecimal score;

    @Column(name="awarded_transaction_b")
    Boolean awardedTransaction;

    @Column(name="award_period_id_n")
    Long awardPeriodId;

    @Column(name="acquirer_id_n")
    Integer acquirerId;

    @Column(name="merchant_id_n")
    Integer merchantId;

}
