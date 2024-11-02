package org.tokio.spring.flight.api.core.validation.annotation.status.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.tokio.spring.flight.api.core.validation.annotation.status.FlightStatusRequired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FlightStatusRequiredValidationImpl implements ConstraintValidator<FlightStatusRequired, String>{

    private List<String> entries;
    private boolean isRequired;

    @Override
    public void initialize(FlightStatusRequired constraintAnnotation) {
        // determina si es requerido o no el attr., "required" de la anotacion, por defecto "true"
        isRequired = constraintAnnotation.required();

        // obtenemos la clase del enumerdoar y lo mappeamos a una lista
        final Class<? extends Enum<?>> enumTarget = constraintAnnotation.target();
        final Enum<?>[] enumValues = enumTarget.getEnumConstants();
        entries = Arrays.stream(enumValues)
                .map(Enum::name)
                .map(String::toUpperCase)
                .toList();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        // se valida el objeto
        if(!this.isRequired){ // no valida, por lo que es true
            return true;
        }

        // se valida el valor del objeto de tipo enum
        if(value == null){
            return false; // No permite nulos
        }
        // Validation
        final Optional<String> trimmedOpt = Optional.of(value)
                .map(StringUtils::stripToNull)
                .map(String::toUpperCase);

        // comprueba que el valor este dentro del enum a validar
        return trimmedOpt.filter(strValue -> this.entries.contains(strValue)).isPresent();
    }
}
