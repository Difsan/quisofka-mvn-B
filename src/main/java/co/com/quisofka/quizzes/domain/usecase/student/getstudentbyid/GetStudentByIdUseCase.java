package co.com.quisofka.quizzes.domain.usecase.student.getstudentbyid;

import co.com.quisofka.quizzes.domain.model.student.Student;
import co.com.quisofka.quizzes.domain.model.student.gateways.StudentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RequiredArgsConstructor
public class GetStudentByIdUseCase implements Function<String, Mono<Student>> {

    private final StudentRepository studentRepository;

    @Override
    public Mono<Student> apply(String studentId) {
        return studentRepository.getStudentById(studentId);
    }
}
