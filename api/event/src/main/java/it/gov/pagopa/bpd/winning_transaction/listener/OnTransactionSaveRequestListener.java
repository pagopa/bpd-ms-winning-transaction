package it.gov.pagopa.bpd.winning_transaction.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.eventlistener.BaseConsumerAwareEventListener;
import eu.sia.meda.eventlistener.BaseEventListener;
import it.gov.pagopa.bpd.winning_transaction.command.SaveTransactionCommand;
import it.gov.pagopa.bpd.winning_transaction.command.model.SaveTransactionCommandModel;
import it.gov.pagopa.bpd.winning_transaction.listener.factory.ModelFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Class Extending the {@link BaseEventListener}, manages the inbound requests, and calls on the appropriate
 * command for the processing logic, associated to the {@link it.gov.pagopa.bpd.winning_transaction.command.model.Transaction} payload
 */

@Service
@Slf4j
public class OnTransactionSaveRequestListener extends BaseConsumerAwareEventListener {

    private final ModelFactory<Pair<byte[], Headers>, SaveTransactionCommandModel>
            saveTransactionCommandModelModelFactory;
    private final BeanFactory beanFactory;
    //    private final PointProcessorErrorPublisherService pointProcessorErrorPublisherService;
    private final ObjectMapper objectMapper;

    @Autowired
    public OnTransactionSaveRequestListener(
            ModelFactory<Pair<byte[], Headers>, SaveTransactionCommandModel> saveTransactionCommandModelModelFactory,
            BeanFactory beanFactory,
//            PointProcessorErrorPublisherService pointProcessorErrorPublisherService,
            ObjectMapper objectMapper) {
        this.saveTransactionCommandModelModelFactory = saveTransactionCommandModelModelFactory;
        this.beanFactory = beanFactory;
//        this.pointProcessorErrorPublisherService = pointProcessorErrorPublisherService;
        this.objectMapper = objectMapper;
    }

    /**
     * Method called on receiving a message in the inbound queue,
     * that should contain a JSON payload containing transaction data,
     * calls on a command to execute the check and send logic for the input {@link it.gov.pagopa.bpd.winning_transaction.command.model.Transaction} data
     * In case of error, sends data to an error channel
     *
     * @param payload Message JSON payload in byte[] format
     * @param headers Kafka headers from the inbound message
     */

    @SneakyThrows
    @Override
    public void onReceived(byte[] payload, Headers headers) {

        SaveTransactionCommandModel saveTransactionCommandModel = null;

        try {

            if (logger.isDebugEnabled()) {
                logger.debug("Processing new request on inbound queue");
            }

            saveTransactionCommandModel = saveTransactionCommandModelModelFactory
                    .createModel(Pair.of(payload, headers));
            SaveTransactionCommand command = beanFactory.getBean(
                    SaveTransactionCommand.class, saveTransactionCommandModel);

            if (!command.execute()) {
                logger.debug("Failed to execute SaveTransactionCommand");
            } else {
                logger.debug("SaveTransactionCommand successfully executed for inbound message");
            }

        } catch (Exception e) {

            String payloadString = "null";
            String error = "Unexpected error during transaction processing";

            try {
                payloadString = new String(payload, StandardCharsets.UTF_8);
            } catch (Exception e2) {
                if (logger.isErrorEnabled()) {
                    logger.error("Something gone wrong converting the payload into String", e2);
                }
            }

            if (saveTransactionCommandModel != null && saveTransactionCommandModel.getPayload() != null) {
                payloadString = new String(payload, StandardCharsets.UTF_8);
                error = String.format("Unexpected error during transaction processing: %s, %s",
                        payloadString, e.getMessage());
            } else if (payload != null) {
                error = String.format("Something gone wrong during the evaluation of the payload: %s, %s",
                        payloadString, e.getMessage());
                if (logger.isErrorEnabled()) {
                    logger.error(error, e);
                }
            }

            //TODO: Gestire adeguatamente casi d'errore
//            if (!pointProcessorErrorPublisherService.publishErrorEvent(payload, headers, error)) {
            if (log.isErrorEnabled()) {
                log.error("Could not publish transaction processing error");
            }
//            throw e;
//            }

        }
    }

}
