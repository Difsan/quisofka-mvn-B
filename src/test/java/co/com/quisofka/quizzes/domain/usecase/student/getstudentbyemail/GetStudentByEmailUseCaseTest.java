package co.com.quisofka.quizzes.domain.usecase.student.getstudentbyemail;

import co.com.quisofka.quizzes.domain.model.student.Student;
import co.com.quisofka.quizzes.domain.model.student.gateways.StudentRepository;
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
class GetStudentByEmailUseCaseTest {

    @Mock
    StudentRepository repository;

    GetStudentByEmailUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetStudentByEmailUseCase(repository);
    }

    @Test
    @DisplayName("GetStudentByEmailUseCase_Success")
    void getStudentByEmail(){
        var student = new Student("1", "Diego", "Sanchez",
                "di@gmail.com",true,"initial");

        Mockito.when(repository.getStudentByEmail("di@gmail.com")).thenReturn(Mono.just(student));

        var result = useCase.apply("di@gmail.com");

        StepVerifier.create(result)
                .expectNext(student)
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).getStudentByEmail("di@gmail.com");
    }

    @Test
    @DisplayName("GetStudentByEmailUseCase_Failed")
    void getStudentByEmail_Failed() {
        String studentEmail= "di@gmail.com";
        Mockito.when(repository.getStudentByEmail(studentEmail))
                .thenReturn(Mono.error(new IllegalArgumentException("There is not " +
                        "student with id: " + studentEmail)));

        var result = useCase.apply(studentEmail);

        StepVerifier.create(result)
                .expectErrorMessage("There is not " +
                        "student with id: " + studentEmail)
                .verify();

        Mockito.verify(repository, Mockito.times(1))
                .getStudentByEmail(studentEmail);
    }
}