package co.com.quisofka.quizzes.domain.usecase.student.savestudent;

import co.com.quisofka.quizzes.domain.model.student.Student;
import co.com.quisofka.quizzes.domain.model.student.gateways.StudentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RequiredArgsConstructor
public class SaveStudentUseCase implements Function<Student, Mono<Student>> {

    private final StudentRepository studentRepository;

    @Override
    public Mono<Student> apply(Student student) {
        return studentRepository.saveStudent(student);
    }
}
