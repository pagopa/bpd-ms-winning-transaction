package it.gov.pagopa.bpd.winning_transaction.command.model;

import it.gov.pagopa.bpd.winning_transaction.command.model.enums.OperationType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;


/**
 * Resource model for the data published through {@link it.gov.pagopa.bpd.winning_transaction.command.SaveTransactionCommand}
 */


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"fiscalCode"}, callSuper = false)
public class InboundCitizenStatusData {

    @NotNull
    @NotBlank
    String fiscalCode;

    @NotNull
    Boolean enabled;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    OffsetDateTime updateDateTime;


}
