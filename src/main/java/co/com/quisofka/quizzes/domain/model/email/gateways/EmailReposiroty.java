package co.com.quisofka.quizzes.domain.model.email.gateways;

import co.com.quisofka.quizzes.domain.model.email.Email;
import reactor.core.publisher.Mono;

public interface EmailReposiroty {
    Mono<Void> sendQuizCodeByEmail(Email email, String quizCode);
    Mono<Void> sendStudentResultByEmail(Email email);
}
