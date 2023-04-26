package co.com.quisofka.quizzes.domain.model.question.gateways;

import com.quisofka.quizzes.domain.model.question.Question;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QuestionRepositoryGateway {

    Flux<Question> getAllQuestions();
    Flux<Question> getFirstLvlQuestions();
    Flux<Question> getSecondLvlQuestions();
    Flux<Question> getThirdLvlQuestions();
    Mono<Question> getQuestionById(String id);
    Mono<Question> createQuestion(Question question);
    Mono<Void> deleteAll();

}
