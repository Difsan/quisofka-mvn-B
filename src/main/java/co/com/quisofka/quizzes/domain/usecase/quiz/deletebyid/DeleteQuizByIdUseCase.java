package co.com.quisofka.quizzes.domain.usecase.quiz.deletebyid;

import co.com.quisofka.quizzes.domain.model.quiz.gateways.QuizRepositoryGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RequiredArgsConstructor
public class DeleteQuizByIdUseCase implements Function <String, Mono<Void>> {

    private final QuizRepositoryGateway quizRepositoryGateway;


    @Override
    public Mono<Void> apply(String id) {
        return quizRepositoryGateway.deleteQuizById(id);
    }
}
