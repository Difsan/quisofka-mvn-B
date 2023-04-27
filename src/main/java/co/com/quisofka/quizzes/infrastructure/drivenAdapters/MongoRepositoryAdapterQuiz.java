package co.com.quisofka.quizzes.infrastructure.drivenAdapters;


import co.com.quisofka.quizzes.domain.model.question.Question;
import co.com.quisofka.quizzes.domain.model.quiz.Quiz;
import co.com.quisofka.quizzes.domain.model.quiz.enums.Level;
import co.com.quisofka.quizzes.domain.model.quiz.enums.Status;
import co.com.quisofka.quizzes.domain.model.quiz.gateways.QuizRepositoryGateway;
import co.com.quisofka.quizzes.infrastructure.drivenAdapters.data.QuizData;
import co.com.quisofka.quizzes.infrastructure.drivenAdapters.util.customCode.CustomUUID;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
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
                .switchIfEmpty(Mono.error(new Throwable("There is not " +
                        "quiz with id: " + id)))
                .map(quizData -> mapper.map(quizData, Quiz.class));
    }

    @Override
    public Mono<Quiz> createQuiz(Quiz quiz) {
        return this.studentRepository
                .findById(quiz.getStudentId())
                .flatMap(studentData -> {
                    if (!studentData.getIsAuthorized()){
                        return Mono.error(new Throwable("Unauthorized student"));
                    }
                    if (studentData.getLevel().equalsIgnoreCase("INTERMEDIATE")){
                        return Mono.error(new Throwable("Student is in the maximum level"));
                    }

                    if (studentData.getLevel().equalsIgnoreCase("PENDING")){
                        return createFirstLvlQuiz(quiz);
                    }

                    if (studentData.getLevel().equalsIgnoreCase("INITIAL")){
                        return createSecondLvlQuiz(quiz);
                    }

                    if (studentData.getLevel().equalsIgnoreCase("BASIC")){
                        return createThirdLvlQuiz(quiz);
                    }

                    else {
                        return createFirstLvlQuiz(quiz);
                    }
                });
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
                            .id(CustomUUID.customUUIDGenerator())
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
                            .id(CustomUUID.customUUIDGenerator())
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
                            .id(CustomUUID.customUUIDGenerator())
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
    public Mono<Quiz> startQuiz(String id) {
        return this.quizRepository.findById(id)
                .switchIfEmpty(Mono.error(new Throwable("There is not " +
                        "quiz with id: " + id)))
                .map(quiz -> mapper.map(quiz, Quiz.class))
                .filter(quiz -> {
                    LocalDateTime createdAt=quiz.getCreatedAt();
                    LocalDateTime now=LocalDateTime.now();
                    Duration duration=Duration.between(createdAt, now);
                    return duration.toHours() < 24;
                })
                .switchIfEmpty(
                        Mono.error(
                                new Throwable (
                                        "Quiz with id "
                                                + id +
                                                " was created more than 24 hours ago and cannot be modified." +
                                                "please ask for another code"))
                )
                .filter(quiz -> !quiz.getStatus().equalsIgnoreCase(Status.STARTED.name()) )
                .switchIfEmpty(
                        Mono.error(
                                new Throwable(
                                        "Quiz with id "
                                         + id +
                                         " has already been started"))
                        )
                .flatMap(quiz1 -> {
                    quiz1.setStartedAt(LocalDateTime.now());
                    quiz1.setStatus(Status.STARTED.name());
                    return this.quizRepository.save(mapper.map(quiz1, QuizData.class));
                })
                .map(quiz2 -> mapper.map(quiz2, Quiz.class));
    }

    @Override
    public Mono<Void> deleteAll() {
        return this.quizRepository.deleteAll()
                .onErrorResume(Mono::error);
    }

}
