package com.example.nexo.shared.validation;

public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) { super(mensaje); }
    public static void exigir(boolean condicion, String mensaje) {
        if (!condicion) throw new ReglaNegocioException(mensaje);
    }
}
