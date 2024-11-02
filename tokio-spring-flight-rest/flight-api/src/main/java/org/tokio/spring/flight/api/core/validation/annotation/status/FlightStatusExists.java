package org.tokio.spring.flight.api.core.validation.annotation.status;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.tokio.spring.flight.api.core.validation.annotation.status.impl.FlightStatusExistsValidationImpl;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FlightStatusExistsValidationImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlightStatusExists {
    Class<? extends Enum<?>> target(); // Especifica el enum que deseas validar.

    String message() default "Valor inválido. Debe coincidir con uno de los valores permitidos del enum";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
