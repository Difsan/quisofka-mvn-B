package co.com.quisofka.quizzes.domain.usecase.quiz.getQuizById;

import com.quisofka.quizzes.domain.model.quiz.Quiz;
import com.quisofka.quizzes.domain.model.quiz.gateways.QuizRepositoryGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RequiredArgsConstructor
public class GetQuizByIdUseCase implements Function<String, Mono<Quiz>> {

    private final QuizRepositoryGateway repositoryGateway;

    @Override
    public Mono<Quiz> apply(String id) {
        return repositoryGateway.getQuizById(id);
    }

}
