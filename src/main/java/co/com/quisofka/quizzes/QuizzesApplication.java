package co.com.quisofka.quizzes;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Quisofka",
				version = "1.0.0",
				description = "Documentation created for Quisofka main API in charge of providing quizzes"
		),
		servers = {
		@Server(url = "http://localhost:8080/"),
		@Server(url = "https://quisofka-mvn-b-production.up.railway.app/")})
public class QuizzesApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizzesApplication.class, args);
	}

}
