package com.projeto.eyesonroad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    	
    	http 
    		.cors(cors -> cors.disable())
    		.csrf(csrf -> csrf.disable())
    		.authorizeHttpRequests(auth -> auth
    				.requestMatchers("/error").permitAll()
    				.requestMatchers("/usuario/**").permitAll()
    				.requestMatchers("/status-sono/**").permitAll()
    				.requestMatchers("/sensor/**").permitAll()
    				.requestMatchers("/monitor/**").permitAll()
    				.requestMatchers("/evento/**").permitAll()
    				.requestMatchers("/endereco/**").permitAll()
    				.requestMatchers("/carro/**").permitAll()
    				.requestMatchers("/atividade/**").permitAll()
    				.anyRequest().authenticated()
    				);
    		
    				
    		return http.build();
    		
    }
}