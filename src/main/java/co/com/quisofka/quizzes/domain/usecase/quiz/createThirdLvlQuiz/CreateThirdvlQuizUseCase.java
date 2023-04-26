package co.com.quisofka.quizzes.domain.usecase.quiz.createThirdLvlQuiz;

import com.quisofka.quizzes.domain.model.quiz.Quiz;
import com.quisofka.quizzes.domain.model.quiz.gateways.QuizRepositoryGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RequiredArgsConstructor
public class CreateThirdvlQuizUseCase implements Function<Quiz, Mono<Quiz>> {

    private final QuizRepositoryGateway repositoryGateway;

    @Override
    public Mono<Quiz> apply(Quiz quiz) {
        return repositoryGateway.createThirdLvlQuiz(quiz);
    }
}
