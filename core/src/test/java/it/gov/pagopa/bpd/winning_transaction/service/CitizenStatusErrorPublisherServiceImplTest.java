package it.gov.pagopa.bpd.winning_transaction.service;

import eu.sia.meda.event.transformer.ErrorEventRequestTransformer;
import eu.sia.meda.event.transformer.SimpleEventRequestTransformer;
import eu.sia.meda.event.transformer.SimpleEventResponseTransformer;
import it.gov.pagopa.bpd.common.BaseTest;
import it.gov.pagopa.bpd.winning_transaction.publisher.CitizenStatusErrorPublisherConnector;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

public class CitizenStatusErrorPublisherServiceImplTest extends BaseTest {

    @Mock
    CitizenStatusErrorPublisherConnector citizenStatusErrorPublisherConnector;

    @Spy
    ErrorEventRequestTransformer simpleEventRequestTransformer;

    @Spy
    SimpleEventResponseTransformer simpleEventResponseTransformer;

    @Before
    public void initTest() {
        Mockito.reset(citizenStatusErrorPublisherConnector);
    }

    @Test
    public void test_publish_OK() {

        CitizenStatusErrorPublisherService citizenStatusErrorPublisherService =
                new CitizenStatusErrorPublisherServiceImpl(
                        citizenStatusErrorPublisherConnector,
                        simpleEventRequestTransformer,
                        simpleEventResponseTransformer);
        citizenStatusErrorPublisherService.publishErrorEvent("test".getBytes(), new RecordHeaders(), "test");
        BDDMockito.verify(citizenStatusErrorPublisherConnector).call(
                Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());

    }

}
