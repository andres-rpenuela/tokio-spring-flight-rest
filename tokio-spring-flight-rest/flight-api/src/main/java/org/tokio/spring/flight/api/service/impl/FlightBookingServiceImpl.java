package org.tokio.spring.flight.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tokio.spring.flight.api.core.exception.FlightException;
import org.tokio.spring.flight.api.core.exception.OverBookingException;
import org.tokio.spring.flight.api.domain.Flight;
import org.tokio.spring.flight.api.domain.FlightBooking;
import org.tokio.spring.flight.api.domain.User;
import org.tokio.spring.flight.api.dto.FlightBookingDTO;
import org.tokio.spring.flight.api.report.BookingReport;
import org.tokio.spring.flight.api.report.FlightReport;
import org.tokio.spring.flight.api.report.UserReport;
import org.tokio.spring.flight.api.service.FlightBookingService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FlightBookingServiceImpl implements FlightBookingService {

    private final FlightReport flightReport;
    private final UserReport userReport;
    private final BookingReport bookingReport;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public FlightBookingDTO newBookingFlight(Long flightId, String userId) throws FlightException, OverBookingException {
        final Flight flight = flightReport.findById(flightId)
                .orElseThrow(()-> new FlightException("Flight with id: %s not found!".formatted(flightId)));

        final User user = userReport.findById(userId)
                .orElseThrow(()-> new FlightException("User with id: %s not found!".formatted(userId)));

        if(flight.getOccupancy() >= flight.getCapacity()){
            throw new OverBookingException("Flight with id:%s without free places".formatted(flightId));
        }

        final FlightBooking flightBooking = FlightBooking.builder()
                .locator(UUID.randomUUID())
                .flight(flight)
                .user(user).build();

        flight.setOccupancy(flight.getOccupancy() + 1);

        if(Objects.isNull(flight.getFlightBookings())){
            flight.setFlightBookings(new HashSet<>());
        }
        flight.getFlightBookings().add(flightBooking);

        flightReport.save(flight); // create in cascade
        //bookingReport.save(flightBooking);

        return modelMapper.map(flightBooking, FlightBookingDTO.class);
    }

    @Override
    public Set<FlightBookingDTO> findAllBooking() {
        return Set.of();
    }

    @Override
    public List<FlightBookingDTO> searchBookingsByFlightId(Long flightId) throws FlightException {
        List<FlightBookingDTO> flightBookingDTOS = new ArrayList<>();
        List<FlightBooking> flightBookings = Optional.ofNullable(flightId)
                        .map(bookingReport::findByFlightId).orElseThrow(()->new FlightException("Flight with id: %s not found!".formatted(flightId)));

        flightBookings.stream().forEach(flightBooking -> flightBookingDTOS.add(modelMapper.map(flightBooking, FlightBookingDTO.class)));
        return flightBookingDTOS;
    }


}
