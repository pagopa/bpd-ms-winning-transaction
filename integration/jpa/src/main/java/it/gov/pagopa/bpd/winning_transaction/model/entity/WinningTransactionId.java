package it.gov.pagopa.bpd.winning_transaction.model.entity;

import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class WinningTransactionId implements Serializable {

    String idTrxAcquirer;
    String acquirerCode;
    OffsetDateTime trxDate;

}
