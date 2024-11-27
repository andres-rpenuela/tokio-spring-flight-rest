package org.tokio.spring.flight.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tokio.spring.flight.domain.Flight;
import org.tokio.spring.flight.domain.STATUS_FLIGHT;
import org.tokio.spring.flight.dto.AccountCancelledFlightsBuAirportDto;
import org.tokio.spring.flight.dto.AccountFlightDto;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightDao extends JpaRepository<Flight,Long> {

    // NAME QUERIES
    String FIND_ALL_FLIGHTS_BY_AIRPORT_ACRONYM = "FIND_ALL_FLIGHTS_BY_AIRPORT_ACRONYM";
    String GET_ACCOUNT_FLIGHTS_BY_AIRPORT_DEPARTURE_ACRONYM = "GET_ACCOUNT_FLIGHTS_BY_AIRPORT_DEPARTURE_ACRONYM";
    String GET_ACCOUNT_FLIGHTS_BY_AIRPORT_ARRIVAL_ACRONYM = "GET_ACCOUNT_FLIGHTS_BY_AIRPORT_ARRIVAL_ACRONYM";
    String GET_ACCOUNT_FLIGHTS_BY_AIRPORT_DEPARTURE_ACRONYM_AND_STATUS_CANCELLED = "GET_ACCOUNT_FLIGHTS_BY_AIRPORT_DEPARTURE_ACRONYM_AND_STATUS_CANCELLED";

    // QUERIES
    @Query(name=FIND_ALL_FLIGHTS_BY_AIRPORT_ACRONYM,value = "SELECT f FROM Flight f where UPPER(f.airportDeparture.acronym) like (UPPER(?1)) or UPPER(f.airportArrival.acronym) like (UPPER(?1))")
    List<Flight> findAllByAirportAcronym(String acronym);

    List<Flight> findAllByAirportDepartureAcronymContainsIgnoreCase(String acronym);
    List<Flight> findAllByAirportArrivalAcronymContainsIgnoreCase(String acronym);


    @Query(name=GET_ACCOUNT_FLIGHTS_BY_AIRPORT_DEPARTURE_ACRONYM,value="SELECT f.id as id ,f.number as number ,f.airportDeparture.acronym as airportDepartureAcronym," +
            "f.airportArrival.acronym as airportArrivalAcronym, count(1) as account FROM Flight f GROUP BY f.airportDeparture.acronym")
    List<AccountFlightDto> getAccountFlightsByAirportDeparture( );

    @Query(name=GET_ACCOUNT_FLIGHTS_BY_AIRPORT_ARRIVAL_ACRONYM,value="SELECT f.id as id ,f.number as number ,f.airportDeparture.acronym as airportDepartureAcronym," +
            "f.airportArrival.acronym as airportArrivalAcronym, count(1) as account FROM Flight f GROUP BY f.airportArrival.acronym")
    List<AccountFlightDto> getAccountFlightsByAirportArrival( );


    List<Flight> findFlightsByDepartureTimeIsAfterAndStatusFlightIs(LocalDateTime departureTimeForm, STATUS_FLIGHT statusFlight);

    List<Flight> findByNumberLike(String number);
    List<Flight> findByNumberContains(String number);

    Page<Flight> findAll(Pageable pageable);

    @Query(name=GET_ACCOUNT_FLIGHTS_BY_AIRPORT_DEPARTURE_ACRONYM_AND_STATUS_CANCELLED,value="SELECT f.id as id ,f.number as number ,f.airportDeparture.acronym as airportDepartureAcronym," +
            "f.airportArrival.acronym as airportArrivalAcronym, count(1) as account FROM Flight f WHERE f.statusFlight = 'CANCELLED' GROUP BY f.airportDeparture.acronym")
    List<AccountFlightDto> findAllByAirportAcronymAndStatusCancelled( );

    // JQPL Query: Numero de aeropuestos de salida/partida cancelados con parametro de entrada
    @Query("""
        SELECT new org.tokio.spring.flight.dto.AccountCancelledFlightsBuAirportDto(
            f.airportDeparture.acronym AS acronym, COUNT(1) AS counter )
        FROM Flight f 
        WHERE f.statusFlight = :status GROUP BY f.airportDeparture.acronym
        """)
    List<AccountCancelledFlightsBuAirportDto> getFlightDepartureCancelledCounter(@Param("status") STATUS_FLIGHT status);

    // Native JQPL Query: Numero de aeropuestos de llegada cancelados con parametro de entrada
    @Query(value = """
        SELECT a.acronym AS acronym, COUNT(1) AS counter
        FROM flights f 
        INNER JOIN airports a ON f.airport_arrival_id = a.acronym
        WHERE f.status = :status GROUP BY f.airport_arrival_id
        """,
        nativeQuery = true)
    List<AccountCancelledFlightsBuAirportDto> getFlightArrivalCancelledCounter(@Param("status") STATUS_FLIGHT status);

    // Modifying
    // Modica el number (UUID) del vuelo mediante consulta JQPL
    // Para que el cambio se vea refejado en la misma sesión de
    // persiencia, se ha de forzar la limpieza la cache
    @Query(
            value = """
                UPDATE Flight f 
                SET f.number = :number 
                WHERE f.id = :id 
            """)
    @Modifying(clearAutomatically = true)
    int updateFlightNumber(@Param("id") Long flightId, @Param("number") String number);
}
