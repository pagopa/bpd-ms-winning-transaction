package it.gov.pagopa.bpd.winning_transaction.listener.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.bpd.winning_transaction.command.model.InboundCitizenStatusData;
import it.gov.pagopa.bpd.winning_transaction.command.model.ProcessCitizenUpdateEventCommandModel;
import it.gov.pagopa.bpd.winning_transaction.command.model.SaveTransactionCommandModel;
import it.gov.pagopa.bpd.winning_transaction.command.model.Transaction;
import it.gov.pagopa.bpd.winning_transaction.publisher.model.CitizenStatusErrorData;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Implementation of {@link ModelFactory}, that maps a pair containing Kafka related byte[] payload and Headers
 * into a single model for usage inside the micro-service core classes
 */

@Component
public class CitizenUpdateEventErrorPayloadModelFactory implements
        ModelFactory<CitizenStatusErrorData, byte[]> {

    private final ObjectMapper objectMapper;

    @Autowired
    public CitizenUpdateEventErrorPayloadModelFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @SneakyThrows
    @Override
    public byte[] createModel(CitizenStatusErrorData dto) {
        return objectMapper.writeValueAsBytes(dto);
    }

}