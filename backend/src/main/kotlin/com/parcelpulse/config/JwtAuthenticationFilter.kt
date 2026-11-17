package com.parcelpulse.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 1. Look for the "Authorization" header
        val authHeader = request.getHeader("Authorization")
        
        // 2. If it's missing or doesn't start with "Bearer ", reject and move on
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        // 3. Extract the token and the username
        val jwt = authHeader.substring(7)
        val username = try {
            jwtService.extractUsername(jwt)
        } catch (e: Exception) {
            null
        }

        // 4. If the token is cryptographically valid, log the user in for this request
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            if (jwtService.isTokenValid(jwt, username)) {
                // In a production app, you would look up the user in PostgreSQL here.
                val userDetails = User.withUsername(username).password("").authorities("USER").build()
                
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                
                // Inform Spring Security that the user is officially authenticated
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        
        filterChain.doFilter(request, response)
    }
}