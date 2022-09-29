package ai.obvs.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileNumberValidator implements ConstraintValidator<MobileNumber, Long> {

    protected long number;

    @Override
    public void initialize(MobileNumber constraintAnnotation) {
        this.number = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Pattern p = Pattern.compile("^[0-9]{10}$");
        if (value == null) {
            return false;
        }
        Matcher m = p.matcher(String.valueOf(value));
        if(m.matches()){
            System.out.println("Matches");
            return true;
        }
        return false;
    }
}
