package org.tokio.spring.flight.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ResourceDTO {
    private Long id;
    private UUID resourceId;
    private String contentType;
    private int size;
    private String filename;
    private byte[] content;
}
