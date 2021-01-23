package com.n26.service;

import com.n26.aspect.StatisticsGenerator;
import com.n26.exception.OlderTransactionException;
import com.n26.exception.UnParsableTransactionException;
import com.n26.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@StatisticsGenerator
public class TransactionServiceImpl implements TransactionService{

	@Value("${transactionService.stale-after-seconds:60}") private long staleAfterSeconds;

	public void removeTransactions() {
		// delete all tx
	}

	protected void createTransactionAtInstant(Transaction tx, Instant instant)
			throws OlderTransactionException, UnParsableTransactionException {
		
		if (tx.getTimestamp() - instant.toEpochMilli() > 0)
			throw new UnParsableTransactionException();
		
		if (tx.getTimestamp() - instant.minusSeconds(staleAfterSeconds).toEpochMilli() < 0)
			throw new OlderTransactionException();
		
		// persist tx
	}
	
	public void createTransaction(Transaction tx) throws OlderTransactionException, UnParsableTransactionException {
		createTransactionAtInstant(tx, Instant.now());
	}
	
	public long getStaleAfterSeconds() {
		return staleAfterSeconds;
	}
	
}
