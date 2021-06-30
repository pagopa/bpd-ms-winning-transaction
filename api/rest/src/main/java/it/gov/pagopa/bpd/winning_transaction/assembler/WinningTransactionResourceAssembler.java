package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.resource.enums.OperationType;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Mapper between <WinningTransaction> Entity class and <WinningTransactionResource> Resource class
 */
@Service
public class WinningTransactionResourceAssembler {

    public WinningTransactionResource toResource(WinningTransaction model) {
        WinningTransactionResource resource = null;

        if (model != null) {
            resource = WinningTransactionResource.builder().build();
            BeanUtils.copyProperties(model, resource, "operationType");
            resource.setOperationType(OperationType.getFromCode(model.getOperationType()));

            if(model.getHpanMaster()!=null
                    && !model.getHpan().equals(model.getHpanMaster())){
                resource.setHashPan(model.getHpanMaster());
            }
        }

        return resource;
    }

}
