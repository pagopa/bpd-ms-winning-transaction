package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionMilestone;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionMilestoneResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class WinningTransactionMilestoneResourceAssembler {

    public WinningTransactionMilestoneResource toResource(WinningTransactionMilestone model) {
        WinningTransactionMilestoneResource resource = null;

        if (model != null) {
            resource = WinningTransactionMilestoneResource.builder().build();
            BeanUtils.copyProperties(model, resource, "trxDate");
            resource.setTrxDate(OffsetDateTime.parse(model.getTrxDate().toInstant().toString(),
                    DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }

        return resource;
    }
}
