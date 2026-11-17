package com.parcelpulse.controller

import com.parcelpulse.config.JwtService
import org.springframework.web.bind.annotation.*

data class AuthRequest(val username: String)
data class AuthResponse(val token: String)

@RestController
@RequestMapping("/api/auth")
class AuthController(private val jwtService: JwtService) {

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): AuthResponse {
        // In a real app, you would hash and verify the user's password here.
        // For this backend simulation, we simply issue a token for the username.
        val token = jwtService.generateToken(request.username)
        return AuthResponse(token)
    }
}