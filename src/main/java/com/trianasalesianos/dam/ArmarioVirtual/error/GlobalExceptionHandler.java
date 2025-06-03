package com.trianasalesianos.dam.ArmarioVirtual.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Validation Error");
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));
        detail.setProperty("errors", errors);
        return detail;
    }

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
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        detail.setTitle("Usuario no autenticado");
        return detail;
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ProblemDetail handleUsuarioNoEncontradoException(UsuarioNoEncontradoException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        detail.setTitle("Usuario no encontrado");
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
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        detail.setTitle("Prenda no encontrada");
        return detail;
    }

    @ExceptionHandler(ConjuntoNoEncontradaException.class)
    public ProblemDetail handleConjuntoNoEncontradaException(ConjuntoNoEncontradaException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        detail.setTitle("Conjunto no encontrado");
        return detail;
    }

    @ExceptionHandler(ComentarioNoEncontradoException.class)
    public ProblemDetail handleComentarioNoEncontradoException(ComentarioNoEncontradoException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        detail.setTitle("Comentario no encontrado");
        return detail;
    }

    @ExceptionHandler(TagNoEncontradoException.class)
    public ProblemDetail handleTagNoEncontradoException(TagNoEncontradoException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        detail.setTitle("Tag no encontrado");
        return detail;
    }

}
