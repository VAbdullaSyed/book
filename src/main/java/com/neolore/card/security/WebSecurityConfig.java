package com.neolore.card.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity httpSecurity) throws Exception {
       return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
               .authorizeHttpRequests( auth -> auth
                       .requestMatchers("**").authenticated()
                       .anyRequest().authenticated()
               )
               .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
               .httpBasic(Customizer.withDefaults())
        .build();
    }
}
