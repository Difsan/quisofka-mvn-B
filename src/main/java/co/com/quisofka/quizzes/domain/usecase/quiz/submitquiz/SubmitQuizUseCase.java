package co.com.quisofka.quizzes.domain.usecase.quiz.submitquiz;

import co.com.quisofka.quizzes.domain.model.quiz.Quiz;
import co.com.quisofka.quizzes.domain.model.quiz.gateways.QuizRepositoryGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@RequiredArgsConstructor
public class SubmitQuizUseCase implements BiFunction<String, Quiz, Mono<Quiz>> {

    private final QuizRepositoryGateway quizRepositoryGateway;

    @Override
    public Mono<Quiz> apply(String id, Quiz quiz) {
        return quizRepositoryGateway.submitQuiz(id, quiz);
    }
}
