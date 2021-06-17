package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionMilestone;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionMilestoneResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Service
public class WinningTransactionMilestoneResourceAssembler {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendInstant(3).toFormatter();

    public WinningTransactionMilestoneResource toWinningTransactionMilestoneResource(WinningTransactionMilestone model) {
        WinningTransactionMilestoneResource resource = null;

        if (model != null) {
            resource = WinningTransactionMilestoneResource.builder().build();
            BeanUtils.copyProperties(model, resource);

            StringBuilder idTrxBuilder = new StringBuilder();
            idTrxBuilder.append(model.getIdTrxAcquirer())
                        .append(model.getTrxDate().format(formatter))
                        .append(model.getAcquirerCode())
                        .append(model.getAcquirerId())
                        .append(model.getOperationType());

            resource.setIdTrx(idTrxBuilder.toString());

            if(model.getHpanMaster()!=null
                    && !model.getHashPan().equals(model.getHpanMaster())){
                resource.setHashPan(model.getHpanMaster());
            }
        }

        return resource;
    }
}
