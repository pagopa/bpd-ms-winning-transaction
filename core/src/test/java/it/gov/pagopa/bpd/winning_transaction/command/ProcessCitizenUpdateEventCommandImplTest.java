package it.gov.pagopa.bpd.winning_transaction.command;

import eu.sia.meda.BaseTest;
import it.gov.pagopa.bpd.winning_transaction.command.model.InboundCitizenStatusData;
import it.gov.pagopa.bpd.winning_transaction.command.model.ProcessCitizenUpdateEventCommandModel;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.CitizenStatusData;
import it.gov.pagopa.bpd.winning_transaction.mapper.CitizenStatusDataMapper;
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

import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;

public class ProcessCitizenUpdateEventCommandImplTest extends BaseTest {

    @Mock
    WinningTransactionService winningTransactionService;

    @Mock
    CitizenStatusDataService citizenStatusUpdateService;

    @Spy
    CitizenStatusDataMapper citizenStatusDataMapperSpy;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void initTest() {
        Mockito.reset(winningTransactionService, citizenStatusUpdateService, citizenStatusDataMapperSpy);
    }

    @Test
    public void TestExecute_OK() {

        InboundCitizenStatusData inboundCitizenStatusData = getRequestModel();
        CitizenStatusData citizenStatusData = getSavedModel();

        BDDMockito.doReturn(true).when(citizenStatusUpdateService).checkAndCreate(
                Mockito.eq(citizenStatusData));
        ProcessCitizenUpdateEventCommandImpl saveTransactionCommand = new ProcessCitizenUpdateEventCommandImpl(
                ProcessCitizenUpdateEventCommandModel.builder().payload(inboundCitizenStatusData).build(),
                citizenStatusUpdateService,
                winningTransactionService,
                citizenStatusDataMapperSpy);
        Boolean executed = saveTransactionCommand.doExecute();
        Mockito.verify(citizenStatusDataMapperSpy).map(Mockito.eq(inboundCitizenStatusData));
        Mockito.verify(citizenStatusUpdateService).checkAndCreate(Mockito.eq(citizenStatusData));
        Assert.assertTrue(executed);

    }

    @Test
    public void TestExecute_OK_Outdated() {

        InboundCitizenStatusData inboundCitizenStatusData = getRequestModel();
        CitizenStatusData citizenStatusData = getSavedModel();

        BDDMockito.doReturn(false).when(citizenStatusUpdateService).checkAndCreate(
                Mockito.eq(citizenStatusData));
        ProcessCitizenUpdateEventCommandImpl saveTransactionCommand = new ProcessCitizenUpdateEventCommandImpl(
                ProcessCitizenUpdateEventCommandModel.builder().payload(inboundCitizenStatusData).build(),
                citizenStatusUpdateService,
                winningTransactionService,
                citizenStatusDataMapperSpy);
        Boolean executed = saveTransactionCommand.doExecute();
        Mockito.verify(citizenStatusDataMapperSpy).map(Mockito.eq(inboundCitizenStatusData));
        Mockito.verify(citizenStatusUpdateService).checkAndCreate(Mockito.eq(citizenStatusData));
        Assert.assertTrue(executed);

    }


    @Test
    public void TestExecute_KO() {

        InboundCitizenStatusData inboundCitizenStatusData = getRequestModel();
        inboundCitizenStatusData.setEnabled(null);

        ProcessCitizenUpdateEventCommandImpl saveTransactionCommand = new ProcessCitizenUpdateEventCommandImpl(
                ProcessCitizenUpdateEventCommandModel.builder().payload(inboundCitizenStatusData).build(),
                citizenStatusUpdateService,
                winningTransactionService,
                citizenStatusDataMapperSpy);
        exceptionRule.expect(ConstraintViolationException.class);
        saveTransactionCommand.doExecute();
        Mockito.verifyZeroInteractions(winningTransactionService);

    }

    protected InboundCitizenStatusData getRequestModel() {
        return InboundCitizenStatusData.builder()
                .updateDateTime(OffsetDateTime.parse("2020-04-10T16:22:45.304Z"))
                .enabled(false)
                .fiscalCode("fiscalCode")
                .build();
    }

    protected CitizenStatusData getSavedModel() {
        return CitizenStatusData.builder()
                .enabled(false)
                .updateTimestamp(OffsetDateTime.parse("2020-04-09T16:22:45.304Z"))
                .fiscalCode("fiscalCode")
                .build();
    }

}