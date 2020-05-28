package it.gov.pagopa.bpd.winning_transaction.exception;

import it.gov.pagopa.bpd.common.exception.ResourceNotFoundException;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;

public class WinningTransactionNotFoundException extends ResourceNotFoundException {

    public WinningTransactionNotFoundException(String hpan) {
        super(WinningTransaction.class, hpan);
    }

}