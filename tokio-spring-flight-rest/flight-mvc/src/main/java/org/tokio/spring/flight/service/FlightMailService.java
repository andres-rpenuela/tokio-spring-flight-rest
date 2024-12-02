package org.tokio.spring.flight.service;

import org.springframework.web.multipart.MultipartFile;
import org.tokio.spring.email.dto.EmailDTO;
import org.tokio.spring.flight.dto.FlightDTO;

public interface FlightMailService {
    EmailDTO sendMailFlightSimple(FlightDTO flightDTO, String to);
    EmailDTO sendMailFlightWithAttachment(FlightDTO flightDTO, String to, MultipartFile... multipartFiles);
}
