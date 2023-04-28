package co.com.quisofka.quizzes.domain.usecase.quiz.deleteAll;

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


@ExtendWith(MockitoExtension.class)
class DeleteAllQuizzesUseCaseTest {

    @Mock
    QuizRepositoryGateway repository;

    DeleteAllQuizzesUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteAllQuizzesUseCase(repository);
    }

    @Test
    @DisplayName("DeleteAllQuizzesUseCase_Success")
    void deleteAllQuizzes(){
        Mockito.when(repository.deleteAll()).thenReturn(Mono.empty());

        var result = useCase.get();

        StepVerifier.create(result)
                .expectNext()
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).deleteAll();
    }

}