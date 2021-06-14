package it.gov.pagopa.bpd.winning_transaction.listener;

import eu.sia.meda.eventlistener.BaseConsumerAwareEventListener;
import eu.sia.meda.eventlistener.BaseEventListener;
import it.gov.pagopa.bpd.winning_transaction.command.ProcessCitizenUpdateEventCommand;
import it.gov.pagopa.bpd.winning_transaction.command.model.ProcessCitizenUpdateEventCommandModel;
import it.gov.pagopa.bpd.winning_transaction.listener.factory.ModelFactory;
import it.gov.pagopa.bpd.winning_transaction.service.TransactionErrorPublisherService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Class Extending the {@link BaseEventListener}, manages the inbound requests, and calls on the appropriate
 * command for the processing logic, associated to the {@link it.gov.pagopa.bpd.winning_transaction.command.model.Transaction} payload
 */

@Service
@Slf4j
@Conditional(CitizenStatusUpdateRequestListenerEnabledCondition.class)
public class OnCitizenStatusUpdateRequestListener extends BaseConsumerAwareEventListener {

    private final ModelFactory<Pair<byte[], Headers>, ProcessCitizenUpdateEventCommandModel>
            processCitizenUpdateEventCommandModelModelFactory;
    private final BeanFactory beanFactory;

    @Autowired
    public OnCitizenStatusUpdateRequestListener(
            ModelFactory<Pair<byte[], Headers>, ProcessCitizenUpdateEventCommandModel> processCitizenUpdateEventCommandModelModelFactory,
            BeanFactory beanFactory) {
        this.processCitizenUpdateEventCommandModelModelFactory = processCitizenUpdateEventCommandModelModelFactory;
        this.beanFactory = beanFactory;
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

        ProcessCitizenUpdateEventCommandModel processCitizenUpdateEventCommandModel = null;

        try {

            if (logger.isDebugEnabled()) {
                logger.debug("Processing new request on inbound queue");
            }

            processCitizenUpdateEventCommandModel = processCitizenUpdateEventCommandModelModelFactory
                    .createModel(Pair.of(payload, headers));
            ProcessCitizenUpdateEventCommand command = beanFactory.getBean(
                    ProcessCitizenUpdateEventCommand.class, processCitizenUpdateEventCommandModel);

            if (!command.execute()) {
                logger.debug("Failed to execute ProcessCitizenUpdateEventCommand");
            } else {
                logger.debug("ProcessCitizenUpdateEventCommand successfully executed for inbound message");
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

            if (processCitizenUpdateEventCommandModel != null && processCitizenUpdateEventCommandModel.getPayload() != null) {
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

        }

    }

}
