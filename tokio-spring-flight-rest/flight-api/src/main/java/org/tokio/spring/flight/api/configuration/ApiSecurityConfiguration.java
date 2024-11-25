package org.tokio.spring.flight.api.configuration;

import org.junit.jupiter.api.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ApiSecurityConfiguration {

    @Bean("securityFilterChainMainly")
    @Order(1)
    public SecurityFilterChain securityFilterChainMainly(HttpSecurity httpSecurity) throws Exception {
        // define una cadena de seugridad
        return httpSecurity
                .securityMatcher("/login","/logout","/api/**") // request security
                .authorizeHttpRequests(
                        authorizeRequest->
                                authorizeRequest
                                        .requestMatchers("/api/**") // el resto y que no coinzida con la anterior
                                        .authenticated() // que este autenticado
                )
                .formLogin(Customizer.withDefaults()) // se usa el por defecto
                //.formLogin(loginCustomer -> loginCustomer.loginPage("/login").defaultSuccessUrl("/flight"))
                .logout(Customizer.withDefaults()) // se usa el por defecot
                //.logout(logoutCustomer -> logoutCustomer.logoutUrl("logout").logoutSuccessUrl("/login"))//
                // habilitar csrf y cors por defecto
                .csrf(Customizer.withDefaults())
                //.cors(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
