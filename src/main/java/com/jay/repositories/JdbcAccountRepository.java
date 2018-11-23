package com.jay.repositories;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jay.entities.Account;


@Repository
@Profile("test")
public class JdbcAccountRepository implements AccountRepository{
	
	private JdbcTemplate jdbcTemplate;
	private static long nextId = 4;
	
	@Autowired
	public 	JdbcAccountRepository(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
	}
	
	@Override
	public List<Account> getAccounts() {
		String sql = "select * from account";
		return jdbcTemplate.query(sql, new AccountMapper());
	}
	@Override
	public Account getAccount(Long id) {
		String sql = "select * from account where id=?";
		return jdbcTemplate.queryForObject(sql, new AccountMapper(), id);
	}
	@Override
	public int getNumberOfAccounts() {
		String sql = "select count(*) from account";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}
	@Override
	public Long createAccount(BigDecimal initialBalance) {
		String sql = "insert into account(id, balance) values(?,?)";
		Long id = nextId++;
		int uc = jdbcTemplate.update(sql, id, initialBalance );
		return id;
	}
	@Override
	public int deleteAccount(Long id) {
		String sql = "delete from account where id=?";
		return jdbcTemplate.update(sql, id);
	}
	@Override
	public void updateAccount(Account account) {
		String sql = "update account set balance=? where id=?";
		jdbcTemplate.update(sql, account.getBalance(), account.getId());
	}
	
	private class AccountMapper implements RowMapper<Account>{

		@Override
		public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Account(rs.getLong("id"),
					rs.getBigDecimal("balance"));
		}
		
	}
	

}
