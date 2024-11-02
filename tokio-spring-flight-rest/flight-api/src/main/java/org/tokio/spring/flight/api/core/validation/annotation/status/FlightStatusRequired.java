package org.tokio.spring.flight.api.core.validation.annotation.status;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.lang.NonNull;
import org.tokio.spring.flight.api.core.validation.annotation.status.impl.FlightStatusRequiredValidationImpl;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FlightStatusRequiredValidationImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlightStatusRequired {

    @NonNull Class<? extends Enum<?>> target(); // Especifica el enum que deseas validar.
    boolean required() default true;

    String message() default "Invalid status flight.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
