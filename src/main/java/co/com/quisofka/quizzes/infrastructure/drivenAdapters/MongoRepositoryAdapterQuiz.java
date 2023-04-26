package co.com.quisofka.quizzes.infrastructure.drivenAdapters;


import co.com.quisofka.quizzes.domain.model.question.Question;
import co.com.quisofka.quizzes.domain.model.quiz.Quiz;
import co.com.quisofka.quizzes.domain.model.quiz.enums.Level;
import co.com.quisofka.quizzes.domain.model.quiz.enums.Status;
import co.com.quisofka.quizzes.domain.model.quiz.gateways.QuizRepositoryGateway;
import co.com.quisofka.quizzes.infrastructure.drivenAdapters.data.QuizData;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MongoRepositoryAdapterQuiz implements QuizRepositoryGateway {

    private final MongoDBRepositoryQuiz quizRepository;
    private final MongoDBRepositoryStudent studentRepository;
    private final ObjectMapper mapper;
    private WebClient questionsAPI=WebClient.create("https://quisofka-b-production-c47e.up.railway.app/quisofka/questions");


    @Override
    public Flux<Quiz> getAllQuizzes() {
        return this.quizRepository
                .findAll()
                .switchIfEmpty(Mono.error(new Throwable("No quizzes available")))
                .map(item -> mapper.map(item, Quiz.class));
    }

    @Override
    public Mono<Quiz> getQuizById(String id) {
        return this.quizRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("There is not " +
                        "quiz with id: " + id)))
                .map(quizData -> mapper.map(quizData, Quiz.class));
    }

    @Override
    public Mono<Quiz> createQuiz(Quiz quiz) {
        return null;
    }

    //TODO: por ahora va sin restricciones, si hay tiempo unificamos para que haya restricciones
    // TODO: por ejemplo comprobar que esté autorizado, que sea del nivel que se pide etc
    @Override
    public Mono<Quiz> createFirstLvlQuiz(Quiz quiz) {

        return questionsAPI.get()
                .uri("/first")
                .retrieve()
                .bodyToFlux(Question.class)
                .collectList()
                .flatMap(questions -> {
                    HashSet<Question> questionList=new HashSet<>(questions);
                    Map<String, Boolean> quizQuestions=questionList.stream()
                            .collect(Collectors.toMap(Question::getId, q -> false));
                    Quiz newQuiz=Quiz.builder()
                            .questionList(questionList)
                            .questions(quizQuestions)
                            .studentId(quiz.getStudentId())
                            .createdAt(LocalDateTime.now())
                            .status(Status.GENERATED.name())
                            .level(Level.INITIAL.name())
                            .build();

                    return this.quizRepository.save(mapper.map(newQuiz, QuizData.class));
                })
                .map(quiz2 -> mapper.map(quiz2, Quiz.class));
    }


    @Override
    public Mono<Quiz> createSecondLvlQuiz(Quiz quiz) {

        return questionsAPI.get()
                .uri("/second")
                .retrieve()
                .bodyToFlux(Question.class)
                .collectList()
                .flatMap(questions -> {
                    HashSet<Question> questionList=new HashSet<>(questions);
                    Map<String, Boolean> quizQuestions=questionList.stream()
                            .collect(Collectors.toMap(Question::getId, q -> false));
                    Quiz newQuiz=Quiz.builder()
                            .questionList(questionList)
                            .questions(quizQuestions)
                            .studentId(quiz.getStudentId())
                            .createdAt(LocalDateTime.now())
                            .status(Status.GENERATED.name())
                            .level(Level.BASIC.name())
                            .build();

                    return this.quizRepository.save(mapper.map(newQuiz, QuizData.class));
                })
                .map(quiz2 -> mapper.map(quiz2, Quiz.class));
    }

    @Override
    public Mono<Quiz> createThirdLvlQuiz(Quiz quiz) {

        return questionsAPI.get()
                .uri("/third")
                .retrieve()
                .bodyToFlux(Question.class)
                .collectList()
                .flatMap(questions -> {
                    HashSet<Question> questionList=new HashSet<>(questions);
                    Map<String, Boolean> quizQuestions=questionList.stream()
                            .collect(Collectors.toMap(Question::getId, q -> false));
                    Quiz newQuiz=Quiz.builder()
                            .questionList(questionList)
                            .questions(quizQuestions)
                            .studentId(quiz.getStudentId())
                            .createdAt(LocalDateTime.now())
                            .status(Status.GENERATED.name())
                            .level(Level.INTERMEDIATE.name())
                            .build();

                    return this.quizRepository.save(mapper.map(newQuiz, QuizData.class));
                })
                .map(quiz2 -> mapper.map(quiz2, Quiz.class));
    }

    @Override
    public Mono<Void> deleteAll() {
        return this.quizRepository.deleteAll()
                .onErrorResume(Mono::error);
    }
}
