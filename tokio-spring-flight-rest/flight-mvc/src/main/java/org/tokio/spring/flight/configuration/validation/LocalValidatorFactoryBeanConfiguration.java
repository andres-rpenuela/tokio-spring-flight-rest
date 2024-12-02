package org.tokio.spring.flight.configuration.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@RequiredArgsConstructor
public class LocalValidatorFactoryBeanConfiguration {

    // ID
    private final MessageSource messageSource;

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        final LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        // defined the message source as messages validation
        localValidatorFactoryBean.setValidationMessageSource(messageSource);

        localValidatorFactoryBean.afterPropertiesSet(); // aplicar configuraciones "custom"
        return localValidatorFactoryBean;
    }
}
