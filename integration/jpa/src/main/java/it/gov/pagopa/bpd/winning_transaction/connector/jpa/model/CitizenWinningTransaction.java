package it.gov.pagopa.bpd.winning_transaction.connector.jpa.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Data
public class CitizenWinningTransaction implements Serializable {

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

    @Column(name = "hpan_s")
    String hpan;

    @Column(name = "operation_type_c")
    String operationType;

    @Column(name = "circuit_type_c")
    String circuitType;

    @Column(name = "id_trx_issuer_s")
    String idTrxIssuer;

    @Column(name = "correlation_id_s")
    String correlationId;

    @Column(name = "amount_i")
    BigDecimal amount;

    @Column(name = "amount_currency_c")
    String amountCurrency;

    @Column(name = "mcc_c")
    String mcc;

    @Column(name = "mcc_descr_s")
    String mccDescription;

    @Column(name = "score_n")
    BigDecimal score;

    @Column(name = "award_period_id_n")
    Long awardPeriodId;

    @Column(name = "acquirer_id_s")
    String acquirerId;

    @Column(name = "merchant_id_s")
    String merchantId;

    @Column(name = "bin_s")
    String bin;

    @Column(name = "terminal_id_s")
    String terminalId;

}

