package it.gov.pagopa.bpd.winning_transaction.command;

import eu.sia.meda.core.command.BaseCommand;
import it.gov.pagopa.bpd.winning_transaction.command.model.InboundCitizenStatusData;
import it.gov.pagopa.bpd.winning_transaction.command.model.ProcessCitizenUpdateEventCommandModel;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.CitizenStatusData;
import it.gov.pagopa.bpd.winning_transaction.mapper.CitizenStatusDataMapper;
import it.gov.pagopa.bpd.winning_transaction.service.CitizenStatusDataService;
import it.gov.pagopa.bpd.winning_transaction.service.WinningTransactionService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

/**
 * Class extending {@link BaseCommand<Boolean>}, implementation of {@link ProcessCitizenUpdateEventCommand}.
 * The command defines the execution of the whole {@link CitizenStatusData} save processing, aggregating and hiding the
 * services used to call on the services and commands involved in the process
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class ProcessCitizenUpdateEventCommandImpl extends BaseCommand<Boolean> implements ProcessCitizenUpdateEventCommand {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    private final ProcessCitizenUpdateEventCommandModel processCitizenUpdateEventCommandModel;
    private CitizenStatusDataMapper citizenStatusDataMapper;
    private WinningTransactionService winningTransactionService;
    private CitizenStatusDataService statusDataService;
    private LocalDate processDateTime;

    public ProcessCitizenUpdateEventCommandImpl(ProcessCitizenUpdateEventCommandModel processCitizenUpdateEventCommandModel) {
        this.processCitizenUpdateEventCommandModel = processCitizenUpdateEventCommandModel;
        this.processDateTime = LocalDate.now();
    }

    public ProcessCitizenUpdateEventCommandImpl(
                                      ProcessCitizenUpdateEventCommandModel processCitizenUpdateEventCommandModel,
                                      CitizenStatusDataService citizenStatusDataService,
                                      WinningTransactionService winningTransactionService,
                                      CitizenStatusDataMapper citizenStatusDataMapper) {
        this.processCitizenUpdateEventCommandModel = processCitizenUpdateEventCommandModel;
        this.statusDataService = citizenStatusDataService;
        this.processDateTime = LocalDate.now();
        this.winningTransactionService = winningTransactionService;
        this.citizenStatusDataMapper = citizenStatusDataMapper;
    }


    @SneakyThrows
    @Override
    public Boolean doExecute() {

        InboundCitizenStatusData inboundCitizenStatusData = processCitizenUpdateEventCommandModel.getPayload();

        try {

            validateRequest(inboundCitizenStatusData);

            CitizenStatusData citizenStatusData = citizenStatusDataMapper.map(inboundCitizenStatusData);
            boolean statusUpdated = statusDataService.checkAndCreate(citizenStatusData);

            if (statusUpdated && !inboundCitizenStatusData.getEnabled()) {
                winningTransactionService.deleteByFiscalCodeIfNotUpdated(
                        inboundCitizenStatusData.getFiscalCode(), inboundCitizenStatusData.getUpdateDateTime());
            }

            return true;

        } catch (Exception e) {

            if (inboundCitizenStatusData != null) {

                if (logger.isErrorEnabled()) {
                    logger.error("Error occured during processing");
                    logger.error(e.getMessage(), e);
                }

            }

            throw e;

        }

    }

    /**
     * Method to process a validation check for the parsed Transaction request
     *
     * @param citizenStatusData instance of CitizenStatusData, parsed from the inbound byte[] payload
     * @throws ConstraintViolationException
     */
    private void validateRequest(InboundCitizenStatusData citizenStatusData) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(citizenStatusData);
        if (constraintViolations.size() > 0) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    @Autowired
    public void setCitizenStatusDataMapper(CitizenStatusDataMapper citizenStatusDataMapper) {
        this.citizenStatusDataMapper = citizenStatusDataMapper;
    }

    @Autowired
    public void setWinningTransactionService(WinningTransactionService winningTransactionService) {
        this.winningTransactionService = winningTransactionService;
    }

    @Autowired
    public void setCitizenStatusDataService(CitizenStatusDataService citizenStatusDataService) {
        this.statusDataService = citizenStatusDataService;
    }

}
