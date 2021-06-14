package it.gov.pagopa.bpd.winning_transaction.command;

import eu.sia.meda.BaseTest;
import it.gov.pagopa.bpd.winning_transaction.command.model.SaveTransactionCommandModel;
import it.gov.pagopa.bpd.winning_transaction.command.model.Transaction;
import it.gov.pagopa.bpd.winning_transaction.command.model.enums.OperationType;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.mapper.TransactionMapper;
import it.gov.pagopa.bpd.winning_transaction.service.CitizenStatusDataService;
import it.gov.pagopa.bpd.winning_transaction.service.WinningTransactionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class SaveTransactionCommandImplTest extends BaseTest {

    @Mock
    WinningTransactionService winningTransactionService;

    @Mock
    CitizenStatusDataService citizenStatusUpdateService;

    @Spy
    TransactionMapper transactionMapperSpy;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void initTest() {
        Mockito.reset(winningTransactionService, citizenStatusUpdateService, transactionMapperSpy);
    }

    @Test
    public void TestExecute_OK() {

        Transaction transaction = getRequestModel();
        WinningTransaction winningTransaction = getSavedModel();

        BDDMockito.doReturn(winningTransaction).when(winningTransactionService).create(Mockito.eq(winningTransaction));
        SaveTransactionCommandImpl saveTransactionCommand = new SaveTransactionCommandImpl(
                SaveTransactionCommandModel.builder().payload(transaction).build(),
                citizenStatusUpdateService,
                winningTransactionService,
                transactionMapperSpy);
        Boolean executed = saveTransactionCommand.doExecute();
        Mockito.verify(transactionMapperSpy).map(Mockito.eq(transaction));
        Mockito.verify(winningTransactionService).create(Mockito.eq(winningTransaction));
        Assert.assertTrue(executed);

    }

    @Test
    public void TestExecute_KO() {

        Transaction transaction = getRequestModel();
        transaction.setAcquirerCode(null);

        SaveTransactionCommandImpl saveTransactionCommand = new SaveTransactionCommandImpl(
                SaveTransactionCommandModel.builder().payload(getRequestModel()).build(),
                citizenStatusUpdateService,
                winningTransactionService,
                transactionMapperSpy);
        exceptionRule.expect(AssertionError.class);
        saveTransactionCommand.doExecute();
        Mockito.verifyZeroInteractions(winningTransactionService);

    }

    protected Transaction getRequestModel() {
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
                .awardPeriodId(1L)
                .score(BigDecimal.ONE)
                .bin("000001")
                .terminalId("0")
                .fiscalCode("fiscalCode")
                .build();
    }

    protected WinningTransaction getSavedModel() {
        return WinningTransaction.builder()
                .idTrxAcquirer("1")
                .acquirerCode("001")
                .trxDate(OffsetDateTime.parse("2020-04-09T16:22:45.304Z"))
                .amount(BigDecimal.valueOf(1313.13))
                .operationType("00")
                .hpan("hpan")
                .merchantId("0")
                .circuitType("00")
                .mcc("813")
                .idTrxIssuer("0")
                .amountCurrency("833")
                .correlationId("1")
                .acquirerId("0")
                .awardPeriodId(1L)
                .score(BigDecimal.ONE)
                .bin("000001")
                .terminalId("0")
                .fiscalCode("fiscalCode")
                .build();
    }

}