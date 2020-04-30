package it.gov.pagopa.bpd.winning_transaction.service;

import it.gov.pagopa.bpd.winning_transaction.WinningTransactionDAO;
import it.gov.pagopa.bpd.winning_transaction.exception.WinningTransactionNotFoundException;
import it.gov.pagopa.bpd.winning_transaction.model.entity.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.model.entity.WinningTransactionId;
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

import javax.persistence.EntityExistsException;
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

    @MockBean
    private WinningTransactionDAO winningTransactionDAOMock;

    @Autowired
    private WinningTransactionService winningTransactionService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private final OffsetDateTime offsetDateTime = OffsetDateTime.parse("2020-04-09T16:22:45.304Z");
    private final WinningTransaction newTransaction =
            WinningTransaction.builder().acquirerCode("0").acquirerId(0).amount(BigDecimal.valueOf(1313.3))
                    .amountCurrency("833").awardPeriodId(0L).circuitType("00")
                    .correlationId(0).hpan("hpan").idTrxAcquirer(0).idTrxIssuer(0).mcc("00")
                    .mccDescription("test").merchantId(0).operationType("00").score(BigDecimal.valueOf(1313.3))
                    .trxDate(offsetDateTime).build();
    private final WinningTransactionId newTransactionId =
            WinningTransactionId.builder()
                    .idTrxAcquirer(0)
                    .acquirerCode("0")
                    .trxDate(offsetDateTime)
                    .build();

    private Random rand = new Random();
    private final Long totalScore = rand.nextLong();

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
        assertEquals(winningTransaction,newTransaction);

        BDDMockito.verify(winningTransactionDAOMock, Mockito.atLeastOnce()).existsById(Mockito.eq(newTransactionId));
        BDDMockito.verify(winningTransactionDAOMock, Mockito.atLeastOnce()).save(Mockito.eq(newTransaction));

    }



    @Test
    public void create_ko() {

        BDDMockito.doReturn(true)
                .when(winningTransactionDAOMock)
                .existsById(Mockito.eq(newTransactionId));

        exceptionRule.expect(EntityExistsException.class);
        exceptionRule.expectMessage("WinningTransaction with id:" +
                newTransaction.getIdTrxAcquirer() + "," +
                newTransaction.getAcquirerCode() + "," +
                newTransaction.getTrxDate() +" already exists");

        WinningTransaction winningTransaction = winningTransactionService.create(newTransaction);
        assertNotNull(winningTransaction);
        assertEquals(winningTransaction,newTransaction);

        BDDMockito.verify(winningTransactionDAOMock, Mockito.atLeastOnce()).existsById(Mockito.eq(newTransactionId));
        BDDMockito.verify(winningTransactionDAOMock, Mockito.never()).save(Mockito.eq(newTransaction));

    }

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

    @Test(expected = WinningTransactionNotFoundException.class)
    public void getWinningTransactions_KO() {

        String wrongHashpan = "wrongHashpan";
        Long awardPeriodId = 0L;

        List<WinningTransaction> winningTransactions = Collections.EMPTY_LIST;

        BDDMockito.doReturn(winningTransactions)
                .when(winningTransactionDAOMock)
                .findByHpanAndAwardPeriodId(
                        Mockito.eq(wrongHashpan),
                        Mockito.eq(awardPeriodId));

        List<WinningTransaction> newWinningTransactions = winningTransactionService
                .getWinningTransactions(wrongHashpan, awardPeriodId);

    }

    @Test
    public void getTotalScore() {

        String hpan = "hashpan";
        Long awardPeriodId = 0L;

        BDDMockito.doReturn(totalScore)
                .when(winningTransactionDAOMock)
                .calculateTotalScore(
                        Mockito.eq(hpan),
                        Mockito.eq(awardPeriodId));

        Long newTotalScore = winningTransactionService.getTotalScore(hpan, awardPeriodId);

        assertNotNull(newTotalScore);
        assertEquals(newTotalScore, totalScore);

        BDDMockito.verify(winningTransactionDAOMock, Mockito.atLeastOnce())
                .calculateTotalScore(
                        Mockito.eq(hpan),
                        Mockito.eq(awardPeriodId));

    }

    @Test(expected = WinningTransactionNotFoundException.class)
    public void getTotalScore_KO() {

        String wrongHpan = "wrongHpan";
        Long awardPeriodId = 0L;

        BDDMockito.doReturn(null)
                .when(winningTransactionDAOMock)
                .calculateTotalScore(
                        Mockito.eq(wrongHpan),
                        Mockito.eq(awardPeriodId));

        Long newTotalScore = winningTransactionService.getTotalScore(wrongHpan, awardPeriodId);
    }
}