package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.model.resource.FindWinningTransactionResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Mapper between <WinningTransaction> Entity class and <FindWinningTransactionResource> Resource class
 */
@Service
public class FindWinningTransactionResourceAssembler {

    public FindWinningTransactionResource toResource(WinningTransaction model) {
        FindWinningTransactionResource resource = null;

        if (model != null) {
            resource = FindWinningTransactionResource.builder().build();
            BeanUtils.copyProperties(model, resource, "operationType");
            resource.setCashback(model.getCashback());
            resource.setHashPan(model.getHpan());
        }

        return resource;
    }

}
