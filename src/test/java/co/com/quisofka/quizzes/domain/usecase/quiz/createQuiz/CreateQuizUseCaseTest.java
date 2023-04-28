package co.com.quisofka.quizzes.domain.usecase.quiz.createQuiz;

import co.com.quisofka.quizzes.domain.model.question.Question;
import co.com.quisofka.quizzes.domain.model.quiz.Quiz;
import co.com.quisofka.quizzes.domain.model.quiz.gateways.QuizRepositoryGateway;
import co.com.quisofka.quizzes.domain.model.student.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CreateQuizUseCaseTest {

    @Mock
    QuizRepositoryGateway repository;

    CreateQuizUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateQuizUseCase(repository);
    }

    @Test
    @DisplayName("CreateQuizUseCase_Success")
    void createQuiz(){
        String studentId = "1";
        Quiz requestQuiz = new Quiz(null,null,null,null,
                studentId,null,null,null,null);

        //Answers to be included in each question
        List<List<Object>> answers1 = new ArrayList<>(
                Arrays.asList(
                        Arrays.asList("Se basa en el uso de flujos de datos",true),
                        Arrays.asList("Requiere el uso de hilos de ejecución", false),
                        Arrays.asList("Permite manejar grandes volúmenes de datos", true),
                        Arrays.asList("Es una técnica obsoleta y poco utilizada", false)
                )

        );
        List<List<Object>> answers2 = new ArrayList<>(
                Arrays.asList(
                        Arrays.asList("map",true),
                        Arrays.asList("filter", true),
                        Arrays.asList("reduce", true),
                        Arrays.asList("merge", false))

        );

        //create the questions
        Question question1 = new Question("6449e69a2f0ebe21ed3e0f1d",
                "¿Cuál(es) de las siguientes afirmaciones son verdaderas sobre programación reactiva en Java?",
                answers1,"Java", "Programación reactiva", "multiple", "INTERMEDIATE");

        Question question2 = new Question("6449e6762f0ebe21ed3e0f12",
                "¿Cuál(es) de los siguientes son operadores de transformación en programación reactiva en Java?",
                answers2, "Java", "Programación reactiva", "multiple","intermediate" );

        // quiz
        // questions that were answered by the student
        List<List<Object>> questions = new ArrayList<>(
                Arrays.asList(
                        Arrays.asList("6449e69a2f0ebe21ed3e0f1d",false),
                        Arrays.asList("6449e6762f0ebe21ed3e0f12",false)
                )
        );

        // questions we get from the DB
        HashSet<Question> questionList = new HashSet<Question>(){{
            add(question1);
            add(question2);
        }};

        //create quiz
        Quiz createdQuiz = new Quiz("APOS-4587", questions, questionList,null,studentId,
                LocalDateTime.now(),null, "GENERATED",
                "BASIC");

        Mockito.when(repository.createQuiz(requestQuiz)).thenReturn(Mono.just(createdQuiz));

        var result = useCase.apply(requestQuiz);

        StepVerifier.create(result)
                .expectNext(createdQuiz)
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).createQuiz(requestQuiz);
    }

    @Test
    @DisplayName("CreateQuizUseCase_FailedByIsAuthorized")
    void createQuiz_FailedByIsAuthorized(){
        String studentId = "1";
        var student = new Student(studentId, "Diego", "Sanchez",
                "di@gmail.com",false,"initial");
        Quiz requestQuiz = new Quiz(null,null,null,null,
                studentId,null,null,null,null);

        Mockito.when(repository.createQuiz(requestQuiz)).thenReturn(Mono.error(
                new Throwable("Unauthorized student")));

        var result = useCase.apply(requestQuiz);

        StepVerifier.create(result)
                .expectErrorMessage("Unauthorized student")
                .verify();

        Mockito.verify(repository, Mockito.times(1)).createQuiz(requestQuiz);

    }

    @Test
    @DisplayName("CreateQuizUseCase_FailedByStudentLevel")
    void createQuiz_FailedByStudentLevel(){
        String studentId = "1";
        var student = new Student(studentId, "Diego", "Sanchez",
                "di@gmail.com",true,"INTERMEDIATE");
        Quiz requestQuiz = new Quiz(null,null,null,null,
                studentId,null,null,null,null);

        Mockito.when(repository.createQuiz(requestQuiz)).thenReturn(Mono.error(
                new Throwable("Student is in the maximum level")));

        var result = useCase.apply(requestQuiz);

        StepVerifier.create(result)
                .expectErrorMessage("Student is in the maximum level")
                .verify();

        Mockito.verify(repository, Mockito.times(1)).createQuiz(requestQuiz);

    }
}