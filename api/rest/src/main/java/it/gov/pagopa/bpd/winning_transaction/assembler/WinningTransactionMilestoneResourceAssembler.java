package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionMilestone;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionMilestoneResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class WinningTransactionMilestoneResourceAssembler {

    public WinningTransactionMilestoneResource toWinningTransactionMilestoneResource(WinningTransactionMilestone model) {
        WinningTransactionMilestoneResource resource = null;

        if (model != null) {
            resource = WinningTransactionMilestoneResource.builder().build();
            BeanUtils.copyProperties(model, resource);

            StringBuilder idTrxBuilder = new StringBuilder();
            idTrxBuilder.append(model.getIdTrxAcquirer())
                        .append(model.getTrxDate().format(DateTimeFormatter.ISO_INSTANT))
                        .append(model.getAcquirerCode())
                        .append(model.getAcquirerId())
                        .append(model.getOperationType());

            resource.setIdTrx(idTrxBuilder.toString());
        }

        return resource;
    }
}
