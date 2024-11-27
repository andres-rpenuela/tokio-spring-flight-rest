package org.tokio.spring.flight.validation.binding;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.tokio.spring.flight.dto.FlightMvcDTO;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FlightMvcDTOValidator extends CustomValidatorBean {
    // Requiere exponer un bean
    // {@link com.tokioschool.flightapp.flight.configuration.validation.LocalValidatorFactoryBeanConfiguration}
    private final LocalValidatorFactoryBean localValidatorFactoryBean; // Para tener encuenta el resto de validaciones

    @Override
    public void validate(Object target, Errors errors) {
        localValidatorFactoryBean.validate(target, errors);

        if(!FlightMvcDTO.class.isInstance(target)) {
            return;
        }

        final FlightMvcDTO  flightMvcDTO = (FlightMvcDTO) target;

        if(Objects.equals(flightMvcDTO.getAirportArrivalAcronym(),flightMvcDTO.getAirportDepartureAcronym())){
            errors.rejectValue(
                    "airportArrivalAcronym",
                    "validation.flight.arrival.equals_departure", // message i18n custom
                    "Arrival cannot be the same as Departure"
            );
        }
    }
}
