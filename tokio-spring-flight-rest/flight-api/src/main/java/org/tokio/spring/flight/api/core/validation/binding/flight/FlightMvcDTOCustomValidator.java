package org.tokio.spring.flight.api.core.validation.binding.flight;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.tokio.spring.flight.api.dto.FlightMvcDTO;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FlightMvcDTOCustomValidator extends CustomValidatorBean {
    /**
     * requiere exponer un bean
     * {@link org.tokio.spring.flight.api.core.validation.binding.LocalValidatorFactoryConfiguration#localValidatorFactoryBean() }
     */
    private final LocalValidatorFactoryBean localValidatorFactoryBean;

    @Override
    public boolean supports(Class<?> clazz) {
        return FlightMvcDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // dar soporte a otros validadores
        localValidatorFactoryBean.validate(target, errors);

        if(! FlightMvcDTO.class.isAssignableFrom(target.getClass())) {
            errors.rejectValue("flight", "flight.invalid", "Flight must be a FlightMvcDTO");
            return;
        }

        final FlightMvcDTO flightMvcDTO = (FlightMvcDTO) target;

        if(isAirportDepartureAcronymAndAirportArrivalAcronymSameAndNonNull(flightMvcDTO.getAirportDepartureAcronym(), flightMvcDTO.getAirportArrivalAcronym())) {
            errors.rejectValue(
                    "airportArrivalAcronym",
                    "validation.flight.arrival.equals_departure", // message i18n custom
                    "Arrival cannot be the same as Departure"
            );
        }
    }

    public static boolean isAirportDepartureAcronymAndAirportArrivalAcronymSameAndNonNull(final String airportDepartureAcronym,
                                                                             final String airportArrivalAcronym) {
        final String maybeArrivalAcronym = stripToNullAndUpperCase(airportArrivalAcronym);
        final String maybeDepartureAcronym = stripToNullAndUpperCase(airportDepartureAcronym);
        return Objects.nonNull(maybeDepartureAcronym) && Objects.nonNull(maybeArrivalAcronym) && Objects.equals(maybeDepartureAcronym, maybeArrivalAcronym);
    }

    private static  String stripToNullAndUpperCase(String element){
        return Optional.ofNullable(element)
                .map(StringUtils::stripToNull)
                .map(StringUtils::upperCase)
                .orElseGet(()-> null);
    }
}
