package it.gov.pagopa.bpd.winning_transaction.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.DummyConfiguration;
import eu.sia.meda.error.config.LocalErrorConfig;
import eu.sia.meda.error.handler.MedaExceptionHandler;
import eu.sia.meda.error.service.impl.LocalErrorManagerServiceImpl;
import it.gov.pagopa.bpd.winning_transaction.assembler.*;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.TrxCountByDay;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionId;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionMilestone;
import it.gov.pagopa.bpd.winning_transaction.exception.WinningTransactionExistsException;
import it.gov.pagopa.bpd.winning_transaction.factory.WinningTransactionModelFactory;
import it.gov.pagopa.bpd.winning_transaction.resource.dto.WinningTransactionDTO;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.FindWinningTransactionResource;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionPage;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionResource;
import it.gov.pagopa.bpd.winning_transaction.service.WinningTransactionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BpdWinningTransactionControllerImpl.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {
        BpdWinningTransactionControllerImpl.class,
        DummyConfiguration.class,
        MedaExceptionHandler.class,
        LocalErrorManagerServiceImpl.class
})
@Import(LocalErrorConfig.class)
@TestPropertySource(properties = {
        "error-manager.enabled=true",
        "spring.application.name=bpd-ms-citizen-api-rest"
})
public class BpdWinningTransactionControllerImplTest {

    private final OffsetDateTime offsetDateTime = OffsetDateTime.parse("2020-04-09T16:22:45.304Z");

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    private WinningTransactionModelFactory winningTransactionFactorySpy;

    @SpyBean
    private WinningTransactionResourceAssembler winningTransactionResourceAssemblerSpy;

    @SpyBean
    private FindWinningTransactionResourceAssembler findWinningTransactionResourceAssemblerSpy;

    @SpyBean
    private WinningTransactionPageResourceAssembler winningTransactionPageResourceAssemblerSpy;

    @SpyBean
    private WinningTransactionMilestoneResourceAssembler winningTransactionMilestoneResourceAssemblerSpy;

    @SpyBean
    private TrxCountByDayResourceAssembler trxCountByDayResourceAssemblerSpy;

    private final WinningTransaction newTransaction =
            WinningTransaction.builder().acquirerCode("0").acquirerId("0").amount(BigDecimal.valueOf(1313.3))
                    .amountCurrency("833").awardPeriodId(0L).circuitType("00")
                    .correlationId("0").hpan("hpan").idTrxAcquirer("0").idTrxIssuer("0").mcc("00")
                    .mccDescription("test").merchantId("0").operationType("00").score(BigDecimal.valueOf(1313.3))
                    .trxDate(offsetDateTime).bin("000011").terminalId("01301313").build();
    private final WinningTransactionMatcher newTransactionMatcher = new WinningTransactionMatcher(newTransaction);

    private final WinningTransactionMilestone newTransactionMilestone = Mockito.mock(WinningTransactionMilestone.class);
    private final TrxCountByDay newTrxCountByDay = Mockito.mock(TrxCountByDay.class);

    @MockBean
    private WinningTransactionService winningTransactionServiceMock;

    @Autowired
    ObjectMapper mapper;

    private final String BASE_URL = "/bpd/winning-transactions";

    private Random rand = new Random();
    private final Long totalScore = rand.nextLong();
    private final WinningTransactionDTO newTransactionDTO =
            WinningTransactionDTO.builder().acquirerCode("0").acquirerId("0").amount(BigDecimal.valueOf(1313.3))
                    .amountCurrency("833").awardPeriodId(0L).circuitType("00")
                    .correlationId("0").hpan("hpan").idTrxAcquirer("0").idTrxIssuer("0").mcc("00")
                    .mccDescription("test").merchantId("0").operationType("00").score(BigDecimal.valueOf(1313.3))
                    .trxDate(offsetDateTime).bin("000011").terminalId("01301313").build();
    private final WinningTransactionDTOMatcher newTransactionDTOMatcher = new WinningTransactionDTOMatcher(newTransactionDTO);


    private static class WinningTransactionMatcher implements ArgumentMatcher<WinningTransaction> {
        private final WinningTransaction expected;

        public WinningTransactionMatcher(WinningTransaction expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(WinningTransaction obj) {
            if (obj == null) {
                return false;
            }

            return StringUtils.equals(expected.getIdTrxAcquirer(), obj.getIdTrxAcquirer())
                    && StringUtils.equals(expected.getAcquirerCode(), obj.getAcquirerCode())
                    && expected.getTrxDate().isEqual(obj.getTrxDate())
                    && StringUtils.equals(expected.getOperationType(), obj.getOperationType())
                    && StringUtils.equals(expected.getAcquirerId(), obj.getAcquirerId());
        }

        @Override
        public String toString() {
            return "WinningTransactionMatcher{" +
                    "expected=" + expected +
                    '}';
        }
    }

    private static class WinningTransactionDTOMatcher implements ArgumentMatcher<WinningTransactionDTO> {
        private final WinningTransactionDTO expected;

        public WinningTransactionDTOMatcher(WinningTransactionDTO expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(WinningTransactionDTO obj) {
            if (obj == null) {
                return false;
            }

            return StringUtils.equals(expected.getIdTrxAcquirer(), obj.getIdTrxAcquirer())
                    && StringUtils.equals(expected.getAcquirerCode(), obj.getAcquirerCode())
                    && expected.getTrxDate().isEqual(obj.getTrxDate())
                    && StringUtils.equals(expected.getOperationType(), obj.getOperationType())
                    && StringUtils.equals(expected.getAcquirerId(), obj.getAcquirerId());
        }

        @Override
        public String toString() {
            return "WinningTransactionDTOMatcher{" +
                    "expected=" + expected +
                    '}';
        }
    }

    @Before
    public void initTest() {
        Mockito.reset(
                winningTransactionFactorySpy,
                winningTransactionResourceAssemblerSpy,
                winningTransactionPageResourceAssemblerSpy,
                winningTransactionMilestoneResourceAssemblerSpy,
                trxCountByDayResourceAssemblerSpy,
                winningTransactionServiceMock);

        Mockito.when(newTransactionMilestone.getAmount()).thenReturn(BigDecimal.valueOf(10.0));
        Mockito.when(newTransactionMilestone.getAwardPeriodId()).thenReturn(0L);
        Mockito.when(newTransactionMilestone.getCashback()).thenReturn(BigDecimal.valueOf(1.0));
        Mockito.when(newTransactionMilestone.getTrxDate()).thenReturn(offsetDateTime);
        Mockito.when(newTransactionMilestone.getCircuitType()).thenReturn("circuitType");
        Mockito.when(newTransactionMilestone.getHashPan()).thenReturn("hpan");
        Mockito.when(newTransactionMilestone.getIdTrxAcquirer()).thenReturn("idTrxAcquirer");
        Mockito.when(newTransactionMilestone.getIdTrxIssuer()).thenReturn("idTrxIssuer");
        Mockito.when(newTransactionMilestone.getAcquirerCode()).thenReturn("acquirerCode");
        Mockito.when(newTransactionMilestone.getAcquirerId()).thenReturn("acquirerId");
        Mockito.when(newTransactionMilestone.getOperationType()).thenReturn("operationType");

        Mockito.when(newTrxCountByDay.getCount()).thenReturn(1L);
        Mockito.when(newTrxCountByDay.getTrxDate()).thenReturn(Timestamp.valueOf(offsetDateTime.toLocalDateTime()));
    }

    @Test
    public void createWinningTransaction_Ok() throws Exception {

        BDDMockito.doReturn(newTransaction)
                .when(winningTransactionServiceMock)
                .create(Mockito.argThat(newTransactionMatcher));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .content(mapper.writeValueAsString(newTransactionDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        String contentString = result.getResponse().getContentAsString();
        assertNotNull(contentString);
        assertFalse(Strings.isBlank(contentString));

        WinningTransactionResource winningTransaction = mapper.readValue(
                contentString, WinningTransactionResource.class);

        assertEquals(winningTransaction.getAcquirerCode(), newTransaction.getAcquirerCode());

        BDDMockito.verify(winningTransactionFactorySpy, Mockito.atLeastOnce())
                .createModel(Mockito.argThat(newTransactionDTOMatcher));
        BDDMockito.verify(winningTransactionServiceMock, Mockito.atLeastOnce())
                .create(Mockito.argThat(newTransactionMatcher));
        BDDMockito.verify(winningTransactionResourceAssemblerSpy, Mockito.atLeastOnce())
                .toResource(Mockito.argThat(newTransactionMatcher));

    }

    @Test
    public void createWinningTransaction_KoForDuplicateEntry() throws Exception {

        BDDMockito.doThrow(
                new WinningTransactionExistsException(WinningTransactionId.builder()
                        .acquirerId(newTransaction.getAcquirerId())
                        .acquirerCode(newTransaction.getAcquirerCode())
                        .idTrxAcquirer(newTransaction.getIdTrxAcquirer())
                        .operationType(newTransaction.getOperationType())
                        .trxDate(newTransaction.getTrxDate())
                        .build()))
                .when(winningTransactionServiceMock)
                .create(Mockito.any(WinningTransaction.class));

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .content(mapper.writeValueAsString(newTransactionDTO))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CONFLICT.value()));

        BDDMockito.verify(winningTransactionFactorySpy, Mockito.atLeastOnce())
                .createModel(Mockito.argThat(newTransactionDTOMatcher));
        BDDMockito.verify(winningTransactionServiceMock, Mockito.atLeastOnce())
                .create(Mockito.argThat(newTransactionMatcher));
        BDDMockito.verify(winningTransactionResourceAssemblerSpy, Mockito.never())
                .toResource((Mockito.argThat(newTransactionMatcher)));

    }

    @Test
    public void createWinningTransaction_KoForDtoValidation() throws Exception {

        WinningTransactionDTO emptyAcquirerCodeTransaction = WinningTransactionDTO.builder().build();
        BeanUtils.copyProperties(newTransaction, emptyAcquirerCodeTransaction);

        emptyAcquirerCodeTransaction.setAcquirerCode(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .content(mapper.writeValueAsString(emptyAcquirerCodeTransaction))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        BDDMockito.verify(winningTransactionFactorySpy, Mockito.never()).createModel(Mockito.any());
        BDDMockito.verify(winningTransactionServiceMock, Mockito.never()).create(Mockito.any());
        BDDMockito.verify(winningTransactionResourceAssemblerSpy, Mockito.never()).toResource((Mockito.any()));

        WinningTransactionDTO emptyIdTrxAcquirerTransaction = WinningTransactionDTO.builder().build();
        BeanUtils.copyProperties(newTransaction, emptyIdTrxAcquirerTransaction);

        emptyIdTrxAcquirerTransaction.setIdTrxAcquirer(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .content(mapper.writeValueAsString(emptyIdTrxAcquirerTransaction))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        BDDMockito.verify(winningTransactionFactorySpy, Mockito.never()).createModel(Mockito.any());
        BDDMockito.verify(winningTransactionServiceMock, Mockito.never()).create(Mockito.any());
        BDDMockito.verify(winningTransactionResourceAssemblerSpy, Mockito.never()).toResource((Mockito.any()));

        WinningTransactionDTO emptyTrxDateTransaction = WinningTransactionDTO.builder().build();
        BeanUtils.copyProperties(newTransaction, emptyTrxDateTransaction);

        emptyTrxDateTransaction.setTrxDate(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .content(mapper.writeValueAsString(emptyTrxDateTransaction))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        BDDMockito.verify(winningTransactionFactorySpy, Mockito.never()).createModel(Mockito.any());
        BDDMockito.verify(winningTransactionServiceMock, Mockito.never()).create(Mockito.any());
        BDDMockito.verify(winningTransactionResourceAssemblerSpy, Mockito.never()).toResource((Mockito.any()));

        WinningTransactionDTO exceedingOperationType = WinningTransactionDTO.builder().build();
        BeanUtils.copyProperties(newTransaction, exceedingOperationType);

        exceedingOperationType.setOperationType("exceeding");

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .content(mapper.writeValueAsString(exceedingOperationType))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        BDDMockito.verify(winningTransactionFactorySpy, Mockito.never()).createModel(Mockito.any());
        BDDMockito.verify(winningTransactionServiceMock, Mockito.never()).create(Mockito.any());
        BDDMockito.verify(winningTransactionResourceAssemblerSpy, Mockito.never()).toResource((Mockito.any()));

        WinningTransactionDTO exceedingCircuitType = WinningTransactionDTO.builder().build();
        BeanUtils.copyProperties(newTransaction, exceedingCircuitType);

        exceedingCircuitType.setOperationType("exceeding");

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .content(mapper.writeValueAsString(exceedingCircuitType))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        BDDMockito.verify(winningTransactionFactorySpy, Mockito.never()).createModel(Mockito.any());
        BDDMockito.verify(winningTransactionServiceMock, Mockito.never()).create(Mockito.any());
        BDDMockito.verify(winningTransactionResourceAssemblerSpy, Mockito.never()).toResource((Mockito.any()));

        WinningTransactionDTO exceedingMcc = WinningTransactionDTO.builder().build();
        BeanUtils.copyProperties(newTransaction, exceedingMcc);

        exceedingMcc.setOperationType("exceeding");

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .content(mapper.writeValueAsString(exceedingMcc))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        BDDMockito.verify(winningTransactionFactorySpy, Mockito.never()).createModel(Mockito.any());
        BDDMockito.verify(winningTransactionServiceMock, Mockito.never()).create(Mockito.any());
        BDDMockito.verify(winningTransactionResourceAssemblerSpy, Mockito.never()).toResource((Mockito.any()));

        WinningTransactionDTO exceedingHpan = WinningTransactionDTO.builder().build();
        BeanUtils.copyProperties(newTransaction, exceedingHpan);

        exceedingHpan.setOperationType("exceeding");

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .content(mapper.writeValueAsString(exceedingHpan))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        BDDMockito.verify(winningTransactionFactorySpy, Mockito.never()).createModel(Mockito.any());
        BDDMockito.verify(winningTransactionServiceMock, Mockito.never()).create(Mockito.any());
        BDDMockito.verify(winningTransactionResourceAssemblerSpy, Mockito.never()).toResource((Mockito.any()));

    }

    @Test
    public void findWinningTransactions_OkWithElement() throws Exception {

        String fiscalCode = "DSULTN82H03H904Q";
        String hpan = "hpan";
        Long awardPeriodId = 0L;

        BDDMockito.doReturn(Collections.singletonList(newTransaction))
                .when(winningTransactionServiceMock)
                .getWinningTransactions(Mockito.eq(hpan), Mockito.eq(awardPeriodId), Mockito.eq(fiscalCode));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_URL)
                        .param("hpan", hpan)
                        .param("awardPeriodId", String.valueOf(awardPeriodId))
                        .param("fiscalCode", fiscalCode)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        String contentString = result.getResponse().getContentAsString();
        assertNotNull(contentString);
        assertFalse(Strings.isBlank(contentString));

        List<FindWinningTransactionResource> winningTransactions = mapper.readValue(
                contentString, new TypeReference<List<FindWinningTransactionResource>>() {
                });

        assertEquals(winningTransactions.size(), 1);
        assertEquals(winningTransactions.get(0).getIdTrxIssuer(), newTransaction.getIdTrxIssuer());

        BDDMockito.verify(winningTransactionServiceMock, Mockito.atLeastOnce())
                .getWinningTransactions(Mockito.eq(hpan), Mockito.eq(awardPeriodId), Mockito.eq(fiscalCode));
        BDDMockito.verify(findWinningTransactionResourceAssemblerSpy, Mockito.times(winningTransactions.size()))
                .toResource(Mockito.any(WinningTransaction.class));
        BDDMockito.verify(findWinningTransactionResourceAssemblerSpy, Mockito.atMost(1))
                .toResource(Mockito.eq(newTransaction));

    }

    @Test
    public void findWinningTransactionsMilestonePage_OkWithElement() throws Exception {

        String fiscalCode = "DSULTN82H03H904Q";
        String hpan = "hpan";
        Long awardPeriodId = 0L;
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("trxDate"), Sort.Order.desc("insertDate")));
        String idTrxExpected = "idTrxAcquirer" + "2020-04-09T16:22:45.304Z" + "acquirerCode" + "acquirerId" + "operationType";

        BDDMockito.doReturn(new PageImpl<>(Collections.singletonList(newTransactionMilestone)))
                .when(winningTransactionServiceMock)
                .getWinningTransactionsMilestonePage(Mockito.eq(hpan), Mockito.eq(awardPeriodId), Mockito.eq(fiscalCode), Mockito.eq(pageable));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/milestone/page")
                        .param("hpan", hpan)
                        .param("awardPeriodId", String.valueOf(awardPeriodId))
                        .param("fiscalCode", fiscalCode)
                        .param("nextCursor", "0")
                        .param("limit", "1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        String contentString = result.getResponse().getContentAsString();
        assertNotNull(contentString);
        assertFalse(Strings.isBlank(contentString));

        WinningTransactionPage winningTransactionsObject = mapper.readValue(
                contentString, new TypeReference<WinningTransactionPage>() {
                });

        assertNotNull(winningTransactionsObject);
        assertEquals(winningTransactionsObject.getTransactions().size(), 1);
        assertEquals(idTrxExpected, winningTransactionsObject.getTransactions().get(0).getTransactions().get(0).getIdTrx());

        BDDMockito.verify(winningTransactionServiceMock, Mockito.atLeastOnce())
                .getWinningTransactionsMilestonePage(Mockito.eq(hpan), Mockito.eq(awardPeriodId), Mockito.eq(fiscalCode), Mockito.eq(pageable));
        BDDMockito.verify(winningTransactionMilestoneResourceAssemblerSpy, Mockito.times(winningTransactionsObject.getTransactions().size()))
                .toWinningTransactionMilestoneResource(Mockito.any(WinningTransactionMilestone.class));
        BDDMockito.verify(winningTransactionPageResourceAssemblerSpy, Mockito.times(winningTransactionsObject.getTransactions().size()))
                .toWinningTransactionsOfTheDayResource(Mockito.any());
        BDDMockito.verify(winningTransactionPageResourceAssemblerSpy, Mockito.times(winningTransactionsObject.getTransactions().size()))
                .toWinningTransactionPageResource(Mockito.any(), Mockito.eq(0), Mockito.anyList());
    }

    @Test
    public void getCountByDay_OkWithElement() throws Exception {
        String fiscalCode = "DSULTN82H03H904Q";
        String hpan = "hpan";
        Long awardPeriodId = 0L;

        BDDMockito.doReturn(Collections.singletonList(newTrxCountByDay))
                .when(winningTransactionServiceMock)
                .getWinningTransactionByDateCount(Mockito.eq(hpan), Mockito.eq(awardPeriodId), Mockito.eq(fiscalCode));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/countbyday")
                        .param("hpan", hpan)
                        .param("awardPeriodId", String.valueOf(awardPeriodId))
                        .param("fiscalCode", fiscalCode)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        String contentString = result.getResponse().getContentAsString();
        assertNotNull(contentString);
        assertFalse(Strings.isBlank(contentString));

        BDDMockito.verify(winningTransactionServiceMock, Mockito.atLeastOnce())
                .getWinningTransactionByDateCount(Mockito.eq(hpan), Mockito.eq(awardPeriodId), Mockito.eq(fiscalCode));
        BDDMockito.verify(trxCountByDayResourceAssemblerSpy, Mockito.times(1))
                .toTrxCountByDayResource(Mockito.any(TrxCountByDay.class));
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/fiscalCode"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        verify(winningTransactionServiceMock).deleteByFiscalCode(any());
    }

    @Test
    public void rollback() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/rollback/fiscalCode")
                .param("requestTimestamp", offsetDateTime.format(dateTimeFormatter)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        verify(winningTransactionServiceMock).reactivateForRollback(any(), any());
    }

}