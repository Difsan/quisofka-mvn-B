package co.com.quisofka.quizzes.domain.usecase.quiz.submitquiz;

import co.com.quisofka.quizzes.domain.model.question.Question;
import co.com.quisofka.quizzes.domain.model.quiz.Quiz;
import co.com.quisofka.quizzes.domain.model.quiz.gateways.QuizRepositoryGateway;
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
import java.util.*;


@ExtendWith(MockitoExtension.class)
class SubmitQuizUseCaseTest {

    @Mock
    QuizRepositoryGateway repository;

    SubmitQuizUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SubmitQuizUseCase(repository);
    }

    @Test
    @DisplayName("SubmitQuizUseCase_Success")
    void submitQuiz(){

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
                LocalDateTime.now(),LocalDateTime.now(), "GENERATED",
                "BASIC");

        //updated quiz, 1 answer right
        // questions that were answered by the student
        List<List<Object>> updatequestions = new ArrayList<>(
                Arrays.asList(
                        Arrays.asList("6449e69a2f0ebe21ed3e0f1d",true),
                        Arrays.asList("6449e6762f0ebe21ed3e0f12",true)
                )
        );
        Quiz toUpdateQuiz = new Quiz("APOS-4587", updatequestions, questionList,0.0,"1",
                LocalDateTime.now(),LocalDateTime.now(), "GENERATED",
                "BASIC");

        Quiz updatedQuiz = new Quiz("APOS-4587", updatequestions, questionList,2.0,"1",
                LocalDateTime.now(),LocalDateTime.now(), "FINISHED",
                "BASIC");

        Mockito.when(repository.submitQuiz("APOS-4587",toUpdateQuiz))
                .thenReturn(Mono.just(updatedQuiz));

        var result = useCase.apply("APOS-4587", toUpdateQuiz);

        StepVerifier.create(result)
                .expectNext(updatedQuiz)
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).submitQuiz("APOS-4587", toUpdateQuiz);
    }

    @Test
    @DisplayName("SubmitQuizUseCase_FailedById")
    void submitQuiz_FailedById(){
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
                LocalDateTime.now(),LocalDateTime.now(), "GENERATED",
                "BASIC");

        // questions that where contested by the student
        List<List<Object>> updatequestions = new ArrayList<>(
                Arrays.asList(
                        Arrays.asList("6449e69a2f0ebe21ed3e0f1d",true),
                        Arrays.asList("6449e6762f0ebe21ed3e0f12",true)
                )
        );
        Quiz toUpdateQuiz = new Quiz("APOS-4587", updatequestions, questionList,0.0,"1",
                LocalDateTime.now(),LocalDateTime.now(), "GENERATED",
                "BASIC");

        Mockito.when(repository.submitQuiz("APOS-4587", toUpdateQuiz))
                .thenReturn(Mono.error(new IllegalArgumentException("There is not " +
                        "quiz with id: APOS-4587")));

        var result = useCase.apply("APOS-4587", toUpdateQuiz);

        StepVerifier.create(result)
                .expectErrorMessage("There is not " +
                        "quiz with id: APOS-4587")
                .verify();

        Mockito.verify(repository, Mockito.times(1)).submitQuiz("APOS-4587", toUpdateQuiz);
    }

    @Test
    @DisplayName("SubmitQuizUseCase_FailedByStatus")
    void submitQuiz_FailedByStatus(){
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
                LocalDateTime.now(),LocalDateTime.now(), "FINISHED",
                "BASIC");

        Mockito.when(repository.submitQuiz("APOS-4587", quiz))
                .thenReturn(Mono.error(new IllegalArgumentException("The quiz can no longer be submitted, " +
                        "it was already submitted")));

        var result = useCase.apply("APOS-4587", quiz);

        StepVerifier.create(result)
                .expectErrorMessage("The quiz can no longer be submitted, " +
                        "it was already submitted")
                .verify();

        Mockito.verify(repository, Mockito.times(1)).submitQuiz("APOS-4587", quiz);

    }

    @Test
    @DisplayName("SubmitQuizUseCase_FailedByStartedAt")
    void submitQuiz_FailedByStartedAt(){
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
                LocalDateTime.now(),LocalDateTime.now().minusHours(2), "GENERATED",
                "BASIC");

        Mockito.when(repository.submitQuiz("APOS-4587", quiz))
                .thenReturn(Mono.error(new IllegalArgumentException("The quiz can no longer be submitted, " +
                        "you spend more than 1 hour on it.")));

        var result = useCase.apply("APOS-4587", quiz);

        StepVerifier.create(result)
                .expectErrorMessage("The quiz can no longer be submitted, " +
                        "you spend more than 1 hour on it.")
                .verify();

        Mockito.verify(repository, Mockito.times(1)).submitQuiz("APOS-4587", quiz);
    }

}