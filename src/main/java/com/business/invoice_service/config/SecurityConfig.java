package com.business.invoice_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/invoices/create").permitAll()
                        .requestMatchers("/api/invoices/update/byBookingId/{bookingId}/endTime").permitAll()
                        .requestMatchers("/api/invoices/all").permitAll()
                        .requestMatchers("/api/invoices/update/bill-totalMoney/{bookingId}").permitAll()
                                .requestMatchers("/api/invoices/update/{id}").permitAll()
                        .requestMatchers("/api/invoices/revenue").permitAll()
                        .anyRequest().authenticated()

                );





        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
