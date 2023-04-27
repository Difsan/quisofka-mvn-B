package co.com.quisofka.quizzes.domain.usecase.quiz.deletebyid;

import co.com.quisofka.quizzes.domain.model.quiz.gateways.QuizRepositoryGateway;
import co.com.quisofka.quizzes.domain.usecase.quiz.getQuizById.GetQuizByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeleteQuizByIdUseCaseTest {

    @Mock
    QuizRepositoryGateway repository;

    DeleteQuizByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteQuizByIdUseCase(repository);
    }

    @Test
    @DisplayName("DeleteQuizByIdUseCase_Success")
    void deleteQuizById(){
        String quizId = "APOS-4587";

        Mockito.when(repository.deleteQuizById(quizId)).thenReturn(Mono.empty());

        var result = repository.deleteQuizById(quizId);

        StepVerifier.create(result)
                .expectNext()
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).deleteQuizById(quizId);
    }

    @Test
    @DisplayName("DeleteQuizByIdUseCase_Failed")
    void deleteQuizById_Failed(){
        String quizId = "APOS-4587";

        Mockito.when(repository.deleteQuizById(quizId))
                .thenReturn(Mono.error(new IllegalArgumentException("There is not " +
                "quiz with id: " + quizId)));

        var result = repository.deleteQuizById(quizId);

        StepVerifier.create(result)
                .expectErrorMessage("There is not " +
                        "quiz with id: " + quizId)
                .verify();

        Mockito.verify(repository, Mockito.times(1)).deleteQuizById(quizId);
    }
}