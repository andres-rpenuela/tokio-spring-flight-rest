package org.tokio.spring.flight.api.core.validation.binding;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@RequiredArgsConstructor
public class LocalValidatorFactoryConfiguration {

    // ID for get messages i18n
    private final MessageSource messageSource;

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        final LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        // defined the message source as messages validation
        localValidatorFactoryBean.setValidationMessageSource(messageSource);

        localValidatorFactoryBean.afterPropertiesSet(); // apply the all "custom" configs
        return localValidatorFactoryBean;
    }

}
