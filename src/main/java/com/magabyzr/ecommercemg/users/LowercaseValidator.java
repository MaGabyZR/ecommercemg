package com.magabyzr.ecommercemg.users;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LowercaseValidator implements ConstraintValidator<Lowercase,String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return true;                                                             //because strings are nullable and lowercase does not apply to null objects.
        return value.equals(value.toLowerCase());
    }
}
