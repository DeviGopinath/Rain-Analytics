package com.tarento.analytics.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan
@EnableTransactionManagement
public class DAODataServiceManagerConfiguration {

	@Bean
	public DataSource getDataSource(AppConfiguration appConfiguration) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(appConfiguration.getSpringDataSourceDriverClassName());
		dataSource.setUrl(appConfiguration.getSpringDataSourceUrl());
		dataSource.setUsername(appConfiguration.getSpringDataSourceUsername());
		dataSource.setPassword(appConfiguration.getSpringDataSourcePassword());
		return dataSource;
	}

}