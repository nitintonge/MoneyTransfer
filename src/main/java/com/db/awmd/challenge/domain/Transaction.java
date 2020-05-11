package com.db.awmd.challenge.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
public class Transaction {

  @NotNull
  @NotEmpty
  private final String transactionId;
  
  @NotNull
  @NotEmpty
  private final String accountId;
  
  @NotNull
  @Min(value = 0, message = "Amount must be positive.")
  private BigDecimal transactionAmount;
  
  private Timestamp transactionDateTime;

  public Transaction(String transactionId, String accountId, BigDecimal transactionAmount, Timestamp transactionDateTime) {
    this.transactionId = transactionId;
    this.accountId = accountId;
    this.transactionAmount = transactionAmount;
    this.transactionDateTime = transactionDateTime;
  }
}
