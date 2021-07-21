package it.gov.pagopa.bpd.winning_transaction.service;

import eu.sia.meda.event.BaseEventConnector;
import eu.sia.meda.event.service.BaseErrorPublisherService;
import eu.sia.meda.event.transformer.ErrorEventRequestTransformer;
import eu.sia.meda.event.transformer.SimpleEventResponseTransformer;
import it.gov.pagopa.bpd.winning_transaction.publisher.CitizenStatusErrorPublisherConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CitizenStatusErrorPublisherServiceImpl
        extends BaseErrorPublisherService implements CitizenStatusErrorPublisherService {

    private final CitizenStatusErrorPublisherConnector citizenStatusErrorPublisherConnector;

    @Autowired
    public CitizenStatusErrorPublisherServiceImpl(
            CitizenStatusErrorPublisherConnector citizenStatusErrorPublisherConnector,
            ErrorEventRequestTransformer errorEventRequestTransformer,
            SimpleEventResponseTransformer simpleEventResponseTransformer) {
        super(errorEventRequestTransformer, simpleEventResponseTransformer);
        this.citizenStatusErrorPublisherConnector = citizenStatusErrorPublisherConnector;
    }

    @Override
    protected BaseEventConnector<byte[], Boolean, byte[], Void> getErrorPublisherConnector() {
        return citizenStatusErrorPublisherConnector;
    }

}
