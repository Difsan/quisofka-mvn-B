package co.com.quisofka.quizzes.infrastructure.drivenAdapters;

import co.com.quisofka.quizzes.domain.model.student.Student;
import co.com.quisofka.quizzes.infrastructure.drivenAdapters.data.StudentData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MongoDBRepositoryStudent extends ReactiveMongoRepository<StudentData, String> {
    Mono<Student> findByEmail(String email);
}
