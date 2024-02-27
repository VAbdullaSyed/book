package com.neolore.card;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = {"com.neolore.card.data"}, enableDefaultTransactions = false)
//@EntityScan("com.neolore.card.data")
public class BookApplication {

	public static void main(String[] args) {
		System.out.println("Hello");
		SpringApplication.run(BookApplication.class, args);
	}

}
