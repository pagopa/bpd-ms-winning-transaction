package it.gov.pagopa.bpd.winning_transaction.publisher;

import eu.sia.meda.event.BaseEventConnector;
import org.springframework.stereotype.Service;

/**
 * Class extending the MEDA BaseEventConnector, is responsible for calling a Kafka outbound channel with messages
 * containing content in byte[] format class
 */

@Service
public class TransactionErrorPublisherConnector
        extends BaseEventConnector<byte[], Boolean, byte[], Void> {

}
