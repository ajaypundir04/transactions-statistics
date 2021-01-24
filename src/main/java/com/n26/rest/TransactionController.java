package com.n26.rest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.n26.exception.OlderTransactionException;
import com.n26.exception.UnParsableTransactionException;
import com.n26.model.Transaction;
import com.n26.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController( TransactionService transactionService ) {
        this.transactionService = transactionService;
    }

    @PostMapping( "/transactions" )
    public ResponseEntity< Void > create( @RequestBody Transaction tx ) throws UnParsableTransactionException,
            OlderTransactionException {
        transactionService.createTransaction( tx );
        return new ResponseEntity<>( HttpStatus.CREATED );
    }

    @DeleteMapping( "/transactions" )
    public ResponseEntity< Void > remove() {
        transactionService.removeTransactions();
        return new ResponseEntity<>( HttpStatus.NO_CONTENT );
    }

    @ExceptionHandler( InvalidFormatException.class )
    public ResponseEntity< Void > handleJacksonMappingError( InvalidFormatException ex ) {
        return new ResponseEntity<>( HttpStatus.UNPROCESSABLE_ENTITY );
    }
}
