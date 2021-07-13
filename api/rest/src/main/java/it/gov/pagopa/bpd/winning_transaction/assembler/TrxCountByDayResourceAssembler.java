package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.TrxCountByDay;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.TrxCountByDayResource;
import org.springframework.stereotype.Service;

/**
 * Mapper between <WinningTransaction> Entity class and <TrxCountByDayResource> Resource class
 */
@Service
public class TrxCountByDayResourceAssembler {
    public TrxCountByDayResource toTrxCountByDayResource(TrxCountByDay model) {
        TrxCountByDayResource resource = null;
        if (model != null) {
            resource = TrxCountByDayResource.builder().build();
            resource.setCount(model.getCount());
            resource.setTrxDate(model.getTrxDate() != null ? model.getTrxDate().toLocalDateTime().toLocalDate() : null);
        }

        return resource;
    }
}
