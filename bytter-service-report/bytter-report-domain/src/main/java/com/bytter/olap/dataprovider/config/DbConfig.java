package com.bytter.olap.dataprovider.config;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfig {
	
		@Value("${jdbc.url}")
		public String url;
		
		@Value("${jdbc.username}")
		public String username;
		
		@Value("${jdbc.password}")
		public String password;
		
		@Value("${jdbc.driverClass}")
		public String driverClass;
		
	 	@Bean("h2DataSource")
	    public DataSource dataSource() {
	        BasicDataSource dataSource = new BasicDataSource();
	        dataSource.setDriverClassName(driverClass);
	        dataSource.setUrl(url);
	        dataSource.setUsername(username);
	        dataSource.setPassword(password);
//	        dataSource.setDriverClassName("org.h2.Driver");
//	        dataSource.setUrl(url);
//	        dataSource.setUsername("sa");
//	        dataSource.setPassword("");
	        dataSource.setMaxTotal(50);
	        return dataSource;
	    }
}
