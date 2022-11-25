package com.tarento.analytics;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.tarento.analytics.constant.Constants;


@SpringBootApplication
public class AnalyticApp {
	 public static void main( String[] args ) {
	        SpringApplication.run(AnalyticApp.class, args);
	    }

	    @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }
	    
	    @Bean
	    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
	        return new JdbcTemplate(dataSource);
	    }
	    
	    @SuppressWarnings("deprecation")
		@Bean
	    public WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurerAdapter() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	                registry.addMapping("/**").allowedMethods(Constants.GET, Constants.POST,Constants.PUT, Constants.DELETE, Constants.OPTIONS).allowedOrigins("*")
	                        .allowedHeaders("*");
	            }
	        };
	    }
}
