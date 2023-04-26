package co.com.quisofka.quizzes.infrastructure.drivenAdapters.util.customCode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@RequiredArgsConstructor
@Component
public class CustomUUID {

    private final static SecureRandom numberGenerator = new SecureRandom();
    private final static QuestionCodeValidator validator = new QuestionCodeValidator();

    private static String randomUUIDString() {

        //nextInt(upperbound-lowerbound) + lowerbound;
        String generatedNumber1 =  String.valueOf((char)(numberGenerator.nextInt(25)+65));
        String generatedNumber2 =  String.valueOf((char)(numberGenerator.nextInt(25)+65));
        String generatedNumber3 =  String.valueOf((char)(numberGenerator.nextInt(25)+65));
        String generatedNumber4 =  String.valueOf((char)(numberGenerator.nextInt(25)+65));

        return generatedNumber1+generatedNumber2+generatedNumber3+generatedNumber4;
    }


    private static String randomUUIDNumber() {

        String generatedNumber1 =  Integer.toString(numberGenerator.nextInt(10));
        String generatedNumber2 =  Integer.toString(numberGenerator.nextInt(10));
        String generatedNumber3 =  Integer.toString(numberGenerator.nextInt(10));
        String generatedNumber4 =  Integer.toString(numberGenerator.nextInt(10));

        return generatedNumber1+generatedNumber2+generatedNumber3+generatedNumber4;
    }

    public static String customUUIDGenerator() {

        String firstGroup = randomUUIDString();
        String secondGroup = randomUUIDNumber();

        while (!validator.stringValidator(firstGroup.toCharArray())) {
            firstGroup = randomUUIDString();
        }
        while (!validator.numbersValidator(secondGroup.toCharArray())) {
            secondGroup = randomUUIDNumber();
        }
        return firstGroup+"-"+secondGroup;
    }





}
