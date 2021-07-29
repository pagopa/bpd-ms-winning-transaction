package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.resource.enums.OperationType;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionResource;
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
            resource.setOperationType(OperationType.getFromCode(model.getOperationType()));
            resource.setAcquirerCode(model.getAcquirerCode());
            resource.setAwardPeriodId(model.getAwardPeriodId());
            resource.setCorrelationId(model.getCorrelationId());
            resource.setHashPan(model.getHpan());
            resource.setIdTrxAcquirer(model.getIdTrxAcquirer());
            resource.setIdTrxIssuer(model.getIdTrxIssuer());
            resource.setMcc(model.getMcc());
            resource.setScore(model.getScore());
            resource.setTrxDate(model.getTrxDate());
        }

        return resource;
    }

}
