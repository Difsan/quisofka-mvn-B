package co.com.quisofka.quizzes.domain.usecase.email.sendstudentresultbyemail;

import co.com.quisofka.quizzes.domain.model.email.Email;
import co.com.quisofka.quizzes.domain.model.email.gateways.EmailReposiroty;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@RequiredArgsConstructor
public class SendStudentResultByEmailUseCase implements BiFunction<Email,String, Mono<Void>> {

    private final EmailReposiroty emailReposiroty;

    @Override
    public Mono<Void> apply(Email email, String quizResult) {
        return emailReposiroty.sendStudentResultByEmail(email, quizResult);
    }
}
