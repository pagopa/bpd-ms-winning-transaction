package it.gov.pagopa.bpd.winning_transaction.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WinningTransactionId implements Serializable {

    Integer idTrxAcquirer;
    String acquirerCode;
    ZonedDateTime trxDate;

}
