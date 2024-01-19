package iainpalangkarayarepository.web;

import iainpalangkarayarepository.web.model.RegisterUserRequest;
import iainpalangkarayarepository.web.model.Role;
import iainpalangkarayarepository.web.service.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IainPalangkarayaRepositoryWebApplication {

	public static void main(String[] args)	 {
		SpringApplication.run(IainPalangkarayaRepositoryWebApplication.class, args);
	}

}
