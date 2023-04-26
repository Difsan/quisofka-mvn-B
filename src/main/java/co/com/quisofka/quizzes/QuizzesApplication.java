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
				description = "API created to creates quizzes for student at SofkaU Bilingual"
		))
public class QuizzesApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizzesApplication.class, args);
	}

}
