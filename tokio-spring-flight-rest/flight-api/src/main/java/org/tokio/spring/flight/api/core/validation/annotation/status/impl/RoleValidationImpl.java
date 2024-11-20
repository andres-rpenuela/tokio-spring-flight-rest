package org.tokio.spring.flight.api.core.validation.annotation.status.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.tokio.spring.flight.api.core.validation.annotation.status.RoleValid;

import java.util.Arrays;
import java.util.List;

public class RoleValidationImpl implements ConstraintValidator<RoleValid, List<String>>{
    private List<String> entries;
    private boolean isRequired;

    @Override
    public void initialize(RoleValid constraintAnnotation) {
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
    public boolean isValid(List<String> value, ConstraintValidatorContext constraintValidatorContext) {
        // se valida el objeto
        if(!this.isRequired){ // no valida, por lo que es true
            return true;
        }

        // se valida el valor del objeto de tipo enum
        if(value == null || value.isEmpty() ){
            return false; // No permite nulos o valores vacios
        }
        // Validation
        final List<String> trimAndUpperSource = value.stream()
                .map(StringUtils::stripToNull)
                .map(String::toUpperCase)
                .toList();

        // comprueba que el valor este dentro del enum a validar
        boolean isValid = true;
        for(String source : trimAndUpperSource){
            if (!trimAndUpperSource.contains(source)) {
                isValid = false;
                break;
            }
        }

        return isValid;
    }
}
