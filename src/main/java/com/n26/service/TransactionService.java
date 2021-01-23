package com.n26.service;

import com.n26.exception.OlderTransactionException;
import com.n26.exception.UnParsableTransactionException;
import com.n26.model.Transaction;

public interface TransactionService {
	
	void createTransaction(Transaction tx) throws OlderTransactionException, UnParsableTransactionException;
	void removeTransactions();
}
