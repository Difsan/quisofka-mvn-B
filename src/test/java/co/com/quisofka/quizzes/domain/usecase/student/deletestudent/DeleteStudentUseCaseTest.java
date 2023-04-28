package co.com.quisofka.quizzes.domain.usecase.student.deletestudent;

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
class DeleteStudentUseCaseTest {

    @Mock
    StudentRepository repository;

    DeleteStudentUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteStudentUseCase(repository);
    }

    @Test
    @DisplayName("DeleteStudentUseCase_Success")
    void deleteStudent(){
        var student = new Student("1", "Diego", "Sanchez",
                "di@gmail.com",true,"initial");

        Mockito.when(repository.deleteStudent("1")).thenReturn(Mono.empty());

        var result = useCase.apply("1");

        StepVerifier.create(result)
                .expectNext()
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).deleteStudent("1");
    }

    @Test
    @DisplayName("DeleteStudentUseCase_Failed")
    void deleteStudent_Failed() {
        String studentId = "1";
        Mockito.when(repository.deleteStudent(studentId))
                .thenReturn(Mono.error(new IllegalArgumentException("There is not " +
                        "student with id: " + studentId)));

        var result = useCase.apply("1");

        StepVerifier.create(result)
                .expectErrorMessage("There is not " +
                        "student with id: " + studentId)
                .verify();

        Mockito.verify(repository, Mockito.times(1)).deleteStudent(studentId);
    }


}