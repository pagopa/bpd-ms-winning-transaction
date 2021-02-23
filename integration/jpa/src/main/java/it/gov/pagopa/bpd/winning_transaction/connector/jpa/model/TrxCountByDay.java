package it.gov.pagopa.bpd.winning_transaction.connector.jpa.model;

import java.sql.Timestamp;

public interface TrxCountByDay {
    Timestamp getTrxDate();
    Long getCount();
}
