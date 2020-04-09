package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.model.entity.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.model.enums.OperationType;
import it.gov.pagopa.bpd.winning_transaction.model.resource.WinningTransactionResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class WinningTransactionResourceAssembler {

    public WinningTransactionResource toResource(WinningTransaction model) {
        WinningTransactionResource resource = null;

        if (model != null) {
            resource = new WinningTransactionResource();
            BeanUtils.copyProperties(model, resource, "operationType");
            resource.setOperationType(OperationType.getFromCode(model.getOperationType()));
        }

        return resource;
    }

}
