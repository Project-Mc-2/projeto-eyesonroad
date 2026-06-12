package com.projeto.eyesonroad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        config.addAllowedOrigin("*"); 
        
        config.addAllowedHeader("*");
        
        config.addAllowedMethod("*"); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        source.registerCorsConfiguration("/usuarios", config);
        source.registerCorsConfiguration("/usuarios/login", config);
        source.registerCorsConfiguration("/usuarios/cadastro", config);

        source.registerCorsConfiguration("/carros", config);
        source.registerCorsConfiguration("/enderecos", config);
        source.registerCorsConfiguration("/sensores", config);
        source.registerCorsConfiguration("/eventos", config);
        source.registerCorsConfiguration("/atividades", config);
        source.registerCorsConfiguration("/monitores", config);
        source.registerCorsConfiguration("/status-sono", config);
        
        return new CorsFilter(source);
    }
}