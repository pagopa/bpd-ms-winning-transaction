package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionMilestone;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionMilestoneResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

            if("01".equals(model.getOperationType())
                    && model.getAmount()!=null
                    && model.getAmount().compareTo(new BigDecimal(0L))>0){
                resource.setAmount(model.getAmount().negate());
            }

            StringBuilder idTrxBuilder = new StringBuilder();
            idTrxBuilder.append(model.getIdTrxAcquirer())
                        .append(model.getTrxDate().format(formatter))
                        .append(model.getAcquirerCode())
                        .append(model.getAcquirerId())
                        .append(model.getOperationType());

            resource.setIdTrx(idTrxBuilder.toString());
        }

        return resource;
    }
}
