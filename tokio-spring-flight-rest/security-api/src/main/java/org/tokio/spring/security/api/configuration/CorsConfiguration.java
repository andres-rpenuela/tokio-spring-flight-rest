package org.tokio.spring.security.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // se registran que todas las paginas de api
        // solo permita las petionces Post y Get a ellas si proceden
        // del entorno local
        registry.addMapping("/api/**")
                .allowedMethods(HttpMethod.POST.name(), HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name())
                .allowedOrigins("http://localhost:8071");

        // para login aplicar lo mismo (ya que esta fuera de "/flight
        registry.addMapping("/login")
                .allowedMethods(HttpMethod.POST.name(), HttpMethod.GET.name())
                .allowedOrigins("http://localhost:8071");
    }
}
