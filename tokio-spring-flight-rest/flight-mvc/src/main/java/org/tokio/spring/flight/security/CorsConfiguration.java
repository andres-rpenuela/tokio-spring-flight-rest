package org.tokio.spring.flight.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // se registran que todas las paginas de flight
        // solo permita las petionces Post y Get a ellas si procden
        // del entorno local
        registry.addMapping("/flight/**")
                .allowedMethods(HttpMethod.POST.name(), HttpMethod.GET.name())
                .allowedOrigins("http://localhost:8080");

        // para login aplicar lo mismo (ya que esta fuera de "/flight
        registry.addMapping("/login")
                .allowedMethods(HttpMethod.POST.name(), HttpMethod.GET.name())
                .allowedOrigins("http://localhost:8080");
    }
}
