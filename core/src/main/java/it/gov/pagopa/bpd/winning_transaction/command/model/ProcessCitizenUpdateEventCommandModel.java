package it.gov.pagopa.bpd.winning_transaction.command.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.header.Headers;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessCitizenUpdateEventCommandModel {

    private InboundCitizenStatusData payload;
    private Headers headers;

}
