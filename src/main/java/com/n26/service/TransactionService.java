package com.n26.service;

import com.n26.exception.OlderTransactionException;
import com.n26.exception.UnParsableTransactionException;
import com.n26.model.Transaction;

public interface TransactionService {
	
	void create(Transaction transaction) throws OlderTransactionException, UnParsableTransactionException;
	void delete();
}
