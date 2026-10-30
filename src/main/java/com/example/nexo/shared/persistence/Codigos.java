package com.example.nexo.shared.persistence;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.UUID;

public final class Codigos {
    private Codigos() { }
    public static String nuevo(String prefijo, int longitud) {
        return desdeToken(UUID.randomUUID().toString(), prefijo, longitud);
    }
    /** El código único SQL representa la solicitud; repetirlo no duplica efectos. */
    public static String desdeToken(String token, String prefijo, int longitud) {
        try {
            String hash = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8)));
            return prefijo + hash.substring(0, longitud-prefijo.length()).toUpperCase(java.util.Locale.ROOT);
        } catch (java.security.NoSuchAlgorithmException e) { throw new IllegalStateException(e); }
    }
}
