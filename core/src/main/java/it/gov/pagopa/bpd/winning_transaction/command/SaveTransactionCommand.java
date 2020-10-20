package it.gov.pagopa.bpd.winning_transaction.command;

import eu.sia.meda.core.command.Command;

/**
 * Interface extending {@link Command<Boolean>}, defines the command,
 * to be used for inbound {@link it.gov.pagopa.bpd.winning_transaction.command.model.Transaction} to be processed
 *
 * @see SaveTransactionCommandImpl
 */

public interface SaveTransactionCommand extends Command<Boolean> {
}
