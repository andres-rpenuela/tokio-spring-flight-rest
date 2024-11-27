package org.tokio.spring.flight.configuration.internazionalitation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
public class LocaleInterceptorConfig implements WebMvcConfigurer {

    /**
     * Created a Bean to LocalResolver based to Cookie and not of HTTP-HEADER
     *
     * @return instance of cookie locale resolver with default locale english
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(Locale.of("en"));
        return localeResolver;
    }

    /**
     * Created a Bean to locale interceptor, this read in the url to any controller the param "lang"
     * and updated the locale
     *
     * @return instance a locale change interceptor
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    /**
     * Registrer to list of interceptors, own interceptor
     *
     * @param registry injection of dependency of registry of interceptors of application
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry ) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
