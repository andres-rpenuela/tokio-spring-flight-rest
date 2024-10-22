package org.tokio.spring.common.core.advice;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class) // aplica a todas las excepcions de RestController
public class APIExceptionHandler {
}
