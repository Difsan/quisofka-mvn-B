package co.com.quisofka.quizzes;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Apprentice Radar API",
				version = "1.0.0",
				description = "API created for the agile challenge at SofkaU Bilingual Training League"
		),      servers = {
		@Server(url = "https://quisofka-mvn-b-production.up.railway.app")
		//@Server(url = "url del servidor de produccion")
})
public class QuizzesApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizzesApplication.class, args);
	}

}
