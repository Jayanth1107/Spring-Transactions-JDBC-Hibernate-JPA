package com.jay.services;

import java.math.BigDecimal;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jay.entities.Account;
import com.jay.repositories.AccountRepository;

@Service
@Transactional
@Profile("test")
public class AccountService {
	
	private AccountRepository accountRepository;
	
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public BigDecimal getBalance(Long id) {
		return accountRepository.getAccount(id).getBalance();
	}
	
	
	public BigDecimal deposit(Long id, BigDecimal amount) {
		Account account = accountRepository.getAccount(id);
		BigDecimal newBalance = account.getBalance().add(amount);
		account.setBalance(newBalance);
		accountRepository.updateAccount(account);
		return newBalance;
	}
	
	public BigDecimal withdraw(Long id, BigDecimal amount) {
		return deposit(id,amount.negate());
	}
	
	public void transfer(Long fromId, Long toId, BigDecimal amount) {
		withdraw(fromId, amount);
		deposit(toId, amount);
	}

}
