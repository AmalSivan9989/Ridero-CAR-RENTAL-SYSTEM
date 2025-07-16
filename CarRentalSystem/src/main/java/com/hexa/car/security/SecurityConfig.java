package com.hexa.car.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

import com.hexa.car.service.UserDataService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDataService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors()
            .and()
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                 .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

           
                .requestMatchers("/auth/welcome", "/auth/addNewUser", "/auth/generateToken").permitAll()

                
                .requestMatchers("/auth/user/userProfile").hasAuthority("ROLE_CUSTOMER")
                .requestMatchers("/auth/admin/adminProfile").hasAuthority("ROLE_ADMIN")

               
                .requestMatchers(
                    "/admin/getAllUsers",
                    "/admin/getAllBookings",
                    "/admin/getAllCars",
                    "/admin/deleteCar/**",
                    "/admin/user/**",
                    "/admin/reports/summary"
                ).hasAnyAuthority("ROLE_AGENT", "ROLE_ADMIN")
.requestMatchers("/getUser/**").hasAuthority("ROLE_ADMIN")
.requestMatchers("/agent/getAllAgents").hasAnyAuthority("ROLE_AGENT", "ROLE_ADMIN")

               
                .requestMatchers(
                    "/bookings/createBooking",
                    "/bookings/updateBooking/**",
                    "/bookings/getMyBookings",
                    "/bookings/cancelBooking/**",
                    "/bookings/checkin/**",
                    "/bookings/checkout/**"
                ).hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN")

              
                .requestMatchers("/agent/checkin/**", "/agent/checkout/**").hasAuthority("ROLE_AGENT")

               
                .requestMatchers("/cars/addCar", "/cars/updateAvailability/**").hasAnyAuthority("ROLE_AGENT", "ROLE_ADMIN")
                .requestMatchers("/cars/getAllAvailableCars", "/cars/getCarsByLocation/**").permitAll()

                
                .requestMatchers("/feedback/**").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN")

              
                .requestMatchers("/maintenance/reportIssue/**").hasAnyAuthority("ROLE_AGENT", "ROLE_ADMIN")
                .requestMatchers("/maintenance/getRequestsByAgent/**").hasAnyAuthority("ROLE_AGENT", "ROLE_ADMIN")

            
                .requestMatchers("/payments/makePayment/**").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN")
                .requestMatchers("/payments/getAllPayments").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/payments/history/**", "/payments/pendingPayments/**").hasAuthority("ROLE_CUSTOMER")

            
                .requestMatchers("/wallet/**").hasAuthority("ROLE_CUSTOMER")
.requestMatchers("/reports/full-summary").hasAuthority("ROLE_ADMIN")

                
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
