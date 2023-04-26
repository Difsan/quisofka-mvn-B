package co.com.quisofka.quizzes.domain.usecase.student.deletestudent;

import co.com.quisofka.quizzes.domain.model.student.gateways.StudentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RequiredArgsConstructor
public class DeleteStudentUseCase implements Function<String, Mono<Void>> {

    private final StudentRepository studentRepository;

    @Override
    public Mono<Void> apply(String studentId) {
        return studentRepository.deleteStudent(studentId);
    }
}
