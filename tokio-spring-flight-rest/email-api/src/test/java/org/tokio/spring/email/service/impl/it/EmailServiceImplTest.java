package org.tokio.spring.email.service.impl.it;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.tokio.spring.email.dto.EmailDTO;
import org.tokio.spring.email.service.EmailService;

@SpringBootTest
@ActiveProfiles("test")
class EmailServiceImplTest {

    @Autowired
    private EmailService emailService;

    @SpyBean    // Gestinado por Spring
    private JavaMailSender mailSender;


    private static final String EMAIL_USER = "andresruizpenuela@gmail.com";
    @Test
    void givenEmailDTO_whenSendEmailSimple_thenOk(){
        final EmailDTO emailDTOMock = buildEmailSimpleDTOMock();
        emailService.sendEmailBasic(emailDTOMock);

        ArgumentCaptor<SimpleMailMessage> messageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(mailSender,Mockito.times(1)).send(messageArgumentCaptor.capture());

        Assertions.assertThat(messageArgumentCaptor.getValue())
                .returns(new String[]{emailDTOMock.getTo()},SimpleMailMessage::getTo)
                .returns(emailDTOMock.getFrom(),SimpleMailMessage::getFrom)
                .returns(emailDTOMock.getSubject(),SimpleMailMessage::getSubject);

    }


    public EmailDTO buildEmailSimpleDTOMock(){
        return EmailDTO.builder()
                .to(EMAIL_USER)
                .from(EMAIL_USER)
                .subject("email de pruebas")
                .textBody("Hola desde Mail Service!")
                .build();
    }

}