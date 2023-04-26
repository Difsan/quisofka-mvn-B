package co.com.quisofka.quizzes.infrastructure.drivenAdapters;


import co.com.quisofka.quizzes.infrastructure.drivenAdapters.data.QuizData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoDBRepositoryQuiz extends ReactiveMongoRepository<QuizData, String> {
}
