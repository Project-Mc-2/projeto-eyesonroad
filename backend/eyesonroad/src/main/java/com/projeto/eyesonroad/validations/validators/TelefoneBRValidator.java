package com.projeto.eyesonroad.validations.validators;

import com.projeto.eyesonroad.validations.annotatios.TelefoneBR;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelefoneBRValidator implements ConstraintValidator<TelefoneBR, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        String regex = "^\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}$";

        return value.matches(regex);
    }
}