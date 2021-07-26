package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.FindWinningTransactionResource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Mapper between <WinningTransaction> Entity class and <FindWinningTransactionResource> Resource class
 */
@Service
public class FindWinningTransactionResourceAssembler {

    public FindWinningTransactionResource toResource(WinningTransaction model) {
        FindWinningTransactionResource resource = null;

        if (model != null) {
            resource = FindWinningTransactionResource.builder().build();
            resource.setAwardPeriodId(model.getAwardPeriodId());
            resource.setCircuitType(model.getCircuitType());
            resource.setIdTrxAcquirer(model.getIdTrxAcquirer());
            resource.setIdTrxIssuer(model.getIdTrxIssuer());
            resource.setTrxDate(model.getTrxDate());
            resource.setCashback(model.getScore());
            resource.setHashPan(model.getHpan());
            resource.setAmount(model.getAmount());

            if ("01".equals(model.getOperationType())
                    && model.getAmount() != null
                    && model.getAmount().compareTo(new BigDecimal(0L)) > 0) {
                resource.setAmount(model.getAmount().negate());
            }
        }

        return resource;
    }

}
