package it.gov.pagopa.bpd.winning_transaction.publisher.model;

import lombok.*;

import java.time.OffsetDateTime;


/**
 * Resource model for the data published through {@link it.gov.pagopa.bpd.winning_transaction.publisher.CitizenStatusErrorPublisherConnector}
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"fiscalCode"}, callSuper = false)
public class CitizenStatusErrorData {

    String fiscalCode;

    Boolean enabled;

    OffsetDateTime updateDateTime;

    String origin;

    String exceptionMessage;

}
