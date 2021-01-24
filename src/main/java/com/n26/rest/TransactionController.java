package com.n26.rest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.n26.exception.OlderTransactionException;
import com.n26.exception.UnParsableTransactionException;
import com.n26.model.Transaction;
import com.n26.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ajay Singh Pundir
 * REST endpoints for performing transaction related operations.
 */
@Api(value = "Transactions API")
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @ApiOperation(value = "Create transactions")
    @PostMapping("/transactions")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void create(@RequestBody Transaction tx) throws UnParsableTransactionException,
            OlderTransactionException {
        transactionService.createTransaction(tx);

    }

    @ApiOperation(value = "Delete transactions")
    @DeleteMapping("/transactions")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void remove() {
        transactionService.removeTransactions();
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Void> handleJacksonMappingError(InvalidFormatException ex) {
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
