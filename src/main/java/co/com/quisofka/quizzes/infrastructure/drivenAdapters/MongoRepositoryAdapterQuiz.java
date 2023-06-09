package co.com.quisofka.quizzes.infrastructure.drivenAdapters;


import co.com.quisofka.quizzes.domain.model.question.Question;
import co.com.quisofka.quizzes.domain.model.quiz.Quiz;
import co.com.quisofka.quizzes.domain.model.student.enums.Level;
import co.com.quisofka.quizzes.domain.model.quiz.enums.Status;
import co.com.quisofka.quizzes.domain.model.quiz.gateways.QuizRepositoryGateway;
import co.com.quisofka.quizzes.domain.model.student.Student;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                    List<List<Object>> quizQuestions = questionList.stream()
                            .map(question -> Arrays.asList((Object) question.getId(), false))
                            .collect(Collectors.toList());
                    /*Map<String, Boolean> quizQuestions=questionList.stream()
                            .collect(Collectors.toMap(Question::getId, q -> false));*/
                    Quiz newQuiz=Quiz.builder()
                            .id(CustomUUID.customUUIDGenerator())
                            .questionList(questionList)
                            .questions(quizQuestions)
                            .studentId(quiz.getStudentId())
                            .createdAt(LocalDateTime.now())
                            .status(Status.GENERATED.name())
                            //the next two lines are just to prove the thing of one hour
                            //.startedAt(LocalDateTime.now().minusHours(2))
                            //.startedAt(LocalDateTime.now())
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
                    List<List<Object>> quizQuestions = questionList.stream()
                            .map(question -> Arrays.asList((Object) question.getId(), false))
                            .collect(Collectors.toList());
                    Quiz newQuiz=Quiz.builder()
                            .id(CustomUUID.customUUIDGenerator())
                            .questionList(questionList)
                            .questions(quizQuestions)
                            .studentId(quiz.getStudentId())
                            .createdAt(LocalDateTime.now())
                            //the next two lines are just to prove the thing of one hour
                            //.startedAt(LocalDateTime.now().minusHours(2))
                            //.startedAt(LocalDateTime.now())
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
                    List<List<Object>> quizQuestions = questionList.stream()
                            .map(question -> Arrays.asList((Object) question.getId(), false))
                            .collect(Collectors.toList());
                    Quiz newQuiz=Quiz.builder()
                            .id(CustomUUID.customUUIDGenerator())
                            .questionList(questionList)
                            .questions(quizQuestions)
                            .studentId(quiz.getStudentId())
                            .createdAt(LocalDateTime.now())
                            //the next two lines are just to prove the thing of one hour
                            //.startedAt(LocalDateTime.now().minusHours(2))
                            //.startedAt(LocalDateTime.now())
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
                .filter(quiz -> !quiz.getStatus().equalsIgnoreCase(Status.STARTED.name()))
                .switchIfEmpty(
                        Mono.error(
                                new Throwable(
                                        "Quiz with id "
                                         + id +
                                         " has already been started"))
                        )
                .filter(quiz -> !quiz.getStatus().equalsIgnoreCase(Status.FINISHED.name()))
                .switchIfEmpty(
                        Mono.error(
                                new Throwable(
                                        "Quiz with id "
                                                + id +
                                                " has already been finished"))
                )
                .flatMap(quiz1 -> {
                    quiz1.setStartedAt(LocalDateTime.now());
                    quiz1.setStatus(Status.STARTED.name());
                    return this.quizRepository.save(mapper.map(quiz1, QuizData.class));
                })
                .map(quiz2 -> mapper.map(quiz2, Quiz.class));
    }

    @Override
    public Mono<Quiz> submitQuiz(String id, Quiz quiz) {
        return this.quizRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("There is not " +
                        "quiz with id: " + id)))
                .flatMap(quizData -> {
                    if (quizData.getStatus().equals("FINISHED")){
                        return Mono.error(new IllegalArgumentException("The quiz can no longer be submitted, " +
                                "it was already submitted"));
                    }
                    else {
                        LocalDateTime now = LocalDateTime.now();
                        Duration duration = Duration.between(quizData.getStartedAt(), now);
                        long hours = duration.toHours();
                        if (hours > 1){
                            return this.quizRepository.deleteById(id)
                                    .then(Mono.error(new IllegalArgumentException("The quiz can no longer be submitted, " +
                                            "you spend more than 1 hour on it.")));
                        }else{
                            quiz.setId(quizData.getId());
                            Double newResult = quiz.getQuestions().stream()
                                    .mapToDouble(list -> (boolean)list.get(1) ? 2 : 0)
                                    .sum();
                            quiz.setScore(newResult);
                            quiz.setStatus(Status.FINISHED.name());
                            System.out.println("student operation");

                            this.studentRepository.findById(quiz.getStudentId())
                                    .switchIfEmpty(Mono.error( new Throwable("Student not found")))
                                    .map(studentData -> {
                                        if (quiz.getScore() < 26) {
                                            studentData.setIsAuthorized(false);
                                            this.studentRepository.save(studentData).subscribe();
                                        }

                                        System.out.println("student operation");

                                        if (quiz.getScore() >= 26) {
                                            switch (studentData.getLevel().toUpperCase()) {
                                                case "PENDING" -> studentData.setLevel(Level.INITIAL.name());
                                                case "INITIAL" -> studentData.setLevel(Level.BASIC.name());
                                                case "BASIC" -> studentData.setLevel(Level.INTERMEDIATE.name());
                                            }
                                            this.studentRepository.save(studentData).subscribe();
                                        }
                                        return studentData;
                                    }).subscribe();
                            return quizRepository.save(mapper.map(quiz, QuizData.class));
                        }
                    }
                })
                .map(quizData -> mapper.map(quizData, Quiz.class));
    }







    @Override
    public Mono<Void> deleteQuizById(String id) {
        return this.quizRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("There is not " +
                        "quiz with id: " + id)))
                .flatMap(studentData -> this.quizRepository.deleteById(studentData.getId()));
    }

    @Override
    public Mono<Void> deleteAll() {
        return this.quizRepository.deleteAll()
                .onErrorResume(Mono::error);
    }

}
