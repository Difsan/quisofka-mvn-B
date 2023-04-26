package co.com.quisofka.quizzes.student.getstudentbyid;

import co.com.quisofka.quizzes.domain.model.student.Student;
import co.com.quisofka.quizzes.domain.model.student.gateways.StudentRepository;
import co.com.quisofka.quizzes.domain.usecase.student.getstudentbyid.GetStudentByIdUseCase;
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
class GetStudentByIdUseCaseTest {

    @Mock
    StudentRepository repository;

    GetStudentByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetStudentByIdUseCase(repository);
    }

    @Test
    @DisplayName("GetStudentByIdUseCase_Success")
    void getStudentById(){
        var student = new Student("1", "Diego", "Sanchez",
                "di@gmail.com",true,"initial");

        Mockito.when(repository.getStudentById("1")).thenReturn(Mono.just(student));

        var result = useCase.apply("1");

        StepVerifier.create(result)
                .expectNext(student)
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).getStudentById("1");
    }

    @Test
    @DisplayName("GetStudentByIdUseCase_Failed")
    void getStudentById_Failed() {

        String studentId = "1";

        Mockito.when(repository.getStudentById(studentId))
                .thenReturn(Mono.error(new IllegalArgumentException("There is not " +
                        "student with id: " + studentId)));

        var result = useCase.apply(studentId);

        StepVerifier.create(result)
                .expectErrorMessage("There is not " +
                        "student with id: " + studentId)
                .verify();

        Mockito.verify(repository, Mockito.times(1))
                .getStudentById(studentId);
    }

}