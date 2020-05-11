package com.db.awmd.challenge.repository;

import java.util.List;

import com.db.awmd.challenge.domain.Transaction;

public interface TransactionRepository {

  void createTransaction(Transaction Transaction);

  List<Transaction> getTransactions(String accountId);

  void clearTransactions();
}
