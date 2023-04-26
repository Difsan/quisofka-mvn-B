package co.com.quisofka.quizzes.domain.model.student;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Student {

    private String id;
    private String name;
    private String lastName;
    private String email;
    private Boolean isAuthorized;

    // it is a enum
    private String level;
}
