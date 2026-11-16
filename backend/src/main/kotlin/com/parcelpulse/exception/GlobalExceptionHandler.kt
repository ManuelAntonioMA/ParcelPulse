package com.parcelpulse.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<Any> {
        val errorBody = mapOf("error" to (ex.message ?: "Invalid request"))
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<Any> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid") }
        val errorBody = mapOf("error" to "Validation failed", "details" to errors)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody)
    }
}