package it.gov.pagopa.bpd.winning_transaction.controller;

import io.swagger.annotations.Api;
import it.gov.pagopa.bpd.winning_transaction.model.dto.WinningTransactionDTO;
import it.gov.pagopa.bpd.winning_transaction.model.resource.WinningTransactionResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Controller to expose MicroService
 */
@Api(tags = "Bonus Pagamenti Digitali winning-transaction Controller")
@RequestMapping("/bpd/winning-transactions")
public interface BpdWinningTransactionController {


    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    WinningTransactionResource createWinningTransaction(
            @Valid @RequestBody WinningTransactionDTO winningTransactionDTO);

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<WinningTransactionResource> findWinningTransactions(
            @Valid @NotBlank @RequestParam String hpan,
            @Valid @NotNull @RequestParam Long awardPeriodId);

    @GetMapping(value = "/total-score", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Long getTotalScore(
            @Valid @NotBlank @RequestParam String hpan,
            @Valid @NotNull @RequestParam Long awardPeriodId) throws ClassNotFoundException;
}
