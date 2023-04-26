package co.com.quisofka.quizzes.infrastructure.drivenAdapters.util.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LevelEnumValidator.class)
public @interface LevelEnum {

    String message() default "Invalid level value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
