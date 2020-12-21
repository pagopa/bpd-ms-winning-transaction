package it.gov.pagopa.bpd.winning_transaction.connector.jpa.model;

import it.gov.pagopa.bpd.common.connector.jpa.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"idTrxAcquirer", "acquirerCode", "trxDate", "operationType"}, callSuper = false)
@IdClass(WinningTransactionId.class)
@Table(name = "bpd_winning_transaction")
@Where(clause = "ENABLED_B = 'TRUE'")
public class WinningTransaction extends BaseEntity implements Serializable, Persistable<WinningTransactionId> {

    @Transient
    private boolean isNew = true;

    @Transient
    private boolean updatable = false;

    @Id
    @Column(name = "id_trx_acquirer_s")
    String idTrxAcquirer;

    @Id
    @Column(name = "acquirer_c")
    String acquirerCode;

    @Id
    @Column(name = "trx_timestamp_t")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    OffsetDateTime trxDate;

    @Column(name="hpan_s")
    String hpan;

    @Id
    @Column(name = "operation_type_c")
    String operationType;

    @Column(name = "circuit_type_c")
    String circuitType;

    @Column(name="id_trx_issuer_s")
    String idTrxIssuer;

    @Column(name="correlation_id_s")
    String correlationId;

    @Column(name="amount_i")
    BigDecimal amount;

    @Column(name="amount_currency_c")
    String amountCurrency;

    @Column(name="mcc_c")
    String mcc;

    @Column(name="mcc_descr_s")
    String mccDescription;

    @Column(name = "score_n")
    BigDecimal score;

    @Column(name="award_period_id_n")
    Long awardPeriodId;

    @Column(name="acquirer_id_s")
    String acquirerId;

    @Column(name="merchant_id_s")
    String merchantId;

    @Column(name="bin_s")
    String bin;

    @Column(name="terminal_id_s")
    String terminalId;

    @Column(name="fiscal_code_s")
    String fiscalCode;

    @Override
    public WinningTransactionId getId() {
        return WinningTransactionId
                .builder()
                .idTrxAcquirer(idTrxAcquirer)
                .acquirerCode(acquirerCode)
                .trxDate(trxDate)
                .operationType(operationType)
                .build();
    }

    @Override
    public boolean isNew() {
        if (!updatable) {
            return true;
        } else {
            return isNew;
        }
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }
}

