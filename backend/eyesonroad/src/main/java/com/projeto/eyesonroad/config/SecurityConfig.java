package com.projeto.eyesonroad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;
import jakarta.servlet.DispatcherType;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public FilterRegistrationBean<SessionAuthenticationFilter> sessionFilterRegistration(SessionAuthenticationFilter filter) {
        FilterRegistrationBean<SessionAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SessionAuthenticationFilter sessions) throws Exception {
        http 
            .cors(Customizer.withDefaults()) // Ativa o CORS integrado com as configurações do Spring
            .csrf(csrf -> csrf.disable())
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())
            .addFilterBefore(sessions, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                    .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/usuarios", "/usuarios/login", "/api/alertas").permitAll()
                    .anyRequest().authenticated()
            )
            .exceptionHandling(errors -> errors
                .authenticationEntryPoint((request, response, ex) -> response.sendError(401))
                .accessDeniedHandler((request, response, ex) -> response.sendError(403)));
            
        return http.build();
    }
}
