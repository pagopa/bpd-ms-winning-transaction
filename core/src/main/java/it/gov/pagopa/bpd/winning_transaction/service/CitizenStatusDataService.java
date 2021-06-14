package it.gov.pagopa.bpd.winning_transaction.service;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.CitizenStatusData;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface CitizenStatusDataService {

    boolean checkAndCreate(CitizenStatusData citizenStatusData);

    Optional<CitizenStatusData> findCitizenStatusData(String fiscalCode);

}
