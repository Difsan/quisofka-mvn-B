package co.com.quisofka.quizzes.domain.usecase.email.sendquizbyemail;

import co.com.quisofka.quizzes.domain.model.email.Email;
import co.com.quisofka.quizzes.domain.model.email.gateways.EmailReposiroty;
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
class SendQuizCodeByEmailUseCaseTest {

    @Mock
    EmailReposiroty repository;

    SendQuizCodeByEmailUseCase useCase;


    @BeforeEach
    void setUp() {
        useCase = new SendQuizCodeByEmailUseCase(repository);
    }

    @Test
    @DisplayName("SendQuizCodeByEmailUseCase_Success")
    void SendQuizCodeByEmail(){
        var email = new Email("di@gmail.com", "Diego Sanchez");

        String quizCode = "opkj-69874";

        Mockito.when(repository.sendQuizCodeByEmail(email, quizCode)).thenReturn(Mono.empty());

        var result = useCase.apply(email, quizCode);

        StepVerifier.create(result)
                .expectNext()
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).sendQuizCodeByEmail(email,quizCode);
    }

    @Test
    @DisplayName("SendQuizCodeByEmailUseCase_Failed")
    void SendQuizCodeByEmail_Failed(){
        var email = new Email("di@gmail.com", "Diego Sanchez");

        String quizCode = "opkj-69874";

        Mockito.when(repository.sendQuizCodeByEmail(email, quizCode)).thenReturn(Mono.error(new RuntimeException()));

        var result = useCase.apply(email, quizCode);

        StepVerifier.create(result)
                .expectError()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).sendQuizCodeByEmail(email,quizCode);
    }


}