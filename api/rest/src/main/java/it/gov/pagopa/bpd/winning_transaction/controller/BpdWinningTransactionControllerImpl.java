package it.gov.pagopa.bpd.winning_transaction.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.winning_transaction.assembler.WinningTransactionResourceAssembler;
import it.gov.pagopa.bpd.winning_transaction.factory.ModelFactory;
import it.gov.pagopa.bpd.winning_transaction.model.dto.WinningTransactionDTO;
import it.gov.pagopa.bpd.winning_transaction.model.entity.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.model.resource.WinningTransactionResource;
import it.gov.pagopa.bpd.winning_transaction.service.WinningTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
class BpdWinningTransactionControllerImpl extends StatelessController implements BpdWinningTransactionController {

    private final ModelFactory<WinningTransactionDTO, WinningTransaction> winningTransactionFactory;
    private final WinningTransactionResourceAssembler winningTransactionResourceAssembler;
    private final WinningTransactionService winningTransactionService;

    @Autowired
    public BpdWinningTransactionControllerImpl(
            ModelFactory<WinningTransactionDTO, WinningTransaction> winningTransactionFactory,
            WinningTransactionResourceAssembler winningTransactionResourceAssembler,
            WinningTransactionService winningTransactionService){
        this.winningTransactionFactory = winningTransactionFactory;
        this.winningTransactionResourceAssembler = winningTransactionResourceAssembler;
        this.winningTransactionService = winningTransactionService;
    }

    @Override
    public WinningTransactionResource createWinningTransaction(
            @Valid @RequestBody WinningTransactionDTO dto) {
        if (log.isDebugEnabled()) {
            log.debug("### BpdWinningTransactionControllerImpl - createWinningTransaction ###");
            log.debug(dto.toString());
        }
        try {
            WinningTransaction winningTransaction = winningTransactionFactory.createModel(dto);
            winningTransaction = winningTransactionService.create(winningTransaction);
            return winningTransactionResourceAssembler.toResource(winningTransaction);
        } catch (EntityExistsException e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @Override
    public List<WinningTransactionResource> findWinningTransactions(
            @Valid @NotBlank String hpan, @Valid @NotNull Long awardPeriodId) {
        if (log.isDebugEnabled()) {
            log.debug("### BpdWinningTransactionControllerImpl - findWinningTransactions ###");
            log.debug("hpan:" + hpan);
            log.debug("awardPeriodId:" + awardPeriodId);
        }
        List<WinningTransaction> winningTransactions = winningTransactionService
                .getWinningTransactions(hpan, awardPeriodId);
        return winningTransactions.stream()
                .map(winningTransactionResourceAssembler::toResource)
                .collect(Collectors.toList());
    }

    @Override
    public Long getTotalScore(
            @Valid @NotBlank String hpan, @Valid @NotNull Long awardPeriodId) {
        if (log.isDebugEnabled()) {
            log.debug("### BpdWinningTransactionControllerImpl - getTotalScore ###");
            log.debug("hpan:" + hpan);
            log.debug("awardPeriodId:" + awardPeriodId);
        }
        Long totalScore = winningTransactionService.getTotalScore(hpan, awardPeriodId);
        if (totalScore == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "hpan non trovato");
        }
        return totalScore;

    }
}
