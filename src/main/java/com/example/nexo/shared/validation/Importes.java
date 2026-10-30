package com.example.nexo.shared.validation;

import java.math.BigDecimal;

public final class Importes {
    private Importes() { }

    public static boolean litros(BigDecimal valor) {
        return positivo(valor, 3, "999999999.999");
    }

    public static boolean dinero(BigDecimal valor) {
        return positivo(valor, 2, "9999999999.99");
    }

    private static boolean positivo(BigDecimal valor, int escala, String maximo) {
        return valor != null && valor.signum() > 0
                && valor.stripTrailingZeros().scale() <= escala
                && valor.compareTo(new BigDecimal(maximo)) <= 0;
    }
}
