package com.example.nexo.shared.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Locale;

/** Conserva etiquetas JSP y almacena los estados/tipos SQL del diccionario. */
@Converter
public class TipoMovimientoConverter implements AttributeConverter<String,String> {
    public String convertToDatabaseColumn(String valor) {
        return valor == null ? null : valor.toUpperCase(Locale.ROOT);
    }
    public String convertToEntityAttribute(String valor) {
        return valor == null || valor.isEmpty() ? valor
                : valor.substring(0,1).toUpperCase(Locale.ROOT) + valor.substring(1).toLowerCase(Locale.ROOT);
    }
}
