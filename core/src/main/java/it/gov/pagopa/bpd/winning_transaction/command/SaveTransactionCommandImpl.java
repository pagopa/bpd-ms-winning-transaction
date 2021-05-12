package it.gov.pagopa.bpd.winning_transaction.command;

import eu.sia.meda.core.command.BaseCommand;
import it.gov.pagopa.bpd.winning_transaction.command.model.SaveTransactionCommandModel;
import it.gov.pagopa.bpd.winning_transaction.command.model.Transaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.mapper.TransactionMapper;
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
 * Class extending {@link BaseCommand<Boolean>}, implementation of {@link SaveTransactionCommand}.
 * The command defines the execution of the whole {@link Transaction} save processing, aggregating and hiding the
 * services used to call on the services and commands involved in the process
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
class SaveTransactionCommandImpl extends BaseCommand<Boolean> implements SaveTransactionCommand {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    private final SaveTransactionCommandModel saveTransactionCommandModel;
    private TransactionMapper transactionMapper;
    private WinningTransactionService winningTransactionService;
    private final LocalDate processDateTime;

    public SaveTransactionCommandImpl(SaveTransactionCommandModel saveTransactionCommandModel) {
        this.saveTransactionCommandModel = saveTransactionCommandModel;
        this.processDateTime = LocalDate.now();
    }

    public SaveTransactionCommandImpl(SaveTransactionCommandModel saveTransactionCommandModel,
                                      WinningTransactionService winningTransactionService,
                                      TransactionMapper transactionMapper) {
        this.saveTransactionCommandModel = saveTransactionCommandModel;
        this.processDateTime = LocalDate.now();
        this.winningTransactionService = winningTransactionService;
        this.transactionMapper = transactionMapper;
    }


    @SneakyThrows
    @Override
    public Boolean doExecute() {

        Transaction transaction = saveTransactionCommandModel.getPayload();

        try {

            validateRequest(transaction);

            WinningTransaction winningTransaction = transactionMapper.map(transaction);

            if (winningTransaction.getValid() == null) {
                winningTransactionService.create(winningTransaction);

                return true;
            } else if (!winningTransaction.getValid() && !winningTransaction.getOperationType().equals("01")) {
                winningTransaction.setElabRanking(true);
            }

            winningTransactionService.create(winningTransaction);

            return true;

        } catch (Exception e) {

            if (transaction != null) {

                if (logger.isErrorEnabled()) {
                    logger.error("Error occured during processing for transaction: " +
                            transaction.getIdTrxAcquirer() + ", " +
                            transaction.getAcquirerCode() + ", " +
                            transaction.getTrxDate());
                    logger.error(e.getMessage(), e);
                }

            }

            throw e;

        }

    }

    /**
     * Method to process a validation check for the parsed Transaction request
     *
     * @param request instance of Transaction, parsed from the inbound byte[] payload
     * @throws ConstraintViolationException
     */
    private void validateRequest(Transaction request) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
        if (constraintViolations.size() > 0) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    @Autowired
    public void setTransactionMapper(TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
    }

    @Autowired
    public void setWinningTransactionService(WinningTransactionService winningTransactionService) {
        this.winningTransactionService = winningTransactionService;
    }

}
