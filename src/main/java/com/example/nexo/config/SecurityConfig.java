package com.example.nexo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * F01 y F02 · Autenticación de la versión 2 con Spring Security.
 *
 * Se respetan los actores oficiales de 03_interfaces.md:
 * - Administrador: P05, P07, P12, P15, P16, P18–P23, P25, P26, P29 y P30;
 * - Operador / Vendedor o Empleado: P08 y P24 (registrar venta) y P13 (salida);
 * - cualquier usuario autenticado: P06, P09, P10, P11, P14 y P28.
 * El formulario de login es propio (JSP login.jsp), la sesión expira con
 * server.servlet.session.timeout y el cierre de sesión invalida la sesión.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain cadenaFiltros(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(autorizar -> autorizar
                        // Públicas: la hoja de estilos, el login, la página de error
                        // y las vistas internas (el forward del controller a un JSP
                        // vuelve a pasar por esta cadena)
                        .requestMatchers("/css/**", "/login", "/error", "/WEB-INF/**").permitAll()
                        // Secciones exclusivas del Administrador
                        .requestMatchers(
                                "/categorias/**",
                                "/combustibles/crear",
                                "/combustibles/editar",
                                "/combustibles/estado",
                                "/inventario/entrada/crear",
                                "/empleados/**",
                                "/usuarios/**",
                                "/compras/**",
                                "/finanzas/**",
                                "/asistencia/control")
                        .hasRole("ADMIN")
                        // Registro de ventas y salidas de inventario: personal de operación
                        .requestMatchers("/ventas/crear", "/inventario/salida/crear")
                        .hasAnyRole("OPERADOR", "EMPLEADO")
                        // El resto de las pantallas requieren sesión iniciada
                        .anyRequest().authenticated())
                .formLogin(formulario -> formulario
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", false)
                        .failureHandler((peticion, respuesta, excepcion) -> {
                            // Una cuenta inactiva recibe su propio aviso (F32)
                            String motivo = (excepcion instanceof DisabledException)
                                    ? "inactivo"
                                    : "credenciales";
                            respuesta.sendRedirect(peticion.getContextPath() + "/login?error=" + motivo);
                        }))
                .logout(cierre -> cierre
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .exceptionHandling(errores -> errores
                        // Un rol que entra a una sección ajena ve un aviso claro, nunca un 500
                        .accessDeniedPage("/sin-permisos"));
        return http.build();
    }

    /** Hash de contraseñas: sólo hashes BCrypt se guardan o se muestran. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
