package com.trianasalesianos.dam.ArmarioVirtual.security.exceptionhandling;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
