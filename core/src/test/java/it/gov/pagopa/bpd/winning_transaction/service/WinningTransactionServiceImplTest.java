package it.gov.pagopa.bpd.winning_transaction.service;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.WinningTransactionDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionId;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = WinningTransactionServiceImpl.class)
@TestPropertySource(properties = "winningTransaction.core.checkExists.enable=true")
public class WinningTransactionServiceImplTest {

    private final OffsetDateTime offsetDateTime = OffsetDateTime.parse("2020-04-09T16:22:45.304Z");

    @MockBean
    private WinningTransactionDAO winningTransactionDAOMock;

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

    private final WinningTransactionId newTransactionId =
            WinningTransactionId.builder()
                    .idTrxAcquirer("0")
                    .acquirerCode("0")
                    .trxDate(offsetDateTime)
                    .operationType("00")
                    .acquirerId("0")
                    .build();


    @Before
    public void initTest() {

        Mockito.reset(winningTransactionDAOMock);

        BDDMockito.doReturn(newTransaction)
                .when(winningTransactionDAOMock)
                .save(Mockito.eq(newTransaction));
    }

    @Test
    public void create_ok() {

        WinningTransaction winningTransaction = winningTransactionService.create(newTransaction);
        assertNotNull(winningTransaction);
        assertEquals(winningTransaction, newTransaction);

        BDDMockito.verify(winningTransactionDAOMock, Mockito.atLeastOnce()).save(Mockito.eq(newTransaction));
    }


    @Test
    public void create_ko() {

        BDDMockito.doThrow(EntityNotFoundException.class)
                .when(winningTransactionDAOMock)
                .save(Mockito.any());

        exceptionRule.expect(EntityNotFoundException.class);

        WinningTransaction winningTransaction = winningTransactionService.create(newTransaction);
        assertNotNull(winningTransaction);
        assertEquals(winningTransaction, newTransaction);

        BDDMockito.verify(winningTransactionDAOMock, Mockito.never()).save(Mockito.eq(newTransaction));

    }

    @Test
    public void getWinningTransactionsNoHpan() {

        String fiscalCode = "fiscalCode";
        Long awardPeriodId = 0L;

        List<WinningTransaction> winningTransactions = Collections.singletonList(newTransaction);

        BDDMockito.doReturn(winningTransactions)
                .when(winningTransactionDAOMock)
                .findCitizenTransactions( Mockito.eq(fiscalCode), Mockito.eq(awardPeriodId));

        List<WinningTransaction> newWinningTransactions = winningTransactionService
                .getWinningTransactions(null, awardPeriodId, fiscalCode);

        assertNotNull(newWinningTransactions);
        assertEquals(newWinningTransactions.size(), 1);
        assertEquals(newWinningTransactions.get(0), newTransaction);

        BDDMockito.verify(winningTransactionDAOMock, Mockito.atLeastOnce())
                .findCitizenTransactions(
                        Mockito.eq(fiscalCode),
                        Mockito.eq(awardPeriodId));
    }

    @Test
    public void getWinningTransactionsWithHpan() {

        String fiscalCode = "fiscalCode";
        String hpan = "hpan";
        Long awardPeriodId = 0L;

        List<WinningTransaction> winningTransactions = Collections.singletonList(newTransaction);

        BDDMockito.doReturn(winningTransactions)
                .when(winningTransactionDAOMock)
                .findCitizenTransactionsByHpan( Mockito.eq(fiscalCode), Mockito.eq(awardPeriodId), Mockito.eq(hpan));

        List<WinningTransaction> newWinningTransactions = winningTransactionService
                .getWinningTransactions(hpan, awardPeriodId, fiscalCode);

        assertNotNull(newWinningTransactions);
        assertEquals(newWinningTransactions.size(), 1);
        assertEquals(newWinningTransactions.get(0), newTransaction);

        BDDMockito.verify(winningTransactionDAOMock, Mockito.atLeastOnce())
                .findCitizenTransactionsByHpan(
                        Mockito.eq(fiscalCode),
                        Mockito.eq(awardPeriodId),
                        Mockito.eq(hpan));
    }

    @Test
    public void deleteByFiscalCode() {
        final String fiscalCode = "fiscalCode";
        winningTransactionService.deleteByFiscalCode(fiscalCode);
        verify(winningTransactionDAOMock, times(1)).deactivateCitizenTransactions(eq(fiscalCode), any());
    }


    @Test
    public void reactivateForRollback() {
        final String fiscalCode = "fiscalCode";
        final OffsetDateTime requestTimestamp = OffsetDateTime.now();
        winningTransactionService.reactivateForRollback("fiscalCode", requestTimestamp);
        verify(winningTransactionDAOMock, times(1)).reactivateForRollback(eq(fiscalCode), eq(requestTimestamp), any());
    }

}