package com.example.nexo.shared.validation;

import java.text.Normalizer;
import java.util.Locale;

public final class TextoNormalizado {
    private TextoNormalizado() { }

    public static String nombre(String texto) {
        if (texto == null) return "";
        return Normalizer.normalize(texto, Normalizer.Form.NFKC)
                .replaceAll("[\\p{Z}\\s]+", " ").strip();
    }

    public static String clave(String texto) {
        return nombre(texto).toLowerCase(Locale.ROOT);
    }
}
