package org.tokio.spring.flight.configuration.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

/**
 * Ejemplo de logger peticiones http, y ver el tiempo que tarda en ejecutarse
 */
@Configuration
@Slf4j
public class LoggerConfiguration implements WebMvcConfigurer {

    @Bean
    public HandlerInterceptor handlerInterceptor() {
        // se puede crear una clase aparte que implment HandlerInterceptor, como LocaleChangeInterceptor
        return new HandlerInterceptor() {
            // intercepotr para moninotrizar el tiempo de ejecución de una peticion que vaya a /flight

            private boolean filter(HttpServletRequest request){
                return request.getRequestURI().startsWith("/flight");
            }

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                // cuando la request esta llegando a la aplicación

                if(filter(request)){
                    // se agrega el tiempo en que se recibe a la cabecera
                    request.setAttribute("ts", System.currentTimeMillis());
                    // uso de MDC (Mapper Diagnostic Context) para asociar un key-valor a la petición
                    // esto va hacer que durante todo el hilo que se procesa la requeste van ha tener el mismo
                    // request-ID
                    // para mostrar la traza, requiere un appender
                    MDC.put("requestID", UUID.randomUUID().toString());
                    log.debug("request {} {} {}",request.getMethod(),request.getRequestURI(),MDC.get("requestID"));
                    findLoggerUser().ifPresent(userId-> MDC.put("user-ID",userId)); // informacion
                }
                // si es true continua con el proeceso de la request
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                // despues de manejar la peticion
                if(filter(request)){
                    // recojo el atributo con el tiempo de inicio y se calcula el tiempo
                    // para mostrar la traza
                    long ts = (long) request.getAttribute("ts");
                    log.debug("response: {} {} ms: {} requestID: {}",
                            request.getMethod(),
                            request.getRequestURI(),
                            System.currentTimeMillis() - ts,
                            MDC.get("requestID"));
                    // si se usa el MDC, van a tener el mismo request-ID que el "preHanle" para cada request que se procese
                }
                HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
             }
        };
    }

    /**
     * Metodo que crea un contexto de autenticación al que se accede
     * para obtner la identidad
     *
     * @return identidad del autenticado u optional empty
     */
    private Optional<String> findLoggerUser() {

        // se cra un contexto de autenticación y se obtiente el elemento autenticado para extrar la identidad
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Principal::getName);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor());
    }
}
