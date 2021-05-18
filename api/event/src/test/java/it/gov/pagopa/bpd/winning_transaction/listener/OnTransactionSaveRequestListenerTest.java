package it.gov.pagopa.bpd.winning_transaction.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.event.service.ErrorPublisherService;
import eu.sia.meda.eventlistener.BaseEventListenerTest;
import it.gov.pagopa.bpd.winning_transaction.command.SaveTransactionCommand;
import it.gov.pagopa.bpd.winning_transaction.command.model.Transaction;
import it.gov.pagopa.bpd.winning_transaction.command.model.enums.OperationType;
import it.gov.pagopa.bpd.winning_transaction.listener.factory.CitizenUpdateEventCommandModelFactory;
import it.gov.pagopa.bpd.winning_transaction.listener.factory.SaveTransactionCommandModelFactory;
import it.gov.pagopa.bpd.winning_transaction.service.TransactionErrorPublisherService;
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

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Import({OnTransactionSaveRequestListener.class})
@TestPropertySource(
        locations = "classpath:config/transactionRequestListener.properties",
        properties = {
                "listener.OnTransactionSaveRequestListener.enableCitizenValidation=false",
                "listeners.eventConfigurations.items.OnTransactionSaveRequestListener.bootstrapServers=${spring.embedded.kafka.brokers}"
        })
public class OnTransactionSaveRequestListenerTest extends BaseEventListenerTest {


    @Value("${listeners.eventConfigurations.items.OnTransactionSaveRequestListener.topic}")
    private String topic;

    @SpyBean
    ObjectMapper objectMapperSpy;

    @SpyBean
    OnTransactionSaveRequestListener onTransactionProcessRequestListenerSpy;

    @SpyBean
    SaveTransactionCommandModelFactory saveTransactionCommandModelFactorySpy;

    @SpyBean
    CitizenUpdateEventCommandModelFactory citizenUpdateEventCommandModelFactorySpy;

    @MockBean
    BeanFactory beanFactoryMock;

    @MockBean
    SaveTransactionCommand saveTransactionCommandMock;

    @MockBean
    TransactionErrorPublisherService transactionErrorPublisherServiceMock;


    @Before
    public void setUp() throws Exception {

        Mockito.reset(
                onTransactionProcessRequestListenerSpy,
                saveTransactionCommandModelFactorySpy,
                citizenUpdateEventCommandModelFactorySpy,
                beanFactoryMock,
                saveTransactionCommandMock,
                transactionErrorPublisherServiceMock);
        Mockito.doReturn(true).when(saveTransactionCommandMock).execute();

    }

    @Override
    protected Object getRequestObject() {
        return Transaction.builder()
                .idTrxAcquirer("1")
                .acquirerCode("001")
                .trxDate(OffsetDateTime.parse("2020-04-09T16:22:45.304Z"))
                .amount(BigDecimal.valueOf(1313.13))
                .operationType(OperationType.PAGAMENTO)
                .hpan("hpan")
                .merchantId("0")
                .circuitType("00")
                .mcc("813")
                .idTrxIssuer("0")
                .amountCurrency("833")
                .correlationId("1")
                .acquirerId("0")
                .bin("000001")
                .terminalId("0")
                .build();
    }

    @Override
    protected String getTopic() {
        return topic;
    }

    @Override
    protected void verifyInvocation(String json) {
        try {
            BDDMockito.verify(saveTransactionCommandModelFactorySpy, Mockito.atLeastOnce())
                    .createModel(Mockito.any());
            BDDMockito.verify(objectMapperSpy, Mockito.atLeastOnce())
                    .readValue(Mockito.anyString(), Mockito.eq(Transaction.class));
            BDDMockito.verify(saveTransactionCommandMock, Mockito.atLeastOnce()).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Override
    protected ErrorPublisherService getErrorPublisherService() {
        return transactionErrorPublisherServiceMock;
    }

}