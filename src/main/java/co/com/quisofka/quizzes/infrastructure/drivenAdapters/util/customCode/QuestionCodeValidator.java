package co.com.quisofka.quizzes.infrastructure.drivenAdapters.util.customCode;

import java.security.SecureRandom;
import java.util.Arrays;


public class QuestionCodeValidator {

    private final SecureRandom numberGenerator = new SecureRandom();


    /**
     * Method to validate if the string is valid for the given condition:
     * "Letters do not repeat each other more than 2 times"
     * @param strings list of strings
     * @return boolean indicating whether the string list is valid
     */
    public Boolean stringValidator(char... strings) {

        Arrays.sort(strings);

        if (strings[0]==(strings[1])){
            if (strings[1]==(strings[2])){
                return false;
            }
        }
        return true;
    }


    /**
     * Method to validate if the string of numbers is valid for the given condition:
     * "Numbers do not repeat each other more than 3 times"
     * @param integers list of integers
     * @return boolean indicating whether the integers list is valid
     */
    public Boolean numbersValidator(char... integers) {

        Arrays.sort(integers);

        if (integers[0]==integers[1]){
            if (integers[1]==integers[2]){
                if (integers[2]==integers[3]){
                    return false;
                }
            }
        }
        return true;
    }


}
