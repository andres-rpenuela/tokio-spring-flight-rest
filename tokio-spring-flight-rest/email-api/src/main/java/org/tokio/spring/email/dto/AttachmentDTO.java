package org.tokio.spring.email.dto;

import lombok.Builder;

@Builder
public record AttachmentDTO(String filename, String contentType, byte[] content) {
}
