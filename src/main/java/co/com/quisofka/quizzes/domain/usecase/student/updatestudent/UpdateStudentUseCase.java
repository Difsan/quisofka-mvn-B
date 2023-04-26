package co.com.quisofka.quizzes.domain.usecase.student.updatestudent;


import co.com.quisofka.quizzes.domain.model.student.Student;
import co.com.quisofka.quizzes.domain.model.student.gateways.StudentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@RequiredArgsConstructor
public class UpdateStudentUseCase implements BiFunction<String, Student, Mono<Student>> {

    private final StudentRepository studentRepository;

    @Override
    public Mono<Student> apply(String studentId, Student student) {
        return studentRepository.updateStudent(studentId, student);
    }
}
