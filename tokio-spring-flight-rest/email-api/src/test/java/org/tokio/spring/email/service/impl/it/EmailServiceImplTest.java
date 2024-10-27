package org.tokio.spring.email.service.impl.it;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.tokio.spring.email.dto.AttachmentDTO;
import org.tokio.spring.email.dto.EmailDTO;
import org.tokio.spring.email.service.EmailService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(messageArgumentCaptor.getValue())
                .returns(new String[]{emailDTOMock.getTo()},SimpleMailMessage::getTo)
                .returns(emailDTOMock.getFrom(),SimpleMailMessage::getFrom)
                .returns(emailDTOMock.getSubject(),SimpleMailMessage::getSubject);

    }

    @Test
    void givenEmailDTOWithAttachment_whenSendEmailWithAttachment_thenOk() throws IOException, AddressException, AddressException {
        final EmailDTO emailDTOMock = buildEmailWithAttachmentDTOMock();
        emailService.sendEmailWithAttachment(emailDTOMock);

        ArgumentCaptor<MimeMessage> messageArgumentCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        Mockito.verify(mailSender,Mockito.times(1)).send(messageArgumentCaptor.capture());


        assertThat(messageArgumentCaptor.getValue())
                // email to
                .returns(  new InternetAddress[]{ new InternetAddress(emailDTOMock.getTo()) } , mimeMessage -> {
                    try {
                        return mimeMessage.getRecipients(Message.RecipientType.TO);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                })
                // email from
                .returns( new InternetAddress[]{ new InternetAddress(emailDTOMock.getFrom()) },mimeMessage -> {
                    try {
                        return mimeMessage.getFrom();
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                })
                // email subject
                .returns(emailDTOMock.getSubject(),mimeMessage -> {
                    try {
                        return mimeMessage.getSubject();
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                })
                // email body of type multipart with two part (text+attachment)
                .satisfies(mimeMessage -> {
                    assertThat(mimeMessage.getContent()).isInstanceOf(MimeMultipart.class)
                            .satisfies(mimeMultipart ->  assertThat( ((MimeMultipart)mimeMultipart).getCount())
                                    .isEqualTo(2));

                });

    }


    public EmailDTO buildEmailSimpleDTOMock(){
        return EmailDTO.builder()
                .to(EMAIL_USER)
                .from(EMAIL_USER)
                .subject("email de pruebas")
                .textBody("Hola desde Mail Service!")
                .build();
    }

    public EmailDTO buildEmailWithAttachmentDTOMock() throws IOException {

        return EmailDTO.builder()
                .to("andresruizpenuela@gmail.com")
                .from("andresruizpenuela@gmail.com")
                .subject("email de pruebas")
                .textBody("Hola desde Mail Service!")
                .attachments(buildAttachmentDTOMock())
                .build();
    }

    public List<AttachmentDTO> buildAttachmentDTOMock() throws IOException {
        final String relativePathFileWithFullName = "static/images/image-default.jpg";

        final File attachmentFile = new File(EmailServiceImplTest.class.getClassLoader()
                .getResource(relativePathFileWithFullName).getFile());

        final byte[] contentAttachment = Files.readAllBytes(attachmentFile.toPath());

        final AttachmentDTO attachmentDTO = AttachmentDTO.builder()
                .contentType("image/jpeg")
                .filename("image-default.jpg")
                .content(contentAttachment).build();

        return List.of(attachmentDTO);
    }


}