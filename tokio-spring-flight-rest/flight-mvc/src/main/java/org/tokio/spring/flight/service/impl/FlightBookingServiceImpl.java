package org.tokio.spring.flight.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tokio.spring.flight.domain.Flight;
import org.tokio.spring.flight.domain.FlightBooking;
import org.tokio.spring.flight.domain.User;
import org.tokio.spring.flight.dto.FlightBookingDTO;
import org.tokio.spring.flight.exception.OverBookingException;
import org.tokio.spring.flight.repository.FlightBookingDao;
import org.tokio.spring.flight.repository.FlightDao;
import org.tokio.spring.flight.repository.UserDao;
import org.tokio.spring.flight.service.FlightBookingService;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightBookingServiceImpl implements FlightBookingService {

    private final FlightDao flightDao;
    private final UserDao userDao;
    private final FlightBookingDao flightBookingDao;

    private final ModelMapper modelMapper;

    @Override
    @Transactional // aplica @version en Flight, locking optimistic > no bloque el acceso solo la modificiaon si cambia la version
    //@Transactional(isolation = Isolation.SERIALIZABLE) // locking pessimic > bloquea el registro y no puede ser uitlizado por otra query
    public FlightBookingDTO newBookingFlight(Long flightId, String userId) throws IllegalArgumentException, OverBookingException {
        final Flight flight = flightDao.findById(flightId)
                .orElseThrow(()-> new IllegalArgumentException("Flight with id: %s not found!".formatted(flightId)));

        final User user = userDao.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("User with id: %s not found!".formatted(userId)));

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

        flightDao.save(flight);

        return modelMapper.map(flightBooking, FlightBookingDTO.class);
    }

    public Set<FlightBookingDTO> findAllBooking(){

        return flightBookingDao.findAll().stream()
                .map(flightBooking -> modelMapper.map(flightBooking, FlightBookingDTO.class))
                .collect(Collectors.toSet());
    }

}
