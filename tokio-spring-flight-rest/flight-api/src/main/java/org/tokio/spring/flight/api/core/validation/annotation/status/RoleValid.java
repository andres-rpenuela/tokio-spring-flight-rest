package org.tokio.spring.flight.api.core.validation.annotation.status;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.lang.NonNull;
import org.tokio.spring.flight.api.core.validation.annotation.status.impl.RoleValidationImpl;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleValidationImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleValid {
    @NonNull Class<? extends Enum<?>> target(); // Especifica la lista que se desea valida
    boolean required() default true;

    String message() default "Valor inválido. Debe coincidir con uno de los valores permitidos del enum";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
