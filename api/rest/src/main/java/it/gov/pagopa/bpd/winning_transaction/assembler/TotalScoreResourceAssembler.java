package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.TotalScoreResourceDTO;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.TotalScoreResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class TotalScoreResourceAssembler {

    public TotalScoreResource toResource(TotalScoreResourceDTO model) {
        TotalScoreResource resource = null;

        if (model != null) {
            resource = TotalScoreResource.builder().build();
            BeanUtils.copyProperties(model, resource);
            resource.setTotalScore(model.getTotalScore());
            resource.setTransactionNumber(model.getTransactionNumber());
        }

        return resource;
    }
}
