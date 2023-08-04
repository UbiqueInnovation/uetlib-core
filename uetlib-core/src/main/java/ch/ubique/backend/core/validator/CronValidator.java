package ch.ubique.backend.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.quartz.CronExpression;

public class CronValidator implements ConstraintValidator<ValidCron, String> {

    @Override
    public boolean isValid(
            final String expression, final ConstraintValidatorContext constraintValidatorContext) {
        return expression == null || CronExpression.isValidExpression(expression);
    }
}
