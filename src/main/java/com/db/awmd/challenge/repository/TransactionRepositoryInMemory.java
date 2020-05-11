package com.db.awmd.challenge.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.db.awmd.challenge.domain.Transaction;

@Repository
public class TransactionRepositoryInMemory implements TransactionRepository {

	private final Map<String, List<Transaction>> accountTransactions = new HashMap<>();

	@Override
	public void createTransaction(Transaction transaction) {
		List<Transaction> transactions = accountTransactions.get(transaction.getAccountId());
		if (transactions == null) {
			transactions = new ArrayList<>();
			transactions.add(transaction);
			accountTransactions.put(transaction.getAccountId(), transactions);
		}
	}

	@Override
	public void clearTransactions() {
		accountTransactions.clear();
	}

	@Override
	public List<Transaction> getTransactions(String accountId) {
		return accountTransactions.get(accountId);
	}

}
