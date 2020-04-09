package it.gov.pagopa.bpd.winning_transaction.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OperationType {

    PAGAMENTO("00", "pagamento"),
    STORNO_PAGAMENTO("01", "storno pagamento"),
    PAGAMENTO_CON_APPLEPAY("02", "pagamento con applePay"),
    PAGAMENTO_CON_GOOGLEPAY("03", "pagamento con GooglePay"),
    USI_FUTURI("xx", "usi futuri");

    private String code;
    private String description;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OperationType getFromCode(String code) {
        for (OperationType operationType : OperationType.values()) {
            if (operationType.getCode().equals(code)) {
                return operationType;
            }
        }
        return USI_FUTURI;
    }
}
