package co.com.quisofka.quizzes.domain.usecase.quiz.deleteAll;

import co.com.quisofka.quizzes.domain.model.quiz.gateways.QuizRepositoryGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class DeleteAllQuizzesUseCase implements Supplier<Mono<Void>> {

    private final QuizRepositoryGateway repositoryGateway;


    @Override
    public Mono<Void> get() {
        return repositoryGateway.deleteAll();
    }

}
