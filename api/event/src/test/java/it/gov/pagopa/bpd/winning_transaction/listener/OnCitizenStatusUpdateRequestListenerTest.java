package it.gov.pagopa.bpd.winning_transaction.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.event.service.ErrorPublisherService;
import eu.sia.meda.eventlistener.BaseEventListenerTest;
import it.gov.pagopa.bpd.winning_transaction.command.ProcessCitizenUpdateEventCommand;
import it.gov.pagopa.bpd.winning_transaction.command.model.InboundCitizenStatusData;
import it.gov.pagopa.bpd.winning_transaction.command.model.Transaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.CitizenStatusData;
import it.gov.pagopa.bpd.winning_transaction.listener.factory.CitizenUpdateEventCommandModelFactory;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.OffsetDateTime;

@Import({OnCitizenStatusUpdateRequestListener.class})
@TestPropertySource(
        locations = "classpath:config/citizenEventRequestListener.properties",
        properties = {
                "listeners.eventConfigurations.items.OnCitizenStatusUpdateRequestListener.bootstrapServers=${spring.embedded.kafka.brokers}"
        })
public class OnCitizenStatusUpdateRequestListenerTest extends BaseEventListenerTest {


    @Value("${listeners.eventConfigurations.items.OnCitizenStatusUpdateRequestListener.topic}")
    private String topic;

    @SpyBean
    ObjectMapper objectMapperSpy;

    @SpyBean
    OnCitizenStatusUpdateRequestListener onCitizenStatusUpdateRequestListenerSpy;

    @SpyBean
    CitizenUpdateEventCommandModelFactory citizenUpdateEventCommandModelFactorySpy;

    @MockBean
    BeanFactory beanFactoryMock;

    @MockBean
    ProcessCitizenUpdateEventCommand processCitizenUpdateEventCommand;

    @Before
    public void setUp() throws Exception {

        Mockito.reset(
                onCitizenStatusUpdateRequestListenerSpy,
                citizenUpdateEventCommandModelFactorySpy,
                beanFactoryMock,
                processCitizenUpdateEventCommand);
        Mockito.doReturn(true).when(processCitizenUpdateEventCommand).execute();

    }

    @Override
    protected Object getRequestObject() {
          CitizenStatusData citizenStatusData = CitizenStatusData.builder()
                    .fiscalCode("fiscalCode")
                    .updateDateTime(OffsetDateTime.parse("2020-04-09T16:22:45.304Z"))
                    .build();
          citizenStatusData.setEnabled(false);
          return citizenStatusData;
    }

    @Override
    protected String getTopic() {
        return topic;
    }

    @Override
    protected void verifyInvocation(String json) {
        try {
            BDDMockito.verify(citizenUpdateEventCommandModelFactorySpy, Mockito.atLeastOnce())
                    .createModel(Mockito.any());
            BDDMockito.verify(objectMapperSpy, Mockito.atLeastOnce())
                    .readValue(Mockito.anyString(), Mockito.eq(InboundCitizenStatusData.class));
            BDDMockito.verify(processCitizenUpdateEventCommand, Mockito.atLeastOnce()).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Override
    protected ErrorPublisherService getErrorPublisherService() {
        return null;
    }

}