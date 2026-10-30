package com.example.nexo.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.nexo.empleado.service.EmpleadoService;
import com.example.nexo.usuario.service.UsuarioService;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

/**
 * F01 y F02 · Autenticación de la versión 2 con Spring Security.
 *
 * Permisos del Word actualizado (5.6.3): Administrador administra catálogos,
 * compras, personal y finanzas; Operador registra ajustes; Vendedor registra
 * ventas. Los tres consultan existencias y su asistencia.
 * El formulario de login es propio (JSP login.jsp), la sesión expira con
 * server.servlet.session.timeout y el cierre de sesión invalida la sesión.
 */
@Configuration
@EnableWebSecurity
@org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain cadenaFiltros(HttpSecurity http, UsuarioService usuarios,
            EmpleadoService empleados) throws Exception {
        http
                .addFilterAfter(new CuentaVigenteFilter(usuarios, empleados), AnonymousAuthenticationFilter.class)
                .authorizeHttpRequests(autorizar -> autorizar
                        // Públicas: la hoja de estilos, el login, la página de error
                        // y las vistas internas (el forward del controller a un JSP
                        // vuelve a pasar por esta cadena)
                        .requestMatchers("/css/**", "/js/**", "/login", "/error", "/WEB-INF/**").permitAll()
                        // Secciones exclusivas del Administrador
                        .requestMatchers(
                                "/categorias/**", "/proveedores/**",
                                "/combustibles/crear",
                                "/combustibles/editar",
                                "/combustibles/estado",
                                "/empleados/**",
                                "/usuarios/**",
                                "/compras/**",
                                "/finanzas/**",
                                "/asistencia/control")
                        .hasRole("ADMIN")
                        // Registro de ventas y salidas de inventario: personal de operación
                        .requestMatchers("/ventas/**")
                        .hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers("/inventario/entrada/crear", "/inventario/salida/crear",
                                "/inventario/entradas", "/inventario/salidas")
                        .hasAnyRole("ADMIN", "OPERADOR")
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
