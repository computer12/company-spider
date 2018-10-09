package com.ssta.company.spider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class CompanySpiderApplication {

    @Autowired
    JdbcTemplate template;

	public static void main(String[] args) {
		SpringApplication.run(CompanySpiderApplication.class, args);
	}

}
