package it.gov.pagopa.bpd.winning_transaction.service;

import it.gov.pagopa.bpd.winning_transaction.connector.CitizenRestClient;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.CitizenTransactionDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.WinningTransactionDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionId;
import it.gov.pagopa.bpd.winning_transaction.connector.model.CitizenResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = WinningTransactionServiceImpl.class)
public class WinningTransactionServiceImplTest {

    private final OffsetDateTime offsetDateTime = OffsetDateTime.parse("2020-04-09T16:22:45.304Z");

    @MockBean
    private WinningTransactionDAO winningTransactionDAOMock;

    @MockBean
    private CitizenRestClient citizenRestClientMock;

    private final WinningTransaction newTransaction =
            WinningTransaction.builder().acquirerCode("0").acquirerId("0").amount(BigDecimal.valueOf(1313.3))
                    .amountCurrency("833").awardPeriodId(0L).circuitType("00")
                    .correlationId("0").hpan("hpan").idTrxAcquirer("0").idTrxIssuer("0").mcc("00")
                    .mccDescription("test").merchantId("0").operationType("00").score(BigDecimal.valueOf(1313.3))
                    .trxDate(offsetDateTime).build();

    @Autowired
    private WinningTransactionService winningTransactionService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @MockBean
    private CitizenTransactionDAO citizenTransactionDAOMock;
    private final WinningTransactionId newTransactionId =
            WinningTransactionId.builder()
                    .idTrxAcquirer("0")
                    .acquirerCode("0")
                    .trxDate(offsetDateTime)
                    .build();

    private Random rand = new Random();

    @Before
    public void initTest() {

        Mockito.reset(winningTransactionDAOMock);

        BDDMockito.doReturn(newTransaction)
                .when(winningTransactionDAOMock)
                .save(Mockito.eq(newTransaction));

    }

    @Test
    public void create_ok() {

        BDDMockito.doReturn(false)
                .when(winningTransactionDAOMock)
                .existsById(Mockito.eq(newTransactionId));

        WinningTransaction winningTransaction = winningTransactionService.create(newTransaction);
        assertNotNull(winningTransaction);
        assertEquals(winningTransaction, newTransaction);
//TODO: Risistemare a termine UAT

//        BDDMockito.verify(winningTransactionDAOMock, Mockito.atLeastOnce()).existsById(Mockito.eq(newTransactionId));
        BDDMockito.verify(winningTransactionDAOMock, Mockito.atLeastOnce()).save(Mockito.eq(newTransaction));

    }

//TODO: Risistemare a termine UAT

//    @Test
//    public void create_ko() {
//
//        BDDMockito.doReturn(true)
//                .when(winningTransactionDAOMock)
//                .existsById(Mockito.eq(newTransactionId));
//
//        exceptionRule.expect(WinningTransactionExistsException.class);
//
//        WinningTransaction winningTransaction = winningTransactionService.create(newTransaction);
//        assertNotNull(winningTransaction);
//        assertEquals(winningTransaction, newTransaction);
//
//        BDDMockito.verify(winningTransactionDAOMock, Mockito.atLeastOnce()).existsById(Mockito.eq(newTransactionId));
//        BDDMockito.verify(winningTransactionDAOMock, Mockito.never()).save(Mockito.eq(newTransaction));
//
//    }

    @Test
    public void getWinningTransactions() {

        String hpan = "hashpan";
        Long awardPeriodId = 0L;

        List<WinningTransaction> winningTransactions = Collections.singletonList(newTransaction);

        BDDMockito.doReturn(winningTransactions)
                .when(winningTransactionDAOMock)
                .findByHpanAndAwardPeriodId(
                        Mockito.eq(hpan),
                        Mockito.eq(awardPeriodId));

        List<WinningTransaction> newWinningTransactions = winningTransactionService
                .getWinningTransactions(hpan, awardPeriodId);

        assertNotNull(newWinningTransactions);
        assertEquals(newWinningTransactions.size(), 1);
        assertEquals(newWinningTransactions.get(0), newTransaction);

        BDDMockito.verify(winningTransactionDAOMock, Mockito.atLeastOnce())
                .findByHpanAndAwardPeriodId(
                        Mockito.eq(hpan),
                        Mockito.eq(awardPeriodId));

    }

    @Test
    public void getCashback() {

        String hpan = "hashpan";
        Long awardPeriodId = 0L;
        String fiscalCode = "fiscalCode";

        CitizenResource resource = new CitizenResource();
        resource.setTransactionNumber(1L);
        resource.setTotalCashback(new BigDecimal(1));

        BDDMockito.doReturn(resource).when(citizenRestClientMock)
                .getTotalCashback(
                        Mockito.eq(hpan),
                        Mockito.eq(fiscalCode),
                        Mockito.eq(awardPeriodId));

        CitizenResource cashback = winningTransactionService.getCashback(hpan, awardPeriodId, fiscalCode);

        assertNotNull(cashback);
        assertEquals(cashback.getTransactionNumber(), resource.getTransactionNumber());
        assertEquals(cashback.getTotalCashback(), resource.getTotalCashback());

        BDDMockito.verify(citizenRestClientMock, Mockito.atLeastOnce())
                .getTotalCashback(
                        Mockito.eq(hpan),
                        Mockito.eq(fiscalCode),
                        Mockito.eq(awardPeriodId));

    }

}