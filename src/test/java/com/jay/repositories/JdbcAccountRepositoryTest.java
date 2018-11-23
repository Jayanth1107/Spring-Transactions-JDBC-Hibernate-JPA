package com.jay.repositories;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.hamcrest.core.IsNot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jay.config.AppConfig;
import com.jay.entities.Account;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
@Transactional
@ActiveProfiles("test")
public class JdbcAccountRepositoryTest {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Test
	public void testGetAccounts() throws Exception{
		List<Account> accounts = accountRepository.getAccounts();
		assertThat(accounts.size(), is(3));
	}

	@Test
	public void testGetAccount() throws Exception{
		Account account = accountRepository.getAccount(1L);
		assertThat(account.getId(), is(1L));
		assertThat(new BigDecimal("100.0"), is(closeTo(account.getBalance(), new BigDecimal("0.01"))));
	}
	
	@Test
	public void testNumberOfAccounts() throws Exception{
		assertThat(accountRepository.getNumberOfAccounts(),is(3));
	}
	
	@Test
	public void testCreateAccount() throws Exception {
		Long id = accountRepository.createAccount(new BigDecimal("253.0"));
		assertThat(id, is(notNullValue()));
		
		Account account = accountRepository.getAccount(id);
		assertThat(account.getId(), is(id));
		assertThat(account.getBalance(),is(closeTo(new BigDecimal("253.00"), new BigDecimal("0.01"))));
	}
	
	@Test
	public void testUpdateAccount() throws Exception {
		Account account = accountRepository.getAccount(1L);
		BigDecimal current = account.getBalance();
		BigDecimal amount = new BigDecimal("50.00");
		account.setBalance(current.add(amount));
		accountRepository.updateAccount(account);
		
		Account ac1 = accountRepository.getAccount(1L);
		assertThat(ac1.getBalance(), is(closeTo(current.add(amount), new BigDecimal("0.01"))));
		
	}
	
	@Test
	public void testDeleteAccount() throws Exception {
		for (Account account : accountRepository.getAccounts()) {
			accountRepository.deleteAccount(account.getId());
		}
		
		assertThat(accountRepository.getNumberOfAccounts(),is(0));
	}
}
