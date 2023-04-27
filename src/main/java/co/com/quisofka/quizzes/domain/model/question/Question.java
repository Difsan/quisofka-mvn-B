package co.com.quisofka.quizzes.domain.model.question;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Question {

    private String id;
    private String description;
    private List<List<Object>> answers;
    private String knowledgeArea;
    private String descriptor;
    //TODO: enums
    private String type;
    private String level;

}
