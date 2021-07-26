package it.gov.pagopa.bpd.winning_transaction.mapper;

import it.gov.pagopa.bpd.winning_transaction.command.model.InboundCitizenStatusData;
import it.gov.pagopa.bpd.winning_transaction.command.model.Transaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.CitizenStatusData;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import org.springframework.stereotype.Service;

/**
 * Class to be used to map a {@link Transaction} from an* {@link WinningTransaction}
 */

@Service
public class CitizenStatusDataMapper {

    /**
     * @param inboundCitizenStatusData instance of an {@link InboundCitizenStatusData}, to be mapped into a {@link CitizenStatusData}
     * @return {@link CitizenStatusData} instance from the input transaction,
     */
    public CitizenStatusData map(
            InboundCitizenStatusData inboundCitizenStatusData) {

        CitizenStatusData citizenStatusData = null;

        if (inboundCitizenStatusData != null) {
            citizenStatusData = CitizenStatusData.builder().build();
            citizenStatusData.setEnabled(inboundCitizenStatusData.getEnabled());
            citizenStatusData.setFiscalCode(inboundCitizenStatusData.getFiscalCode());
            citizenStatusData.setUpdateDateTime(inboundCitizenStatusData.getUpdateDateTime());
        }

        return citizenStatusData;

    }

}
