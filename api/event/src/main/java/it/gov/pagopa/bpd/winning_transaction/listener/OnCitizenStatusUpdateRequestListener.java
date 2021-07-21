package it.gov.pagopa.bpd.winning_transaction.listener;

import eu.sia.meda.eventlistener.BaseConsumerAwareEventListener;
import eu.sia.meda.eventlistener.BaseEventListener;
import it.gov.pagopa.bpd.winning_transaction.command.ProcessCitizenUpdateEventCommand;
import it.gov.pagopa.bpd.winning_transaction.command.model.InboundCitizenStatusData;
import it.gov.pagopa.bpd.winning_transaction.command.model.ProcessCitizenUpdateEventCommandModel;
import it.gov.pagopa.bpd.winning_transaction.listener.factory.ModelFactory;
import it.gov.pagopa.bpd.winning_transaction.publisher.model.CitizenStatusErrorData;
import it.gov.pagopa.bpd.winning_transaction.service.CitizenStatusErrorPublisherService;
import it.gov.pagopa.bpd.winning_transaction.service.TransactionErrorPublisherService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.common.header.Header;
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
public class OnCitizenStatusUpdateRequestListener extends BaseConsumerAwareEventListener {

    private final ModelFactory<Pair<byte[], Headers>, ProcessCitizenUpdateEventCommandModel>
            processCitizenUpdateEventCommandModelModelFactory;
    private final ModelFactory<CitizenStatusErrorData, byte[]> citizenStatusErrorDataModelFactory;
    private final BeanFactory beanFactory;
    private final CitizenStatusErrorPublisherService citizenStatusErrorPublisherService;

    @Autowired
    public OnCitizenStatusUpdateRequestListener(
            ModelFactory<Pair<byte[], Headers>, ProcessCitizenUpdateEventCommandModel> processCitizenUpdateEventCommandModelModelFactory,
            ModelFactory<CitizenStatusErrorData, byte[]> citizenStatusErrorDataModelFactory,
            CitizenStatusErrorPublisherService citizenStatusErrorPublisherService,
            BeanFactory beanFactory) {
        this.processCitizenUpdateEventCommandModelModelFactory = processCitizenUpdateEventCommandModelModelFactory;
        this.citizenStatusErrorPublisherService = citizenStatusErrorPublisherService;
        this.beanFactory = beanFactory;
        this.citizenStatusErrorDataModelFactory = citizenStatusErrorDataModelFactory;
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

            logger.debug("Processing new request on inbound queue");

            processCitizenUpdateEventCommandModel = processCitizenUpdateEventCommandModelModelFactory
                    .createModel(Pair.of(payload, headers));
            ProcessCitizenUpdateEventCommand command = beanFactory.getBean(
                    ProcessCitizenUpdateEventCommand.class, processCitizenUpdateEventCommandModel);

            if (!processCitizenUpdateEventCommandModel.getPayload().getApplyTo().equals("all") &&
                !processCitizenUpdateEventCommandModel.getPayload().getApplyTo().equals("winning_transaction")) {
                logger.debug("Processed request refers to an update event not to be applied in bpd_winning_transaction");
                return;
            }

            if (!command.execute()) {
                logger.error("Failed to execute ProcessCitizenUpdateEventCommand");
            } else {
                logger.debug("ProcessCitizenUpdateEventCommand successfully executed for inbound message");
            }

        } catch (Exception e) {

            String payloadString = "null";
            String error = "Unexpected error during transaction processing";

            try {
                payloadString = new String(payload, StandardCharsets.UTF_8);
            } catch (Exception e2) {
                logger.error("Something gone wrong converting the payload into String", e2);
            }

            if (processCitizenUpdateEventCommandModel != null &&
                    processCitizenUpdateEventCommandModel.getPayload() != null) {
                payloadString = new String(payload, StandardCharsets.UTF_8);
                error = String.format("Unexpected error during citizen status update processing: %s, %s",
                        payloadString, e.getMessage());


                InboundCitizenStatusData inboundCitizenStatusData = processCitizenUpdateEventCommandModel.getPayload();


                if (!citizenStatusErrorPublisherService.publishErrorEvent(
                        citizenStatusErrorDataModelFactory.createModel(
                                CitizenStatusErrorData
                                        .builder()
                                        .fiscalCode(inboundCitizenStatusData.getFiscalCode())
                                        .updateDateTime(inboundCitizenStatusData.getUpdateDateTime())
                                        .enabled(inboundCitizenStatusData.getEnabled())
                                        .exceptionMessage(error)
                                        .origin("bpd_winning_transaction")
                                        .build()), headers, error)) {
                    log.error("Could not publish transaction processing error");
                    throw e;
                }

            } else if (payload != null) {
                error = String.format("Something gone wrong during the evaluation of the payload: %s, %s",
                        payloadString, e.getMessage());
                logger.error(error, e);
            }

        }

    }

}
