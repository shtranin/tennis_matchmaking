package com.kefx.tennis_matchmaking;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories
//@EnableMongoRepositories(basePackageClasses= RegistrationStatementRepo.class)
public class TennisMatchmakingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TennisMatchmakingApplication.class, args);

	}

}
