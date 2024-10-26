package org.tokio.spring.resources.dto;

import lombok.NonNull;

import java.util.UUID;

public record ResourceIdDTO(@NonNull UUID resourceId) {
}
