package co.com.quisofka.quizzes.domain.usecase.student.updatestudent;


import co.com.quisofka.quizzes.domain.model.student.Student;
import co.com.quisofka.quizzes.domain.model.student.gateways.StudentRepository;
import co.com.quisofka.quizzes.domain.usecase.student.updatestudent.UpdateStudentUseCase;
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

class UpdateStudentUseCaseTest {

    @Mock
    StudentRepository repository;

    UpdateStudentUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateStudentUseCase(repository);
    }

    @Test
    @DisplayName("UpdateStudentUseCase_Success")
    void UpdateStudent() {
        var originalStudent = new Student("1", "Diego", "Sanchez",
                "di@gmail.com",true,"initial");

        var updatedStudent = new Student("1", "Diego", "Sanchez",
                "dps@gmail.com",false,"progress");

        Mockito.when(repository.updateStudent("1", updatedStudent))
                .thenReturn(Mono.just(updatedStudent));

        var result = useCase.apply("1", updatedStudent);

        StepVerifier.create(result)
                .expectNext(updatedStudent)
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1))
                .updateStudent("1", updatedStudent);
    }

    @Test
    @DisplayName("UpdateStudentUseCase_Failed")
    void UpdateStudent_Failed() {
        var originalStudent = new Student("1", "Diego", "Sanchez",
                "di@gmail.com",true,"initial");

        var updatedStudent = new Student("1", "Diego", "Sanchez",
                "dps@gmail.com",false,"progress");

        Mockito.when(repository.updateStudent("1", updatedStudent))
                .thenReturn(Mono.error(new IllegalArgumentException("There is not " +
                        "student with id: " + "1")));

        var result = useCase.apply("1", updatedStudent);

        StepVerifier.create(result)
                .expectErrorMessage("There is not " +
                        "student with id: " + "1")
                .verify();

        Mockito.verify(repository, Mockito.times(1))
                .updateStudent("1", updatedStudent);
    }
}