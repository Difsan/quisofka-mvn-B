package co.com.quisofka.quizzes.infrastructure.drivenAdapters;

import co.com.quisofka.quizzes.domain.model.email.Email;
import co.com.quisofka.quizzes.domain.model.email.gateways.EmailReposiroty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AdapterEmail implements EmailReposiroty {
    @Override
    public Mono<Void> sendQuizCodeByEmail(Email email) {
        return null;
    }

    @Override
    public Mono<Void> sendStudentResultByEmail(Email email) {
        return null;
    }
}
