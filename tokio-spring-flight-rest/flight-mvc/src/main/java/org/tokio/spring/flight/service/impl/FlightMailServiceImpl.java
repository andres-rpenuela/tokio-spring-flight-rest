package org.tokio.spring.flight.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.email.dto.AttachmentDTO;
import org.tokio.spring.email.dto.EmailDTO;
import org.tokio.spring.email.service.EmailService;
import org.tokio.spring.flight.dto.FlightDTO;
import org.tokio.spring.flight.service.FlightMailService;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightMailServiceImpl implements FlightMailService {

    private final EmailService emailService;
    private final MessageSource messageSource;

    private static final String KEY_SUBJECT_MAIL_I18N = "flight.mail.subject";
    private static final String KEY_BODY_MAIL_I18N = "flight.mail.body";

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public EmailDTO sendMailFlightSimple(FlightDTO flightDTO, String to) {
        // multilanguage
        final Locale localeSpanish = new Locale("es","ES");
        final String subjectEs = buildSubject(localeSpanish,flightDTO.getNumber());
        final String subjectEn = buildSubject(Locale.ENGLISH,flightDTO.getNumber());
        final String subject = String.format("%s / %s",subjectEs,subjectEn);

        final String boydEs = buildBody(localeSpanish,flightDTO);
        final String bodyEn = buildBody(Locale.ENGLISH,flightDTO);
        final String body = String.format("%n%s%n--------------------------------------%n%s",boydEs,bodyEn);


        final EmailDTO emailDTO = EmailDTO.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .textBody(body)
                .build();
        emailService.sendEmailBasic(emailDTO);
        return emailDTO;
    }

    @Override
    public EmailDTO sendMailFlightWithAttachment(FlightDTO flightDTO, String to, MultipartFile... multipartFiles) {
        // multilanguage
        final Locale localeSpanish = new Locale("es","ES");
        final String subjectEs = buildSubject(localeSpanish,flightDTO.getNumber());
        final String subjectEn = buildSubject(Locale.ENGLISH,flightDTO.getNumber());
        final String subject = String.format("%s / %s",subjectEs,subjectEn);

        final String boydEs = buildBody(localeSpanish,flightDTO);
        final String bodyEn = buildBody(Locale.ENGLISH,flightDTO);
        final String body = String.format("%n%s%n--------------------------------------%n%s",boydEs,bodyEn);

        final List<AttachmentDTO> attachmentDTOList = buildAttachment(multipartFiles) ;

        final EmailDTO emailDTO = EmailDTO.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .textBody(body)
                .attachments(attachmentDTOList)
                .build();
        emailService.sendEmailWithAttachment(emailDTO);
        return emailDTO;
    }


    private String buildSubject(Locale locale,Object... args) {
        return messageSource.getMessage(KEY_SUBJECT_MAIL_I18N, args, locale);
    }

    private <T extends FlightDTO> String buildBody(Locale locale, T flight) {

        final NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);

        DateTimeFormatter dateTimeFormatter =
                DateTimeFormatter.ofPattern("EEEE MM yyyy HH:mm").withLocale(locale);

        long departureTimeAtMinutes = ChronoUnit.MINUTES.between(flight.getDepartureTime(), LocalDateTime.now());

        final String[] bodyArgs = {
                flight.getNumber(),
                flight.getAirportDepartureAcronym(),
                flight.getAirportDepartureAcronym(),
                dateTimeFormatter.format(flight.getDepartureTime()),
                numberFormat.format(departureTimeAtMinutes)
        };

        return messageSource.getMessage(KEY_BODY_MAIL_I18N,bodyArgs,locale);
    }


    private List<AttachmentDTO> buildAttachment(MultipartFile[] multipartFiles) {

        if(multipartFiles != null && multipartFiles.length > 0) {
            List<AttachmentDTO> attachmentDTOList = new ArrayList<>();
            Arrays.stream(multipartFiles)
                    .filter(Predicate.not(MultipartFile::isEmpty))
                    .forEach(multipartFile -> {
                        try {
                            final AttachmentDTO attachmentDTO = convertToAttachment(multipartFile);
                            attachmentDTOList.add(attachmentDTO);
                        } catch (IOException e) {
                            log.error("Error al procesar el attacment: {}, se ignora.",multipartFile.getName(),e);
                        }
                    });
            return attachmentDTOList;
        } else {
            log.debug("Don't attachment for proccesing.");
            return List.of();
        }
    }

    private AttachmentDTO convertToAttachment(MultipartFile multipartFile) throws IOException {
        return AttachmentDTO.builder()
                .filename(multipartFile.getName())
                .contentType(multipartFile.getContentType())
                .content(multipartFile.getBytes()).build();
    }
}
