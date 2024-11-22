package org.tokio.spring.flight.api.dto;

import org.springframework.validation.ObjectError;

import java.util.List;

public record ResponseDTO<T>(List<ObjectError> errors,T data) {
}
