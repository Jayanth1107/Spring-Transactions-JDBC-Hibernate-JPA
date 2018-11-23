package com.jay.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.jay")
@PropertySource("classpath:prod.properties")
@EnableTransactionManagement
public class AppConfig {
	
	@Autowired
	private Environment env;
	
	@Bean(name="dataSource", destroyMethod="shutdown")
	@Profile("test")
	public DataSource datasourceForTest() {
		return new EmbeddedDatabaseBuilder()
				.generateUniqueName(true)
				.setType(EmbeddedDatabaseType.H2)
				.setScriptEncoding("UTF-8")
				.ignoreFailedDrops(true)
				.addScript("schema.sql")
				.addScripts("data.sql")
				.build();
	}
	
	@Bean(name="transactionManager")
	@Profile("test")
	public PlatformTransactionManager transactionManagerForTest() {
		return new DataSourceTransactionManager(datasourceForTest());
	}
	
	@Bean(name="dataSource")
	@Profile("prod")
	public DataSource datasourceForProd() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("db.driver"));
		dataSource.setUrl("db.url");
		dataSource.setUsername(env.getProperty("db.username"));
		dataSource.setPassword(env.getProperty("db.password"));
		
		return dataSource;
	}
	
	@Bean(name="transactionManager")
	@Profile("prod")
	public PlatformTransactionManager transactionManagerForProd() {
		return new DataSourceTransactionManager(datasourceForProd());
	}

}
