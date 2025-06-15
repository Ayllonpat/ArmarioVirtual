package com.trianasalesianos.dam.ArmarioVirtual.security.jwt.refresh;

import com.trianasalesianos.dam.ArmarioVirtual.security.exceptionhandling.JwtException;

public class RefreshTokenException extends JwtException {
    public RefreshTokenException(String s) {
        super(s);
    }
}
