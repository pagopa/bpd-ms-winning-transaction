package it.gov.pagopa.bpd.winning_transaction.command.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.header.Headers;

/**
 * Model containing the inbound message data
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveTransactionCommandModel {

    private Transaction payload;
    private Headers headers;

}
