package co.com.quisofka.quizzes.infrastructure.entryPoints;

import co.com.quisofka.quizzes.domain.model.quiz.Quiz;
import co.com.quisofka.quizzes.domain.model.student.Student;
import co.com.quisofka.quizzes.domain.usecase.quiz.StartQuiz.StartQuizUseCase;
import co.com.quisofka.quizzes.domain.usecase.quiz.createFirstLvlQuiz.CreateFirstLvlQuizUseCase;
import co.com.quisofka.quizzes.domain.usecase.quiz.createQuiz.CreateQuizUseCase;
import co.com.quisofka.quizzes.domain.usecase.quiz.createSecondLvlQuiz.CreateSecondLvlQuizUseCase;
import co.com.quisofka.quizzes.domain.usecase.quiz.createThirdLvlQuiz.CreateThirdvlQuizUseCase;
import co.com.quisofka.quizzes.domain.usecase.quiz.deleteAll.DeleteAllQuizzesUseCase;
import co.com.quisofka.quizzes.domain.usecase.quiz.deletebyid.DeleteQuizByIdUseCase;
import co.com.quisofka.quizzes.domain.usecase.quiz.getAllQuizzes.GetAllQuizzesUseCase;
import co.com.quisofka.quizzes.domain.usecase.quiz.getQuizById.GetQuizByIdUseCase;
import co.com.quisofka.quizzes.domain.usecase.quiz.submitquiz.SubmitQuizUseCase;
import co.com.quisofka.quizzes.domain.usecase.student.deletestudent.DeleteStudentUseCase;
import co.com.quisofka.quizzes.domain.usecase.student.getstudentbyid.GetStudentByIdUseCase;
import co.com.quisofka.quizzes.domain.usecase.student.updatestudent.UpdateStudentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
@RequiredArgsConstructor
public class RouterRestQuiz {

    @Bean
    public RouterFunction<ServerResponse> getAll(GetAllQuizzesUseCase getAllQuizzesUseCase){
        return route(GET("/quisofka/quizzes/quizzes"),
                request -> ServerResponse.status(200)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getAllQuizzesUseCase.get(), Quiz.class))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NO_CONTENT).bodyValue(throwable.getMessage())));
    }


    @Bean
    @RouterOperation(path = "/quisofka/quizzes/quizzes/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetQuizByIdUseCase.class,
            method = RequestMethod.GET,
            beanMethod = "apply",
            operation = @Operation(operationId = "getQuizById", tags = "Quiz usecases",
                    parameters = {@Parameter(name = "id", description = "quiz Id", required= true, in = ParameterIn.PATH)},
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success",
                                    content = @Content(schema = @Schema(implementation = Quiz.class))),
                            @ApiResponse(responseCode = "404", description = "Not Found")
                    }))
    public RouterFunction<ServerResponse> getQuizById(GetQuizByIdUseCase getQuizByIdUseCase){
        return route(GET("/quisofka/quizzes/quizzes/{id}"),
                request -> getQuizByIdUseCase.apply(request.pathVariable("id"))
                        .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NO_CONTENT.toString())))
                        .flatMap(quiz -> ServerResponse.status(200)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(quiz))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).bodyValue(throwable.getMessage())));
    }


    @Bean
    @RouterOperation(path = "/quisofka/quizzes/quizzes", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = CreateQuizUseCase.class, method = RequestMethod.POST,
            beanMethod = "apply",
            operation = @Operation(operationId = "createQuiz", tags = "Quiz usecases",
                    parameters = {@Parameter(name = "quiz", in = ParameterIn.PATH,
                            schema = @Schema(implementation = Quiz.class))},
                    responses = {
                            @ApiResponse(responseCode = "201", description = "Success",
                                    content = @Content(schema = @Schema(implementation = Quiz.class))),
                            @ApiResponse(responseCode = "406", description = "Not acceptable, Try again")
                    }
                    ,
                    requestBody = @RequestBody(required = true, description = "Save an Quiz following the schema",
                            content = @Content(schema = @Schema(implementation = Quiz.class)))
            ))
    public RouterFunction<ServerResponse> createQuiz(CreateQuizUseCase createQuizUseCase) {
        return route(POST("/quisofka/quizzes/quizzes").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(Quiz.class)
                        .flatMap(quiz -> createQuizUseCase.apply(quiz)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).bodyValue(throwable.getMessage()))));
    }

    @Bean
    public RouterFunction<ServerResponse> createFirstLvlQuiz(CreateFirstLvlQuizUseCase createFirstLvlQuizUseCase) {
        return route(POST("/quisofka/quizzes/quizzes/firstlvl").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(Quiz.class)
                        .flatMap(quiz -> createFirstLvlQuizUseCase.apply(quiz)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).bodyValue(throwable.getMessage()))));
    }

    @Bean
    public RouterFunction<ServerResponse> createFirstlvlQuiz(CreateFirstLvlQuizUseCase createFirstLvlQuizUseCase) {
        return route(POST("/quisofka/quizzes/quizzes/firstlvl").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(Quiz.class)
                        .flatMap(quiz -> createFirstLvlQuizUseCase.apply(quiz)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).bodyValue(throwable.getMessage()))));
    }

    @Bean
    public RouterFunction<ServerResponse> createSecondLvlQuiz(CreateSecondLvlQuizUseCase createSecondLvlQuizUseCase) {
        return route(POST("/quisofka/quizzes/quizzes/secondlvl").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(Quiz.class)
                        .flatMap(quiz -> createSecondLvlQuizUseCase.apply(quiz)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).bodyValue(throwable.getMessage()))));
    }

    @Bean
    public RouterFunction<ServerResponse> createThirdLvlQuiz(CreateThirdvlQuizUseCase createThirdvlQuizUseCase) {
        return route(POST("/quisofka/quizzes/quizzes/thirdlvl").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(Quiz.class)
                        .flatMap(quiz -> createThirdvlQuizUseCase.apply(quiz)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).bodyValue(throwable.getMessage()))));
    }


    @Bean
    @RouterOperation(path = "/quisofka/quizzes/quizzes/start/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = StartQuizUseCase.class,
            method = RequestMethod.PATCH,
            beanMethod = "apply",
            operation = @Operation(operationId = "startQuizById", tags = "Quiz usecases",
                    parameters = {@Parameter(name = "id", description = "quiz Id", required= true, in = ParameterIn.PATH)},
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success",
                                    content = @Content(schema = @Schema(implementation = Quiz.class))),
                            @ApiResponse(responseCode = "404", description = "Not Found")
                    }))
    public RouterFunction<ServerResponse> startQuiz(StartQuizUseCase startQuizUseCase){
        return route(PATCH("/quisofka/quizzes/quizzes/start/{id}"),
                request -> startQuizUseCase.apply(request.pathVariable("id"))
                        .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NO_CONTENT.toString())))
                        .flatMap(quiz -> ServerResponse.status(200)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(quiz))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).bodyValue(throwable.getMessage())));
    }

    @Bean
    @RouterOperation(path = "/quisofka/quizzes/quizzes/submit/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = SubmitQuizUseCase.class, method = RequestMethod.PATCH,
            beanMethod = "apply",
            operation = @Operation(operationId = "submitQuiz", tags = "Quiz usecases",
                    parameters = {@Parameter(name = "id", description = "quiz Id", required= true, in = ParameterIn.PATH),
                            @Parameter(name = "quiz", in = ParameterIn.PATH,
                                    schema = @Schema(implementation = Student.class))},
                    responses = {
                            @ApiResponse(responseCode = "201", description = "Success",
                                    content = @Content (schema = @Schema(implementation = Quiz.class))),
                            @ApiResponse(responseCode = "406", description = "Not acceptable, Try again")
                    },
                    requestBody = @RequestBody(required = true, description = "Submit a quiz following the schema",
                            content = @Content(schema = @Schema(implementation = Quiz.class)))
            ))
    public RouterFunction<ServerResponse> submitQuiz (SubmitQuizUseCase submitQuizUseCase){
        return route(PATCH("/quisofka/quizzes/quizzes/submit/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(Quiz.class)
                        .flatMap(quiz -> submitQuizUseCase.apply(request.pathVariable("id"),
                                        quiz)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                        .bodyValue(throwable.getMessage()))
                        )
        );
    }

    @Bean
    @RouterOperation(path = "/quisofka/quizzes/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = DeleteQuizByIdUseCase.class, method = RequestMethod.DELETE,
            beanMethod = "apply",
            operation = @Operation(operationId = "deleteQuizById", tags = "Quiz usecases",
                    parameters = {@Parameter(name = "id", description = "quiz Id", required= true, in = ParameterIn.PATH)},
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success",
                                    content = @Content (schema = @Schema(implementation = Quiz.class))),
                            @ApiResponse(responseCode = "404", description = "Not Found")
                    }))
    public RouterFunction<ServerResponse> deleteQuiz (DeleteQuizByIdUseCase deleteQuizByIdUseCase){
        return route(DELETE("/quisofka/quizzes/{id}"),
                request -> deleteQuizByIdUseCase.apply(request.pathVariable("id"))
                        .thenReturn(ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Collections.singletonMap("message", "Quiz deleted")))
                        .flatMap(serverResponseMono -> serverResponseMono)
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(throwable.getMessage()))
        );
    }



    @Bean
    public RouterFunction<ServerResponse> deleteAllQuizzes(DeleteAllQuizzesUseCase deleteAllQuizzesUseCase){
        return route(DELETE("/quisofka/quizzes/quizzes"),
                request -> deleteAllQuizzesUseCase.get()
                        .thenReturn(
                                ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(Collections.singletonMap("message", "All quizzes have been deleted")))
                        .flatMap(serverResponseMono -> serverResponseMono)
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NO_CONTENT).bodyValue(throwable.getMessage())));
    }

}
