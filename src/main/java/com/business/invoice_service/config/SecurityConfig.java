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
                        .requestMatchers("/api/invoices/endpoints").permitAll()
                        .requestMatchers("/api/invoices/create").permitAll()
                        .requestMatchers("/api/invoices/update/byBookingId/{bookingId}/endTime").permitAll()
                        .requestMatchers("/api/invoices/all").permitAll()
                        .requestMatchers("/api/invoices/update/bill-totalMoney/{bookingId}").permitAll()
                        .requestMatchers("/api/invoices/update/bill-totalMoney/{tableId}").permitAll()
                        .requestMatchers("/api/invoices/update/bill-totalMoney/{bookingId}/{tableId}").permitAll()
                                .requestMatchers("/api/invoices/update/{id}").permitAll()
                        .requestMatchers("/api/invoices/revenue").permitAll()
                        .requestMatchers("/api/invoices/booking/{bookingId}").permitAll()
                        .requestMatchers("/api/invoices/total-playtime").permitAll()
                        .requestMatchers("/api/invoices/updateEndTimeAndLinkTable/{tableId}").permitAll()
                        .requestMatchers("/api/invoices/byTableIdAndStatus/{tableId}/{status}").permitAll()
                        .requestMatchers("/api/invoices/createForSelectedTable").permitAll()
                        .requestMatchers("/api/invoices/create-for-booking/{bookingId}").permitAll()
                        .requestMatchers("/api/invoices/{id}").permitAll()

                        .requestMatchers("/api/invoices/invoice_table/{tableId}").permitAll()

                        .requestMatchers("/api/invoices/payments/all").permitAll()
                        .requestMatchers("/api/invoices/check-table-used/{tableId}").permitAll()

                        .anyRequest().authenticated()

                );





        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
