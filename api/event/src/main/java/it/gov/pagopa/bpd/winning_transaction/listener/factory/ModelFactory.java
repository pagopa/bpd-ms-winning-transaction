package it.gov.pagopa.bpd.winning_transaction.listener.factory;

/**
 * interface to be used for inheritance for model factories from a DTO
 *
 * @see SaveTransactionCommandModelFactory
 */

public interface ModelFactory<T, U> {

    U createModel(T dto);

}
