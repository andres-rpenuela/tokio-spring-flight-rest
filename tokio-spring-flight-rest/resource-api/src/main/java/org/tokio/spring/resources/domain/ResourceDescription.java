package org.tokio.spring.resources.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ResourceDescription {
    @NonNull String resourceName;
    @NonNull String resourceType;
    String description;
    int size;
}
