package it.gov.pagopa.bpd.winning_transaction.connector.jpa;

import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.CitizenStatusData;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenStatusDataDAO extends CrudJpaDAO<CitizenStatusData, String> {}
