package co.com.quisofka.quizzes.domain.usecase.quiz.StartQuiz;

import co.com.quisofka.quizzes.domain.model.question.Question;
import co.com.quisofka.quizzes.domain.model.quiz.Quiz;
import co.com.quisofka.quizzes.domain.model.quiz.gateways.QuizRepositoryGateway;
import co.com.quisofka.quizzes.domain.usecase.quiz.deletebyid.DeleteQuizByIdUseCase;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StartQuizUseCaseTest {

    @Mock
    QuizRepositoryGateway repository;

    StartQuizUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new StartQuizUseCase(repository);
    }

    @Test
    @DisplayName("StartQuizUseCase_Success")
    void startQuiz(){
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
        // questions that where contested by the student
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
        Quiz originalQuiz = new Quiz("APOS-4587", questions, questionList,0.0,"1",
                LocalDateTime.now().minusHours(7),null,
                "GENERATED",    "BASIC");

        Quiz sumitedQuiz = new Quiz("APOS-4587", questions, questionList,0.0,"1",
                LocalDateTime.now().minusHours(7),LocalDateTime.now(),
                "GENERATED",    "BASIC");

        Mockito.when(repository.startQuiz("APOS-4587")).thenReturn(Mono.just(sumitedQuiz));

        var result = useCase.apply("APOS-4587");

        StepVerifier.create(result)
                .expectNext(sumitedQuiz)
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).startQuiz("APOS-4587");
    }

    @Test
    @DisplayName("StartQuizUseCase_FailedById")
    void startQuiz_FailedById(){

        String quizId = "APOS-4587";

        Mockito.when(repository.startQuiz("APOS-4587")).thenReturn(Mono.error(new Throwable("There is not " +
                "quiz with id: " + quizId)));

        var result = useCase.apply(quizId);

        StepVerifier.create(result)
                .expectErrorMessage("There is not " +
                        "quiz with id: " + quizId)
                .verify();

        Mockito.verify(repository, Mockito.times(1)).startQuiz(quizId);

    }

    @Test
    @DisplayName("StartQuizUseCase_FailedByCreatedAt")
    void startQuiz_FailedByCreatedAt(){
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
        // questions that where contested by the student
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

        //create quiz, failed because it was created more than 24 hour from now
        Quiz quiz = new Quiz("APOS-4587", questions, questionList,0.0,"1",
                LocalDateTime.now().minusHours(30),null,
                "GENERATED",    "BASIC");

        Mockito.when(repository.startQuiz("APOS-4587")).thenReturn(Mono.error(
                new Throwable (
                        "Quiz with id "
                                + "APOS-4587" +
                                " was created more than 24 hours ago and cannot be modified." +
                                "please ask for another code")));

        var result = useCase.apply("APOS-4587");

        StepVerifier.create(result)
                .expectErrorMessage(
                        "Quiz with id "
                                + "APOS-4587" +
                                " was created more than 24 hours ago and cannot be modified." +
                                "please ask for another code")
                .verify();

        Mockito.verify(repository, Mockito.times(1)).startQuiz("APOS-4587");
    }

    @Test
    @DisplayName("StartQuizUseCase_FailedByStatus")
    void startQuiz_FailedByStatus(){

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
        // questions that where contested by the student
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

        //create quiz, failed because it was ALREADY started
        Quiz quiz = new Quiz("APOS-4587", questions, questionList,0.0,"1",
                LocalDateTime.now().minusHours(10),LocalDateTime.now(),
                "STARTED",    "BASIC");

        Mockito.when(repository.startQuiz("APOS-4587")).thenReturn(Mono.error(
                new Throwable("Quiz with id "
                        + "APOS-4587" +
                        " has already been started")));

        var result = useCase.apply("APOS-4587");

        StepVerifier.create(result)
                .expectErrorMessage("Quiz with id "
                        + "APOS-4587" +
                        " has already been started")
                .verify();

        Mockito.verify(repository, Mockito.times(1)).startQuiz("APOS-4587");

    }


}