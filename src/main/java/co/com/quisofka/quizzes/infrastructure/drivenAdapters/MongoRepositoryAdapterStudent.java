package co.com.quisofka.quizzes.infrastructure.drivenAdapters;

import co.com.quisofka.quizzes.domain.model.student.Student;
import co.com.quisofka.quizzes.domain.model.student.gateways.StudentRepository;
import co.com.quisofka.quizzes.infrastructure.drivenAdapters.data.StudentData;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class MongoRepositoryAdapterStudent implements StudentRepository {

    private final MongoDBRepositoryStudent repository;

    private final ObjectMapper mapper;


    @Override
    public Mono<Student> getStudentById(String studentId) {
        return this.repository.findById(studentId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("There is not " +
                        "student with id: " + studentId)))
                .map(studentData -> mapper.map(studentData, Student.class));
    }

    @Override
    public Mono<Student> getStudentByEmail(String email) {
        return this.repository.findByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("There is not " +
                "student with email: " + email)))
                .map(studentData -> mapper.map(studentData, Student.class));
    }

    /*@Override
    public Mono<Student> saveStudent(Student student) {
        return this.repository
                .save(mapper.map(student, StudentData.class))
                .switchIfEmpty(Mono.empty())
                .map(studentData -> mapper.map(studentData, Student.class));
    }*/

    @Override
    public Mono<Student> saveStudent(Student student) {
        return Mono.just(student)
                .flatMap(student1 -> {
                    Student student2 = Student.builder()
                            .name(student1.getName())
                            .lastName(student1.getLastName())
                            .email(student1.getEmail())
                            .isAuthorized(student1.getIsAuthorized())
                            .level(student1.getLevel())
                            .build();
                    return this.repository.save(mapper.map(student2, StudentData.class));
                }).map(student3 -> mapper.map(student3, Student.class));
    }


    @Override
    public Mono<Student> updateStudent(String studentId, Student student) {
        return this.repository
                .findById(studentId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("There is not " +
                        "student with id: " + studentId)))
                .flatMap(studentData -> {
                    student.setId(studentData.getId());
                    return repository.save(mapper.map(student, StudentData.class));
                })
                .map(studentData -> mapper.map(studentData, Student.class));
    }

    @Override
    public Mono<Void> deleteStudent(String studentId) {
        return this.repository
                .findById(studentId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("There is not " +
                        "student with id: " + studentId)))
                .flatMap(studentData -> this.repository.deleteById(studentData.getId()));
    }
}
