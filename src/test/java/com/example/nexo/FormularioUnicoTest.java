package com.example.nexo;

import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import com.example.nexo.shared.web.FormularioUnico;

class FormularioUnicoTest {
    @Test void tokenSoloEjecutaUnaVez() {
        var sesion = new MockHttpSession();
        String token = FormularioUnico.emitir(sesion, "venta");
        var contador = new AtomicInteger();
        assertNull(FormularioUnico.ejecutar(sesion, "venta", token, () -> {contador.incrementAndGet(); return null;}));
        assertNotNull(FormularioUnico.ejecutar(sesion, "venta", token, () -> {contador.incrementAndGet(); return null;}));
        assertEquals(1, contador.get());
    }

    @Test void tokenNoEsTransferibleEntreSesionesNiOperaciones() {
        var sesion = new MockHttpSession();
        String token = FormularioUnico.emitir(sesion, "venta");
        assertNotNull(FormularioUnico.ejecutar(new MockHttpSession(), "venta", token, () -> fail()));
        assertNotNull(FormularioUnico.ejecutar(sesion, "compra", token, () -> fail()));
    }

    @Test void errorDeValidacionNoConsumeToken() {
        var sesion = new MockHttpSession();
        String token = FormularioUnico.emitir(sesion, "venta");
        assertEquals("Cantidad inválida", FormularioUnico.ejecutar(sesion, "venta", token, () -> "Cantidad inválida"));
        assertNull(FormularioUnico.ejecutar(sesion, "venta", token, () -> null));
    }

    @Test void reenviosConcurrentesEjecutanUnaVez() throws Exception {
        var sesion = new MockHttpSession();
        String token = FormularioUnico.emitir(sesion, "venta");
        var contador = new AtomicInteger();
        ExecutorService pool = Executors.newFixedThreadPool(8);
        try {
            var tareas = new java.util.ArrayList<Callable<String>>();
            for (int i = 0; i < 24; i++) tareas.add(() -> FormularioUnico.ejecutar(sesion,
                    "venta", token, () -> {contador.incrementAndGet(); return null;}));
            for (var resultado : pool.invokeAll(tareas)) resultado.get();
            assertEquals(1, contador.get());
        } finally { pool.shutdownNow(); }
    }
}
