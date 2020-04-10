package it.gov.pagopa.bpd.winning_transaction.model.entity;

import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class WinningTransactionId implements Serializable {

    Integer idTrxAcquirer;
    String acquirerCode;
    ZonedDateTime trxDate;

}
