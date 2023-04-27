package co.com.quisofka.quizzes.domain.usecase.quiz.getAllQuizzes;

import co.com.quisofka.quizzes.domain.model.question.Question;
import co.com.quisofka.quizzes.domain.model.quiz.Quiz;
import co.com.quisofka.quizzes.domain.model.quiz.gateways.QuizRepositoryGateway;
import co.com.quisofka.quizzes.domain.usecase.quiz.getQuizById.GetQuizByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GetAllQuizzesUseCaseTest {

    @Mock
    QuizRepositoryGateway repository;

    GetAllQuizzesUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAllQuizzesUseCase(repository);
    }

    @Test
    @DisplayName("GetAllQuizzesUseCase_Success")
    void getAllQuizzes(){

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
        Quiz quiz = new Quiz("APOS-4587", questions, questionList,0.0,"1",
                LocalDateTime.now(),LocalDateTime.now(), "GENERATED",
                "BASIC");

        Quiz quiz2 = new Quiz("APOS-4588", questions, questionList,0.0,"1",
                LocalDateTime.now(),LocalDateTime.now(), "GENERATED",
                "BASIC");

        // List of quizzes
        var fluxQuizzes = Flux.just(quiz, quiz2);

        Mockito.when(repository.getAllQuizzes()).thenReturn(fluxQuizzes);

        var result = useCase.get();

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();

        Mockito.verify(repository, Mockito.times(1)).getAllQuizzes();

    }

    @Test
    @DisplayName("GetAllQuizzesUseCase_Failed")
    void getAllQuizzes_Failed(){
        Mockito.when(repository.getAllQuizzes()).thenReturn(Flux.error(new Throwable("No quizzes available")));

        var result = useCase.get();

        StepVerifier.create(result)
                .expectErrorMessage("No quizzes available")
                .verify();

        Mockito.verify(repository, Mockito.times(1)).getAllQuizzes();
    }
}