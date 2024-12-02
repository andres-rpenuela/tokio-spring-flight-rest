package org.tokio.spring.flight.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Devuevle una instancia nueva de {@link ModelMapper}, cada vez
 * que detecte un componente del tipo {@link ModelMapper}
 */

@Configuration
public class ModelMapperBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

