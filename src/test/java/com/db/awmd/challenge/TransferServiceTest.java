package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.InsufficientAmountException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.TransferService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransferServiceTest {

	@Autowired
	private AccountsService accountsService;

	@Autowired
	private TransferService transferService;

	@Test
	public void transferBetween1To2() throws Exception {
		Account account1 = new Account("ID-1");
		account1.setBalance(new BigDecimal(1000));
		this.accountsService.createAccount(account1);

		assertThat(this.accountsService.getAccount("ID-1")).isEqualTo(account1);

		Account account2 = new Account("ID-2");
		account2.setBalance(new BigDecimal(1000));
		this.accountsService.createAccount(account2);

		assertThat(this.accountsService.getAccount("ID-2")).isEqualTo(account2);

		this.transferService.transfer("ID-1", "ID-2", BigDecimal.valueOf(100));

		assertThat(this.accountsService.getAccount("ID-1").getBalance()).isEqualByComparingTo(BigDecimal.valueOf(900));

		assertThat(this.accountsService.getAccount("ID-2").getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1100));
	}

	@Test
	public void transferBetween3To4_failsOnInsufficientBalance() throws Exception {
		Account account1 = new Account("ID-3");
		account1.setBalance(new BigDecimal(1000));
		this.accountsService.createAccount(account1);

		assertThat(this.accountsService.getAccount("ID-3")).isEqualTo(account1);

		Account account2 = new Account("ID-4");
		account2.setBalance(new BigDecimal(1000));
		this.accountsService.createAccount(account2);

		assertThat(this.accountsService.getAccount("ID-4")).isEqualTo(account2);

		try {
			this.transferService.transfer("ID-3", "ID-4", BigDecimal.valueOf(1100));
			fail("Should have failed when from account do not have sufficient balance");
		} catch (InsufficientAmountException ex) {
			assertThat(ex.getMessage()).isEqualTo(account1.getAccountId()+" Account do not have sufficient balance to transfer.");
		}

	}
}
