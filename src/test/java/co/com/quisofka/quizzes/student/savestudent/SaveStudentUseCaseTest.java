package co.com.quisofka.quizzes.student.savestudent;

import co.com.quisofka.quizzes.domain.model.student.Student;
import co.com.quisofka.quizzes.domain.model.student.gateways.StudentRepository;
import co.com.quisofka.quizzes.domain.usecase.student.savestudent.SaveStudentUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.HttpURLConnection;

@ExtendWith(MockitoExtension.class)
class SaveStudentUseCaseTest {

    @Mock
    StudentRepository repository;

    SaveStudentUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SaveStudentUseCase(repository);
    }

    @Test
    @DisplayName("SaveStudentUseCase_Success")
    void saveStudent(){
        var student = new Student("1", "Diego", "Sanchez",
                "di@gmail.com",true,"initial");

        Mockito.when(repository.saveStudent(student)).thenReturn(Mono.just(student));

        var result = useCase.apply(student);

        StepVerifier.create(result)
                .expectNext(student)
                .expectComplete()
                .verify();

        Mockito.verify(repository, Mockito.times(1)).saveStudent(student);

    }

    @Test
    @DisplayName("SaveStudentUseCase_Failed")
    void saveStudent_Failed() {
        var student = new Student("1", "Diego", "Sanchez",
                "di@gmail.com",true,"initial");

        Mockito.when(repository.saveStudent(Mockito.any(Student.class)))
                .thenReturn(Mono.error(new Throwable(Integer.toString(
                        HttpURLConnection.HTTP_BAD_REQUEST))));

        var result = useCase.apply(student);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable != null &&
                        throwable.getMessage().equals(Integer.toString(
                                HttpURLConnection.HTTP_BAD_REQUEST)))
                .verify();

        Mockito.verify(repository, Mockito.times(1)).saveStudent(student);
    }
}