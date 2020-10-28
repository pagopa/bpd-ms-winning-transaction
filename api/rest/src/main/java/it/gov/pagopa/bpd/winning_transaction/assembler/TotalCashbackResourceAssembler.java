package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.connector.model.CitizenResource;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.TotalCashbackResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class TotalCashbackResourceAssembler {

    public TotalCashbackResource toResource(CitizenResource model) {
        TotalCashbackResource resource = null;

        if (model != null) {
            resource = TotalCashbackResource.builder().build();
            BeanUtils.copyProperties(model, resource);
            resource.setTotalCashback(model.getTotalCashback());
            resource.setTransactionNumber(model.getTransactionNumber());
        }

        return resource;
    }
}
