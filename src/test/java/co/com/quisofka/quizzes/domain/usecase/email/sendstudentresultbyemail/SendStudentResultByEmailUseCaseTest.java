package co.com.quisofka.quizzes.domain.usecase.email.sendstudentresultbyemail;

import co.com.quisofka.quizzes.domain.model.email.Email;
import co.com.quisofka.quizzes.domain.model.email.gateways.EmailReposiroty;
import co.com.quisofka.quizzes.domain.usecase.email.sendquizbyemail.SendQuizCodeByEmailUseCase;
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
class SendStudentResultByEmailUseCaseTest {

    @Mock
    EmailReposiroty repository;

    SendStudentResultByEmailUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SendStudentResultByEmailUseCase(repository);
    }

    @Test
    @DisplayName("SendStudentResultByEmailUseCase_Success")
    void sendStudentResultByEmail(){
        var email = new Email("di@gmail.com", "Diego Sanchez");

        String studentResult = "26";

        Mockito.when(repository.sendStudentResultByEmail(email, studentResult)).thenReturn(Mono.empty());

        var result = useCase.apply(email, studentResult);

        StepVerifier.create(result)
                .expectNext()
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).sendStudentResultByEmail(email,studentResult);
    }

    @Test
    @DisplayName("SendStudentResultByEmailUseCase_Failed")
    void sendStudentResultByEmail_Failed(){
        var email = new Email("di@gmail.com", "Diego Sanchez");

        String studentResult = "26";

        Mockito.when(repository.sendStudentResultByEmail(email, studentResult)).thenReturn(Mono.error(new RuntimeException()));

        var result = useCase.apply(email, studentResult);

        StepVerifier.create(result)
                .expectError()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).sendStudentResultByEmail(email,studentResult);
    }
}