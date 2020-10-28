package it.gov.pagopa.bpd.winning_transaction.connector.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Resource model (output) for {@link it.gov.pagopa.bpd.winning_transaction.connector.CitizenRestClient}
 */
@Data
public class CitizenResource {

    private Long transactionNumber;
    private BigDecimal totalCashback;

}
