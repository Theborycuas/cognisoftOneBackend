package com.cognisoftone.common.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - BAD REQUEST: Datos mal formados o inválidos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(); // o procesa todos
        return ApiErrorUtil.build(HttpStatus.BAD_REQUEST, message, request);
    }

    // 400 - BAD REQUEST: parámetros faltantes o mal enviados
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        return ApiErrorUtil.build(HttpStatus.BAD_REQUEST, "Falta el parámetro requerido: " + ex.getParameterName(), request);
    }

    // 401 - UNAUTHORIZED: Credenciales incorrectas
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return ApiErrorUtil.build(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas. Verifica tu email y contraseña.", request);
    }

    // 401 - UNAUTHORIZED: usuario no autenticado
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        return ApiErrorUtil.build(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    // 401 - UNAUTHORIZED: Invalid Token Exception
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidToken(InvalidTokenException ex, HttpServletRequest request) {
        return ApiErrorUtil.build(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    // 403 - FORBIDDEN: acceso denegado
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return ApiErrorUtil.build(HttpStatus.FORBIDDEN, "Acceso denegado.", request);
    }

    // 404 - NOT FOUND: recurso no encontrado
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        return ApiErrorUtil.build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // 404 - NOT FOUND: recurso no encontrado
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUsernameNotFound(UsernameNotFoundException ex, HttpServletRequest request) {
        return ApiErrorUtil.build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // 409 - CONFLICT: recurso duplicado o inconsistencia lógica
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicate(DuplicateResourceException ex, HttpServletRequest request) {
        return ApiErrorUtil.build(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    // 422 - UNPROCESSABLE ENTITY: semántica válida pero no puede procesarse
    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ApiErrorResponse> handleUnprocessable(UnprocessableEntityException ex, HttpServletRequest request) {
        return ApiErrorUtil.build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), request);
    }

    // 500 - INTERNAL SERVER ERROR: catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        ex.printStackTrace(); // solo para desarrollo
        return ApiErrorUtil.build(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado.", request);
    }
}
