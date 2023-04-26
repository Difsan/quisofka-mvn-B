package co.com.quisofka.quizzes.infrastructure.drivenAdapters.util.validators;

import co.com.quisofka.quizzes.domain.model.student.enums.Level;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class LevelEnumValidator implements ConstraintValidator<LevelEnum, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Arrays.stream(Level.values())
                .map(Level::name)
                .anyMatch(name -> name.equalsIgnoreCase(value));
    }
}
