package org.tokio.spring.email.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.tokio.spring.email.dto.EmailDTO;
import org.tokio.spring.email.service.EmailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    // auto config
    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmailBasic(EmailDTO emailDTO) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailDTO.getFrom());
        message.setTo(emailDTO.getTo());
        message.setSubject(emailDTO.getSubject());
        message.setText(emailDTO.getTextBody());

        javaMailSender.send(message);
    }
}
