package com.trianasalesianos.dam.ArmarioVirtual.error;

public class UsuarioNoAutenticadoException extends RuntimeException {
    public UsuarioNoAutenticadoException(String message) {
        super(message);
    }
}
