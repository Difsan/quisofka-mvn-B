package co.com.quisofka.quizzes.domain.model.student.gateways;

import co.com.quisofka.quizzes.domain.model.student.Student;
import reactor.core.publisher.Mono;

public interface StudentRepository {

    Mono<Student> getStudentById(String studentId);

    Mono<Student> getStudentByEmail(String email);

    Mono<Student> saveStudent(Student student);

    //Mono<Boolean> isFieldUnique(String email);

    Mono<Student> updateStudent(String studentId, Student student);

    Mono<Void> deleteStudent(String studentId);
}
