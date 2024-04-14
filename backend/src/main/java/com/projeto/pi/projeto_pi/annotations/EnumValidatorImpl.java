package com.projeto.pi.projeto_pi.annotations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.ConstraintValidatorContext;

public class EnumValidatorImpl implements jakarta.validation.ConstraintValidator<EnumValidator, String> {

    private List<String> acceptedvalues;

    @Override
    public void initialize(EnumValidator annotation) {
        acceptedvalues = Stream.of(annotation.enumclass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        // context.disableDefaultConstraintViolation();
        // context.buildConstraintViolationWithTemplate("Invalid value").addConstraintViolation();

        return acceptedvalues.contains(value.toString());
    }

}