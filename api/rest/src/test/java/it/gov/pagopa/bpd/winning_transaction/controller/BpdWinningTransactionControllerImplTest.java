package it.gov.pagopa.bpd.winning_transaction.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.bpd.winning_transaction.assembler.FindWinningTransactionResourceAssembler;
import it.gov.pagopa.bpd.winning_transaction.assembler.TotalScoreResourceAssembler;
import it.gov.pagopa.bpd.winning_transaction.assembler.WinningTransactionResourceAssembler;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.TotalScoreResourceDTO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.factory.WinningTransactionModelFactory;
import it.gov.pagopa.bpd.winning_transaction.resource.dto.WinningTransactionDTO;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.FindWinningTransactionResource;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.TotalScoreResource;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionResource;
import it.gov.pagopa.bpd.winning_transaction.service.WinningTransactionService;
import org.apache.logging.log4j.util.Strings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityExistsException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BpdWinningTransactionControllerImpl.class)
@WebMvcTest(value = BpdWinningTransactionControllerImpl.class, secure = false)
public class BpdWinningTransactionControllerImplTest {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    private WinningTransactionModelFactory winningTransactionFactorySpy;

    @SpyBean
    private WinningTransactionResourceAssembler winningTransactionResourceAssemblerSpy;

    @SpyBean
    private FindWinningTransactionResourceAssembler findWinningTransactionResourceAssemblerSpy;

    private final WinningTransaction newTransaction =
            WinningTransaction.builder().acquirerCode("0").acquirerId("0").amount(BigDecimal.valueOf(1313.3))
                    .amountCurrency("833").awardPeriodId(0L).circuitType("00")
                    .correlationId("0").hpan("hpan").idTrxAcquirer("0").idTrxIssuer("0").mcc("00")
                    .mccDescription("test").merchantId("0").operationType("00").score(BigDecimal.valueOf(1313.3))
                    .trxDate(offsetDateTime).bin("000011").terminalId("01301313").build();

    @MockBean
    private WinningTransactionService winningTransactionServiceMock;

    @Autowired
    ObjectMapper mapper;

    private final String BASE_URL = "/bpd/winning-transactions";

    private final OffsetDateTime offsetDateTime = OffsetDateTime.parse("2020-04-09T16:22:45.304Z");

    private Random rand = new Random();
    private final Long totalScore = rand.nextLong();
    private final WinningTransactionDTO newTransactionDTO =
            WinningTransactionDTO.builder().acquirerCode("0").acquirerId("0").amount(BigDecimal.valueOf(1313.3))
                    .amountCurrency("833").awardPeriodId(0L).circuitType("00")
                    .correlationId("0").hpan("hpan").idTrxAcquirer("0").idTrxIssuer("0").mcc("00")
                    .mccDescription("test").merchantId("0").operationType("00").score(BigDecimal.valueOf(1313.3))
                    .trxDate(offsetDateTime).bin("000011").terminalId("01301313").build();
    @SpyBean
    private TotalScoreResourceAssembler totalScoreResourceAssemblerSpy;


    @Before
    public void initTest() {
        Mockito.reset(
                winningTransactionFactorySpy,
                winningTransactionResourceAssemblerSpy,
                winningTransactionServiceMock);
    }

    @Test
    public void createWinningTransaction_Ok() throws Exception {

        BDDMockito.doReturn(newTransaction)
                .when(winningTransactionServiceMock)
                .create(Mockito.eq(newTransaction));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .content(mapper.writeValueAsString(newTransactionDTO))
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        String contentString = result.getResponse().getContentAsString();
        assertNotNull(contentString);
        assertFalse(Strings.isBlank(contentString));

        WinningTransactionResource winningTransaction = mapper.readValue(
                contentString, WinningTransactionResource.class);

        assertEquals(winningTransaction.getAcquirerCode(), newTransaction.getAcquirerCode());

        BDDMockito.verify(winningTransactionFactorySpy, Mockito.atLeastOnce())
                .createModel(Mockito.eq(newTransactionDTO));
        BDDMockito.verify(winningTransactionServiceMock, Mockito.atLeastOnce())
                .create(Mockito.eq(newTransaction));
        BDDMockito.verify(winningTransactionResourceAssemblerSpy, Mockito.atLeastOnce())
                .toResource(Mockito.eq(newTransaction));

    }

    @Test
    public void createWinningTransaction_KoForDuplicateEntry() throws Exception {

        BDDMockito.doThrow(
                new EntityExistsException("WinningTransaction with id:" +
                        newTransaction.getIdTrxAcquirer() + "," +
                        newTransaction.getAcquirerCode() + "," +
                        newTransaction.getTrxDate() +" already exists"))
                .when(winningTransactionServiceMock)
                .create(Mockito.any(WinningTransaction.class));

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .content(mapper.writeValueAsString(newTransactionDTO))
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CONFLICT.value()));

        BDDMockito.verify(winningTransactionFactorySpy, Mockito.atLeastOnce())
                .createModel(Mockito.eq(newTransactionDTO));
        BDDMockito.verify(winningTransactionServiceMock, Mockito.atLeastOnce())
                .create(Mockito.eq(newTransaction));
        BDDMockito.verify(winningTransactionResourceAssemblerSpy, Mockito.never())
                .toResource((Mockito.eq(newTransaction)));

    }

    @Test
    public void createWinningTransaction_KoForDtoValidation() throws Exception {

        WinningTransactionDTO emptyAcquirerCodeTransaction = WinningTransactionDTO.builder().build();
        BeanUtils.copyProperties(newTransaction, emptyAcquirerCodeTransaction);

        emptyAcquirerCodeTransaction.setAcquirerCode(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post(BASE_URL)
                        .content(mapper.writeValueAsString(emptyAcquirerCodeTransaction))
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        BDDMockito.verify(winningTransactionFactorySpy, Mockito.never()).createModel(Mockito.any());
        BDDMockito.verify(winningTransactionServiceMock, Mockito.never()).create(Mockito.any());
        BDDMockito.verify(winningTransactionResourceAssemblerSpy, Mockito.never()).toResource((Mockito.any()));

    }

    @Test
    public void findWinningTransactions_OkWithElement() throws Exception {

        String hpan = "hpan";
        Long awardPeriodId = 0L;

        BDDMockito.doReturn(Collections.singletonList(newTransaction))
                .when(winningTransactionServiceMock)
                .getWinningTransactions(Mockito.eq(hpan), Mockito.eq(awardPeriodId));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_URL)
                .param("hpan", hpan)
                .param("awardPeriodId", String.valueOf(awardPeriodId))
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        String contentString = result.getResponse().getContentAsString();
        assertNotNull(contentString);
        assertFalse(Strings.isBlank(contentString));

        List<FindWinningTransactionResource> winningTransactions = mapper.readValue(
                contentString, new TypeReference<List<FindWinningTransactionResource>>() {});

        assertEquals(winningTransactions.size(), 1);
        assertEquals(winningTransactions.get(0).getIdTrxIssuer(), newTransaction.getIdTrxIssuer());

        BDDMockito.verify(winningTransactionServiceMock, Mockito.atLeastOnce())
                .getWinningTransactions(Mockito.eq(hpan), Mockito.eq(awardPeriodId));
        BDDMockito.verify(findWinningTransactionResourceAssemblerSpy, Mockito.times(winningTransactions.size()))
                .toResource(Mockito.any(WinningTransaction.class));
        BDDMockito.verify(findWinningTransactionResourceAssemblerSpy, Mockito.atMost(1))
                .toResource(Mockito.eq(newTransaction));

    }

    @Test
    public void getTotalScore_OK() throws Exception {

        String hpan = "hpan";
        Long awardPeriodId = 0L;
        String fiscalCode = "fiscalCode";
        BigDecimal testTotalScore = new BigDecimal(totalScore);
        TotalScoreResourceDTO totalScoreResource = new TotalScoreResourceDTO();
        totalScoreResource.setTotalScore(testTotalScore);

        BDDMockito.doReturn(totalScoreResource)
                .when(winningTransactionServiceMock)
                .getTotalScore(Mockito.eq(hpan), Mockito.eq(awardPeriodId), Mockito.eq(fiscalCode));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/total-cashback")
                        .param("hpan", hpan)
                        .param("awardPeriodId", String.valueOf(awardPeriodId))
                        .param("fiscalCode", fiscalCode)
                        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();


        String contentString = result.getResponse().getContentAsString();
        assertNotNull(contentString);
        assertFalse(Strings.isBlank(contentString));

        TotalScoreResource resource = mapper.readValue(
                contentString, new TypeReference<TotalScoreResource>() {
                });

        BigDecimal finalTotalScore = new BigDecimal(totalScore);
        assertEquals(resource.getTotalScore(), finalTotalScore);

        BDDMockito.verify(winningTransactionServiceMock, Mockito.atLeastOnce())
                .getTotalScore(Mockito.eq(hpan), Mockito.eq(awardPeriodId), Mockito.eq(fiscalCode));

    }

    @Test
    public void getTotalScore_nullAwardPeriod() throws Exception {
        String hpan = "hpan";
        Long awardPeriodId = null;

        MvcResult result = (MvcResult) mockMvc.perform(MockMvcRequestBuilders
                .get(BASE_URL + "/total-score")
                .param("hpan", hpan)
                .param("awardPeriodId", String.valueOf(awardPeriodId))
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        Mockito.verifyZeroInteractions(winningTransactionServiceMock);
    }

}