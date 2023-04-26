package co.com.quisofka.quizzes.infrastructure.entryPoints;

import co.com.quisofka.quizzes.domain.model.email.Email;
import co.com.quisofka.quizzes.domain.model.student.Student;
import co.com.quisofka.quizzes.domain.usecase.email.sendquizbyemail.SendQuizCodeByEmailUseCase;
import co.com.quisofka.quizzes.domain.usecase.student.updatestudent.UpdateStudentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Collections;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRestEmail {

    @Bean
    @RouterOperation(path = "/quisofka/quizzes/emails/generatedCode/{quizCode}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = SendQuizCodeByEmailUseCase.class, method = RequestMethod.POST,
            beanMethod = "apply",
            operation = @Operation(operationId = "sentQuizCodeByEmail", tags = "Email usecases",
                    parameters = {@Parameter(name = "quizCode", description = "quiz code", required= true, in = ParameterIn.PATH),
                            @Parameter(name = "email", in = ParameterIn.PATH,
                                    schema = @Schema(implementation = Email.class))},
                    responses = {
                            @ApiResponse(responseCode = "201", description = "Success",
                                    content = @Content(schema = @Schema(implementation = Email.class))),
                            @ApiResponse(responseCode = "406", description = "Not acceptable, Try again")
                    },
                    requestBody = @RequestBody(required = true, description = "Send a email following the schema",
                            content = @Content(schema = @Schema(implementation = Email.class)))
            ))
    public RouterFunction<ServerResponse> sendCodeByEmail (SendQuizCodeByEmailUseCase codeByEmailUseCase){
        return route(POST("/quisofka/quizzes/emails/generatedCode/{quizCode}").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(Email.class)
                        .flatMap(email -> codeByEmailUseCase.apply(email,request.pathVariable("quizCode"))
                                .thenReturn(ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(Collections.singletonMap("message","email sent")))
                                .flatMap(serverResponseMono -> serverResponseMono)
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                        .bodyValue(throwable.getMessage()))
                        )
        );
    }
}
