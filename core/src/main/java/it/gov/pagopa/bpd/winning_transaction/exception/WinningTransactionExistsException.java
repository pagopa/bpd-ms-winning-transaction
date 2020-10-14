package it.gov.pagopa.bpd.winning_transaction.exception;

import it.gov.pagopa.bpd.common.exception.ResourceExistsException;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionId;

public class WinningTransactionExistsException extends ResourceExistsException {

    public WinningTransactionExistsException(WinningTransactionId id) {
        super(WinningTransaction.class, id);
    }

}