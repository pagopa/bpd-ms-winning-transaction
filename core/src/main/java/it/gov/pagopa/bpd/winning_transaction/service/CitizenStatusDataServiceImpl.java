package it.gov.pagopa.bpd.winning_transaction.service;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.CitizenStatusDataDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.WinningTransactionDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.WinningTransactionReplicaDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.CitizenStatusData;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.TrxCountByDay;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionMilestone;
import it.gov.pagopa.bpd.winning_transaction.exception.WinningTransactionExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @See CitizenStatusDataService
 */
@Service
@Slf4j
public class CitizenStatusDataServiceImpl implements CitizenStatusDataService {

    private final CitizenStatusDataDAO citizenStatusDataDAO;

    @Autowired
    public CitizenStatusDataServiceImpl(
            ObjectProvider<CitizenStatusDataDAO> citizenStatusDataDAO) {
        this.citizenStatusDataDAO = citizenStatusDataDAO.getIfAvailable();
    }

    @Override
    public boolean checkAndCreate(CitizenStatusData citizenStatusData) {

        Optional<CitizenStatusData> savedCitizenStatusDataOpt =
                citizenStatusDataDAO.findById(citizenStatusData.getFiscalCode());

        if (savedCitizenStatusDataOpt.isPresent() &&
            savedCitizenStatusDataOpt.get().getUpdateDateTime().compareTo(
                    citizenStatusData.getUpdateDateTime()) >= 0) {
            log.warn("Encountered an update event that is outdated");
            return false;
        }

        citizenStatusDataDAO.update(citizenStatusData);

        return true;
    }

    @Override
    public Optional<CitizenStatusData> findCitizenStatusData(String fiscalCode) {
        return citizenStatusDataDAO.findById(fiscalCode);
    }
}
