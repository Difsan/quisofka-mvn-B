package co.com.quisofka.quizzes.infrastructure.entryPoints;

import co.com.quisofka.quizzes.domain.model.student.Student;
import co.com.quisofka.quizzes.domain.usecase.student.deletestudent.DeleteStudentUseCase;
import co.com.quisofka.quizzes.domain.usecase.student.getstudentbyemail.GetStudentByEmailUseCase;
import co.com.quisofka.quizzes.domain.usecase.student.getstudentbyid.GetStudentByIdUseCase;
import co.com.quisofka.quizzes.domain.usecase.student.savestudent.SaveStudentUseCase;
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
public class RouterRestStudent {

    @Bean
    @RouterOperation(path = "/quisofka/quizzes/students/{studentId}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetStudentByIdUseCase.class,
            method = RequestMethod.GET,
            beanMethod = "apply",
            operation = @Operation(operationId = "getStudentById", tags = "Student usecases",
                    parameters = {@Parameter(name = "studentId", description = "student Id", required= true, in = ParameterIn.PATH)},
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success",
                                    content = @Content(schema = @Schema(implementation = Student.class))),
                            @ApiResponse(responseCode = "404", description = "Not Found")
                    }))
    public RouterFunction<ServerResponse> getStudentById (GetStudentByIdUseCase getStudentByIdUseCase){
        return route(GET("/quisofka/quizzes/students/{studentId}"),
                request -> getStudentByIdUseCase.apply(request.pathVariable("studentId"))
                        .flatMap(student -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(student))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND)
                                .bodyValue(throwable.getMessage()))
        );
    }

    @Bean
    @RouterOperation(path = "/quisofka/quizzes/students/byEmail/{email}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetStudentByEmailUseCase.class,
            method = RequestMethod.GET,
            beanMethod = "apply",
            operation = @Operation(operationId = "getStudentByEmail", tags = "Student usecases",
                    parameters = {@Parameter(name = "email", description = "student by email", required= true, in = ParameterIn.PATH)},
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success",
                                    content = @Content (schema = @Schema(implementation = Student.class))),
                            @ApiResponse(responseCode = "404", description = "Not Found")
                    }))
    public RouterFunction<ServerResponse> getStudentByEmail (GetStudentByEmailUseCase getStudentByEmailUseCase){
        return route(GET("/quisofka/quizzes/students/byEmail/{email}"),
                request -> getStudentByEmailUseCase.apply(request.pathVariable("email"))
                        .flatMap(student -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(student))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(throwable.getMessage()))
        );
    }

    @Bean
    @RouterOperation(path = "/quisofka/quizzes/students", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = SaveStudentUseCase.class, method = RequestMethod.POST,
            beanMethod = "apply",
            operation = @Operation(operationId = "saveStudent", tags = "Student usecases",
                    parameters = {@Parameter(name = "student", in = ParameterIn.PATH,
                            schema = @Schema(implementation = Student.class))},
                    responses = {
                            @ApiResponse(responseCode = "201", description = "Success",
                                    content = @Content (schema = @Schema(implementation = Student.class))),
                            @ApiResponse(responseCode = "406", description = "Not acceptable, Try again")
                    }
                    ,
                    requestBody = @RequestBody(required = true, description = "Save an Student following the schema",
                            content = @Content(schema = @Schema(implementation = Student.class)))
            ))
    public RouterFunction<ServerResponse> saveStudent (SaveStudentUseCase saveStudentUseCase){
        return route(POST("/quisofka/quizzes/students").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(Student.class)
                        .flatMap(student -> saveStudentUseCase.apply(student)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                        .bodyValue(throwable.getMessage()))));
    }

    @Bean
    @RouterOperation(path = "/quisofka/quizzes/students/{studentId}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = UpdateStudentUseCase.class, method = RequestMethod.PUT,
            beanMethod = "apply",
            operation = @Operation(operationId = "updateStudent", tags = "Student usecases",
                    parameters = {@Parameter(name = "studentId", description = "student Id", required= true, in = ParameterIn.PATH),
                            @Parameter(name = "student", in = ParameterIn.PATH,
                                    schema = @Schema(implementation = Student.class))},
                    responses = {
                            @ApiResponse(responseCode = "201", description = "Success",
                                    content = @Content (schema = @Schema(implementation = Student.class))),
                            @ApiResponse(responseCode = "406", description = "Not acceptable, Try again")
                    },
                    requestBody = @RequestBody(required = true, description = "Update a Student following the schema",
                            content = @Content(schema = @Schema(implementation = Student.class)))
            ))
    public RouterFunction<ServerResponse> updateStudent (UpdateStudentUseCase updateStudentUseCase){
        return route(PUT("/quisofka/quizzes/students/{studentId}").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(Student.class)
                        .flatMap(student -> updateStudentUseCase.apply(request.pathVariable("studentId"),
                                        student)
                                .flatMap(result -> ServerResponse.status(201)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                                        .bodyValue(throwable.getMessage()))
                        )
        );
    }

    @Bean
    @RouterOperation(path = "/quisofka/quizzes/students/{studentId}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = DeleteStudentUseCase.class, method = RequestMethod.DELETE,
            beanMethod = "apply",
            operation = @Operation(operationId = "deleteStudentById", tags = "Student usecases",
                    parameters = {@Parameter(name = "studentId", description = "student Id", required= true, in = ParameterIn.PATH)},
                    responses = {
                            @ApiResponse(responseCode = "200", description = "Success",
                                    content = @Content (schema = @Schema(implementation = Student.class))),
                            @ApiResponse(responseCode = "404", description = "Not Found")
                    }))
    public RouterFunction<ServerResponse> deleteStudent (DeleteStudentUseCase deleteStudentUseCase){
        return route(DELETE("/quisofka/quizzes/students/{studentId}"),
                request -> deleteStudentUseCase.apply(request.pathVariable("studentId"))
                        .thenReturn(ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Collections.singletonMap("message", "Student deleted")))
                        .flatMap(serverResponseMono -> serverResponseMono)
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(throwable.getMessage()))
        );
    }

}
