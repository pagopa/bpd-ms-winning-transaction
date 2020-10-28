package it.gov.pagopa.bpd.winning_transaction.connector;

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import it.gov.pagopa.bpd.common.connector.BaseFeignRestClientTest;
import it.gov.pagopa.bpd.winning_transaction.connector.config.CitizenRestConnectorConfig;
import it.gov.pagopa.bpd.winning_transaction.connector.model.CitizenResource;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@TestPropertySource(
        locations = "classpath:config/rest-client.properties",
        properties = {
                "logging.level.it.gov.pagopa.bpd.winning-transaction=DEBUG",
                "spring.application.name=bpd-ms-winning-transaction-integration-rest"
        })
@ContextConfiguration(initializers = CitizenRestClientTest.RandomPortInitializer.class,
        classes = CitizenRestConnectorConfig.class)
public class CitizenRestClientTest extends BaseFeignRestClientTest {

    @ClassRule
    public static WireMockClassRule wireMockRule;

    static {
        String port = System.getenv("WIREMOCKPORT");
        wireMockRule = new WireMockClassRule(wireMockConfig()
                .port(port != null ? Integer.parseInt(port) : 0)
                .bindAddress("localhost")
                .usingFilesUnderClasspath("stubs")
                .extensions(new ResponseTemplateTransformer(false))
        );
    }

    @Autowired
    private CitizenRestClient restClient;

    @Test
    public void findById() {
        final String hpan = "hpan";
        final String fiscalCode = "fiscalCode";
        final Long aw_period = 1L;

        final CitizenResource actualResponse = restClient.getTotalCashback(hpan, fiscalCode, aw_period);

        Assert.assertNotNull(actualResponse);
    }

    public static class RandomPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @SneakyThrows
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils
                    .addInlinedPropertiesToEnvironment(applicationContext,
                            String.format("rest-client.citizen.base-url=http://%s:%d/bpd/citizens",
                                    wireMockRule.getOptions().bindAddress(),
                                    wireMockRule.port())
                    );
        }
    }


}
