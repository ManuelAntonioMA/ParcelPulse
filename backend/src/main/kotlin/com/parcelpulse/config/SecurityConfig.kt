package com.parcelpulse.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // Disable CSRF protection since this is a stateless REST API
            .csrf { it.disable() }
            // Allow all requests to pass through without authentication for development
            .authorizeHttpRequests { auth ->
                auth.anyRequest().permitAll()
            }
        return http.build()
    }
}