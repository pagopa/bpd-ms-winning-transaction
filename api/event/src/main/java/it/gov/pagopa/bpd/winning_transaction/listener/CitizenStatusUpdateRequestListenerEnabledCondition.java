package it.gov.pagopa.bpd.winning_transaction.listener;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class CitizenStatusUpdateRequestListenerEnabledCondition extends AllNestedConditions {

    public CitizenStatusUpdateRequestListenerEnabledCondition() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(prefix = "listeners.eventConfigurations.items.OnCitizenStatusUpdateRequestListener",
            name = "enable",
            havingValue = "true")
    public static class CitizenUpdateRequestListenerEnabled {}

}
