package org.tokio.spring.email.service;

import org.tokio.spring.email.dto.EmailDTO;

public interface EmailService {

    void sendEmailBasic(EmailDTO emailDTO);
    void sendEmailWithAttachment(EmailDTO emailDTO);
}
