package com.trianasalesianos.dam.ArmarioVirtual.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailDuplicadoException.class)
    public ProblemDetail handleEmailDuplicadoException(EmailDuplicadoException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        detail.setTitle("Email ya registrado");
        return detail;
    }

    @ExceptionHandler(UsernameDuplicadoException.class)
    public ProblemDetail handleUsernameDuplicadoException(UsernameDuplicadoException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        detail.setTitle("Username en uso");
        return detail;
    }

    @ExceptionHandler(TipoUsuarioInvalidoException.class)
    public ProblemDetail handleTipoUsuarioInvalidoException(TipoUsuarioInvalidoException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        detail.setTitle("Tipo de usuario inválido");
        return detail;
    }

    @ExceptionHandler(UsuarioNoAutenticadoException.class)
    public ProblemDetail handleUsuarioNoAutenticadoException(UsuarioNoAutenticadoException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        detail.setTitle("Usuario no autenticado");
        return detail;
    }

    @ExceptionHandler(TipoPrendaNoEncontradaException.class)
    public ProblemDetail handleTipoPrendaNoEncontradaException(TipoPrendaNoEncontradaException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        detail.setTitle("TipoPrenda no encontrada");
        return detail;
    }

    @ExceptionHandler(PrendaNoEncontradaException.class)
    public ProblemDetail handlePrendaNoEncontradaException(PrendaNoEncontradaException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        detail.setTitle("Prenda no encontrada");
        return detail;
    }

    @ExceptionHandler(ConjuntoNoEncontradaException.class)
    public ProblemDetail handleConjuntoNoEncontradaException(ConjuntoNoEncontradaException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        detail.setTitle("Conjunto no encontrada");
        return detail;
    }
}
