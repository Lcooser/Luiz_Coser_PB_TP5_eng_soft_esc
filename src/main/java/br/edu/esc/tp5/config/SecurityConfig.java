package br.edu.esc.tp5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.CrossOriginOpenerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.CrossOriginResourcePolicyHeaderWriter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/actuator/health", "/actuator/info").permitAll()
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers
                        .contentSecurityPolicy(policy -> policy.policyDirectives(
                                "default-src 'self'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; " +
                                        "font-src 'self'; script-src 'self'; object-src 'none'; base-uri 'self'; " +
                                        "form-action 'self'; frame-ancestors 'none'"
                        ))
                        .frameOptions(frameOptions -> frameOptions.deny())
                        .referrerPolicy(referrerPolicy -> referrerPolicy.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN))
                        .permissionsPolicy(permissions -> {
                            permissions.policy(
                                "accelerometer=(), autoplay=(), camera=(), display-capture=(), fullscreen=(self), " +
                                        "geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), " +
                                        "payment=(), usb=()"
                            );
                        })
                        .crossOriginOpenerPolicy(crossOriginOpenerPolicy -> crossOriginOpenerPolicy
                                .policy(CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy.SAME_ORIGIN))
                        .crossOriginResourcePolicy(crossOriginResourcePolicy -> crossOriginResourcePolicy
                                .policy(CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy.SAME_ORIGIN))
                )
                .csrf(Customizer.withDefaults());

        return http.build();
    }
}
