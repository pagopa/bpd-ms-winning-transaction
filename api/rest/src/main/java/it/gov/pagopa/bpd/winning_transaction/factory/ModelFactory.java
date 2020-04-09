package it.gov.pagopa.bpd.winning_transaction.factory;

public interface ModelFactory<T, U> {

    U createModel(T dto);

}
