package com.customer_service.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.customer_service.config.jwt.JwtAuthFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtFilter;
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfig corsConfig) throws Exception {
    
        http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
                auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/error", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/info", "/actuator/health").permitAll()
                .requestMatchers(HttpMethod.GET, "/region", "/region/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/region", "/region/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/region", "/region/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/region", "/region/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/customer").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/customer/{id}").hasAnyAuthority("ADMIN", "CUSTOMER")
                .requestMatchers(HttpMethod.POST, "/customer").hasAnyAuthority("ADMIN", "CUSTOMER")
                .requestMatchers(HttpMethod.PUT, "/customer/{id}").hasAnyAuthority("ADMIN", "CUSTOMER")
                .requestMatchers(HttpMethod.PATCH, "/customer/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/customer-image").hasAnyAuthority("ADMIN", "CUSTOMER")
                .anyRequest().authenticated()
                )
        .cors(cors -> cors.configurationSource(corsConfig))
        .httpBasic(Customizer.withDefaults())
        .formLogin(form -> form.disable())
        .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}
