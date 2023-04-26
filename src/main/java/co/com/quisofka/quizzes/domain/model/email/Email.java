package co.com.quisofka.quizzes.domain.model.email;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Email {

    String to;
    String messageSubject;
    String studentName;
}
