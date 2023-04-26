package co.com.quisofka.quizzes.domain.usecase.student.getstudentbyemail;

import co.com.quisofka.quizzes.domain.model.student.Student;
import co.com.quisofka.quizzes.domain.model.student.gateways.StudentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RequiredArgsConstructor
public class GetStudentByEmailUseCase implements Function<String, Mono<Student>> {

    private final StudentRepository studentRepository;

    @Override
    public Mono<Student> apply(String email) {
        return studentRepository.getStudentByEmail(email);
    }
}
