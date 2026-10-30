package com.example.nexo;

import static org.junit.jupiter.api.Assertions.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import com.example.nexo.usuario.service.UsuarioServiceImpl;

/** Peticiones HTTP reales a Tomcat/Jasper: detecta errores JSP invisibles para MockMvc. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@org.springframework.test.context.ActiveProfiles("demo")
class JspHttpTest {
    @LocalServerPort int puerto;

    URI uri(String path) { return URI.create("http://localhost:" + puerto + path); }

    String pagina(HttpClient cliente, String path) throws Exception {
        var response = cliente.send(HttpRequest.newBuilder(uri(path)).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), path + "\n" + response.body());
        assertFalse(response.body().contains("$2a$"), "Se expuso un hash BCrypt en " + path);
        assertFalse(response.body().contains("$2b$"), "Se expuso un hash BCrypt en " + path);
        return response.body();
    }

    String encode(String valor) { return URLEncoder.encode(valor, StandardCharsets.UTF_8); }

    HttpClient login(String username) throws Exception {
        var cookies = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        var cliente = HttpClient.newBuilder().cookieHandler(cookies).build();
        String html = pagina(cliente, "/login");
        var csrf = Pattern.compile("name=\"_csrf\"[^>]*value=\"([^\"]+)\"").matcher(html);
        assertTrue(csrf.find(), "El formulario JSP debe incluir CSRF");
        String body = "username=" + username + "&password=" + encode(UsuarioServiceImpl.CONTRASENA_DEMO)
                + "&_csrf=" + encode(csrf.group(1));
        var response = cliente.send(HttpRequest.newBuilder(uri("/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body)).build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(302, response.statusCode());
        assertFalse(response.headers().firstValue("Location").orElse("").contains("error"));
        return cliente;
    }

    @Test void administradorCompilaTodasSusJsp() throws Exception {
        var cliente = login("ediaz");
        for (String path : new String[] {
                "/dashboard", "/proveedores/list", "/proveedores/crear", "/proveedores/editar?id=1", "/categorias/list", "/categorias/crear", "/categorias/editar?id=1",
                "/combustibles/list", "/combustibles/crear", "/combustibles/editar?id=PR01",
                "/empleados/list", "/empleados/crear", "/empleados/editar?id=1",
                "/usuarios/list", "/usuarios/crear", "/usuarios/editar?id=1",
                "/compras/list", "/compras/crear", "/compras/detalle?id=C001",
                "/ventas/list", "/ventas/crear", "/ventas/detalle?id=V001",
                "/inventario/list", "/inventario/entradas", "/inventario/salidas",
                "/inventario/entrada/crear", "/inventario/salida/crear",
                "/finanzas/list", "/finanzas/ingresos", "/finanzas/egresos", "/finanzas/detalle?id=MC001",
                "/finanzas/conceptos/list", "/finanzas/conceptos/crear", "/finanzas/conceptos/editar?id=CE01",
                "/asistencia/mi", "/asistencia/control"}) {
            pagina(cliente, path);
        }
    }

    @Test void vendedorVeNavegacionYPrecisionPermitidas() throws Exception {
        var cliente = login("lrojas");
        String inventario = pagina(cliente, "/inventario/list");
        assertFalse(inventario.contains("Libro de movimientos físicos"));
        assertFalse(inventario.contains("href=\"/compras/list\""));
        String venta = pagina(cliente, "/ventas/crear");
        assertTrue(venta.contains("step=\"0.001\""));
        assertTrue(venta.contains("name=\"formularioToken\""));
        assertFalse(venta.contains("name=\"idUsuario\""));
    }

    @Test void operadorVeAjustesYSusPantallas() throws Exception {
        var cliente = login("atorres");
        for (String path : new String[] {"/combustibles/list", "/inventario/list", "/inventario/entradas",
                "/inventario/salidas", "/inventario/entrada/crear", "/inventario/salida/crear", "/asistencia/mi"}) {
            String html = pagina(cliente, path);
            assertFalse(html.contains("href=\"/ventas/list\""));
        }
    }
}
