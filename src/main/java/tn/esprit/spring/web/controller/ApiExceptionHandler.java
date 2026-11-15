package tn.esprit.spring.web.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tn.esprit.spring.web.dto.ApiError;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> traiterEntiteIntrouvable(EntityNotFoundException exception,
                                                             HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage(), Map.of(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> traiterValidation(MethodArgumentNotValidException exception,
                                                      HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            fieldErrors.putIfAbsent(error.getField(),
                    error.getDefaultMessage() == null ? "Valeur invalide." : error.getDefaultMessage());
        }
        return build(HttpStatus.BAD_REQUEST, "Validation échouée.", fieldErrors, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> traiterContrainteValidation(ConstraintViolationException exception,
                                                                HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        exception.getConstraintViolations().forEach(violation ->
                fieldErrors.putIfAbsent(violation.getPropertyPath().toString(), violation.getMessage()));
        return build(HttpStatus.BAD_REQUEST, "Validation échouée.", fieldErrors, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> traiterCorpsInvalide(HttpMessageNotReadableException exception,
                                                         HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Corps JSON absent ou invalide.", Map.of(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> traiterParametreInvalide(MethodArgumentTypeMismatchException exception,
                                                             HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST,
                "Paramètre invalide : " + exception.getName() + ".", Map.of(), request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> traiterParametreManquant(MissingServletRequestParameterException exception,
                                                             HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST,
                "Paramètre obligatoire manquant : " + exception.getParameterName() + ".", Map.of(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> traiterArgumentInvalide(IllegalArgumentException exception,
                                                            HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, exception.getMessage(), Map.of(), request);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiError> traiterIdentifiantNonValide(
            ObjectOptimisticLockingFailureException exception, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST,
                "Ne pas envoyer d'identifiant lors de la création.", Map.of(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> traiterContrainte(DataIntegrityViolationException exception,
                                                      HttpServletRequest request) {
        return build(HttpStatus.CONFLICT,
                "Opération refusée : contrainte de base de données non respectée.", Map.of(), request);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message,
                                           Map<String, String> fieldErrors,
                                           HttpServletRequest request) {
        ApiError error = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                fieldErrors);
        return ResponseEntity.status(status).body(error);
    }
}
