package org.tokio.spring.flight.api.core.validation.annotation.status.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.tokio.spring.flight.api.core.validation.annotation.status.FlightStatusExists;

import java.util.stream.Stream;

public class FlightStatusExistsValidationImpl implements ConstraintValidator<FlightStatusExists, String>{

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(FlightStatusExists constraintAnnotation) {
        this.enumClass = constraintAnnotation.target(); // recupera la clase del objecto a validar, recidibo por el atributo "target" de la anotacion
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true; // Permite nulos; usa @NotNull si quieres evitar esto
        }

        // Verifica si el valor proporcionado existe en el enum.
        return Stream.of(enumClass.getEnumConstants())
                .anyMatch(e -> e.name().equals(value));
    }
}
