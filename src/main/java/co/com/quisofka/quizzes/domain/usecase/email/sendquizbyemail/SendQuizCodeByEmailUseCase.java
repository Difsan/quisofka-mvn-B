package co.com.quisofka.quizzes.domain.usecase.email.sendquizbyemail;

import co.com.quisofka.quizzes.domain.model.email.Email;
import co.com.quisofka.quizzes.domain.model.email.gateways.EmailReposiroty;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RequiredArgsConstructor
public class SendQuizCodeByEmailUseCase implements Function<Email, Mono<Void>> {

    private final EmailReposiroty emailReposiroty;

    @Override
    public Mono<Void> apply(Email email) {
        return emailReposiroty.sendQuizCodeByEmail(email);
    }
}
