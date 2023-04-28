package co.com.quisofka.quizzes.domain.usecase.quiz.createFirstLvlQuiz;

import co.com.quisofka.quizzes.domain.model.question.Question;
import co.com.quisofka.quizzes.domain.model.quiz.Quiz;
import co.com.quisofka.quizzes.domain.model.quiz.gateways.QuizRepositoryGateway;
import co.com.quisofka.quizzes.domain.model.student.Student;
import co.com.quisofka.quizzes.domain.usecase.quiz.StartQuiz.StartQuizUseCase;
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
class CreateFirstLvlQuizUseCaseTest {

    @Mock
    QuizRepositoryGateway repository;

    CreateFirstLvlQuizUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateFirstLvlQuizUseCase(repository);
    }

    @Test
    @DisplayName("CreateFirstLvlQuizUseCase_Success")
    void createFirstLvlQuiz(){

        String studentId = "1";

        var student = new Student(studentId, "Diego", "Sanchez",
                "di@gmail.com",false,"PENDING");

        Quiz requestQuiz = new Quiz(null,null,null,null,
                studentId,null,null,null,null);

        //Answers to be included in each question
        List<List<Object>> answers1 = new ArrayList<>(
                Arrays.asList(
                        Arrays.asList("Verdadero",true),
                        Arrays.asList("Falso", false)
                )

        );
        List<List<Object>> answers2 = new ArrayList<>(
                Arrays.asList(
                        Arrays.asList("Un lenguaje de programación que se utiliza para crear páginas web dinámicas",false),
                        Arrays.asList("Una base de datos que se utiliza para almacenar información de una aplicación web", false),
                        Arrays.asList("Un modelo de objetos que representa la estructura de una página web", true),
                        Arrays.asList("Una biblioteca de funciones que se utiliza para manipular datos en una aplicación web", false))

        );

        //create the questions
        Question question1 = new Question("644adbf2f396e447be1a49c8",
                "Angular utiliza TypeScript como lenguaje de programación",
                answers1,"Javascript", "Angular", "truefalse", "BASIC");

        Question question2 = new Question("644adbf2f396e447be1a49f4",
                "¿Qué es el DOM en JavaScript?",
                answers2, "Javascript", "DOM", "single","BASIC");

        // quiz
        // questions that where contested by the student
        List<List<Object>> questions = new ArrayList<>(
                Arrays.asList(
                        Arrays.asList("644adbf2f396e447be1a49c8",false),
                        Arrays.asList("644adbf2f396e447be1a49f4",false)
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

        Mockito.when(repository.createFirstLvlQuiz(requestQuiz)).thenReturn(Mono.just(createdQuiz));

        var result = useCase.apply(requestQuiz);

        StepVerifier.create(result)
                .expectNext(createdQuiz)
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).createFirstLvlQuiz(requestQuiz);

    }
}