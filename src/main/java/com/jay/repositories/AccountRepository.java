package com.jay.repositories;

import java.math.BigDecimal;
import java.util.List;

import com.jay.entities.Account;

public interface AccountRepository {
	List<Account> getAccounts();
	
	Account getAccount(Long id);
	
	int getNumberOfAccounts();
	
	Long createAccount(BigDecimal initialBalance);
	
	int deleteAccount(Long id);
	
	void updateAccount(Account account);

}
