package com.db.awmd.challenge.service;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transaction;
import com.db.awmd.challenge.exception.InsufficientAmountException;
import com.db.awmd.challenge.repository.TransactionRepository;

import lombok.Getter;

@Service
public class TransferService {

	@Getter
	private TransactionRepository transactionsRepository;

	@Autowired
	private AccountsService accountsService;

	@Autowired
	public TransferService(TransactionRepository transactionsRepository) {
		this.transactionsRepository = transactionsRepository;
	}

	public void transfer(String fromAccountId, String toAccountId, BigDecimal amount) {

		// aquire locks on from and to account always in same order to avoid
		// deadlock
		Object lock1 = new Object();
		Object lock2 = new Object();
		if (fromAccountId.compareTo(toAccountId) == 1) {
			lock1 = fromAccountId;
			lock2 = toAccountId;
		} else {
			lock2 = fromAccountId;
			lock1 = toAccountId;
		}

		synchronized (lock1) {
			synchronized (lock2) {
				// get current balances for from and to account
				Account fromAccount = accountsService.getAccount(fromAccountId);
				Account toAccount = accountsService.getAccount(toAccountId);

				// check from account balance. should be positive and greater
				// than amount needs to be transfer
				if (fromAccount.getBalance().compareTo(BigDecimal.ONE) == 1
						&& fromAccount.getBalance().compareTo(amount) == 1) {
					// update from account balance and log transaction
					fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
					transactionsRepository.createTransaction(new Transaction("", fromAccountId, amount.negate(),
							new Timestamp(System.currentTimeMillis())));

					// update to account balance and log transaction
					toAccount.setBalance(toAccount.getBalance().add(amount));
					transactionsRepository.createTransaction(
							new Transaction("", toAccountId, amount, new Timestamp(System.currentTimeMillis())));
				} else {
					throw new InsufficientAmountException(fromAccountId +" Account do not have sufficient balance to transfer.");
				}

			}
		}
	}

}
