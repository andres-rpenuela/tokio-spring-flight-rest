package org.tokio.spring.email.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class EmailDTO {

    String from;
    String to;
    String subject;
    String textBody;
    List<AttachmentDTO> attachments;
}
