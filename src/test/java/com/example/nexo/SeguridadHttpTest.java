package com.example.nexo;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.web.servlet.MockMvc;
import com.example.nexo.usuario.service.UsuarioServiceImpl;
import com.example.nexo.empleado.service.EmpleadoService;
import com.example.nexo.usuario.service.UsuarioService;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles("demo")
class SeguridadHttpTest {
    @Autowired MockMvc mvc;
    @Autowired org.springframework.jdbc.core.JdbcTemplate jdbc;
    @Autowired com.example.nexo.usuario.repository.UsuarioRepository usuarioRepo;
    @Autowired EmpleadoService empleados;
    @Autowired UsuarioService usuarios;
    @Autowired com.example.nexo.venta.service.VentaService ventas;
    @Autowired com.example.nexo.inventario.service.MovimientoInventarioService inventario;
    @Autowired com.example.nexo.asistencia.service.AsistenciaService asistencia;

    record Formulario(MockHttpSession sesion, CsrfToken csrf) { }

    Formulario formulario() throws Exception {
        var request = mvc.perform(get("/login")).andReturn().getRequest();
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        token.getToken(); // Materializa el token diferido en la sesión real del filtro.
        return new Formulario((MockHttpSession) request.getSession(), token);
    }

    MockHttpSession login(String username) throws Exception {
        var f = formulario();
        var result = mvc.perform(post("/login").session(f.sesion())
                .param(f.csrf().getParameterName(), f.csrf().getToken())
                .param("username", username).param("password", UsuarioServiceImpl.CONTRASENA_DEMO))
                .andExpect(status().is3xxRedirection()).andReturn();
        assertFalse(result.getResponse().getRedirectedUrl().contains("error"));
        return (MockHttpSession) result.getRequest().getSession(false);
    }

    @Test void accesoAnonimoRedirigeAlLogin() throws Exception {
        mvc.perform(get("/usuarios/list")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test void loginRequiereCsrf() throws Exception {
        mvc.perform(post("/login").param("username", "ediaz").param("password", "incorrecta"))
                .andExpect(status().isForbidden());
    }

    @Test void credencialesIncorrectasYCuentasInexistentesSeRechazan() throws Exception {
        for (String usuario : new String[] {"ediaz", "noexiste"}) {
            var f = formulario();
            mvc.perform(post("/login").session(f.sesion())
                    .param(f.csrf().getParameterName(), f.csrf().getToken())
                    .param("username", usuario).param("password", "incorrecta"))
                    .andExpect(redirectedUrl("/login?error=credenciales"));
        }
    }

    @Test void permisosSeparanAdministradorOperadorVendedor() throws Exception {
        MockHttpSession admin = login("ediaz");
        mvc.perform(get("/usuarios/list").session(admin)).andExpect(status().isOk());
        mvc.perform(get("/compras/crear").session(admin)).andExpect(status().isOk());
        MockHttpSession operador = login("atorres");
        mvc.perform(get("/inventario/entrada/crear").session(operador)).andExpect(status().isOk());
        mvc.perform(get("/ventas/crear").session(operador)).andExpect(status().isForbidden());
        mvc.perform(get("/usuarios/list").session(operador)).andExpect(status().isForbidden());
        MockHttpSession vendedor = login("lrojas");
        mvc.perform(get("/ventas/crear").session(vendedor)).andExpect(status().isOk());
        mvc.perform(get("/inventario/salida/crear").session(vendedor)).andExpect(status().isForbidden());
        mvc.perform(get("/finanzas/list").session(vendedor)).andExpect(status().isForbidden());
    }

    @Test void empleadoInactivoBloqueaLoginYSesionAnterior() throws Exception {
        MockHttpSession sesion = login("atorres");
        jdbc.update("update empleado set estado='Inactivo' where id_empleado=1");
        try {
            mvc.perform(get("/asistencia/mi").session(sesion))
                    .andExpect(redirectedUrl("/login?error=inactivo"));
            var f = formulario();
            mvc.perform(post("/login").session(f.sesion())
                    .param(f.csrf().getParameterName(), f.csrf().getToken())
                    .param("username", "atorres").param("password", UsuarioServiceImpl.CONTRASENA_DEMO))
                    .andExpect(redirectedUrl("/login?error=inactivo"));
        } finally { jdbc.update("update empleado set estado='Activo' where id_empleado=1"); }
    }

    @Test void usuarioInactivoNoPuedeAcceder() throws Exception {
        var cuenta = usuarios.buscarUsuarioPorUsername("lrojas");
        cuenta.setEstado("Inactivo"); usuarioRepo.saveAndFlush(cuenta);
        try {
            var f = formulario();
            mvc.perform(post("/login").session(f.sesion())
                    .param(f.csrf().getParameterName(), f.csrf().getToken())
                    .param("username", "lrojas").param("password", UsuarioServiceImpl.CONTRASENA_DEMO))
                    .andExpect(redirectedUrl("/login?error=inactivo"));
        } finally { cuenta.setEstado("Activo"); usuarioRepo.saveAndFlush(cuenta); }
    }

    @Test void escrituraSinTokenSeRechazaAunqueHayaSesion() throws Exception {
        mvc.perform(post("/categorias/estado").session(login("ediaz"))
                .param("id", "1").param("estado", "Inactivo"))
                .andExpect(status().isForbidden());
    }

    @Test void logoutInvalidaSesion() throws Exception {
        MockHttpSession sesion = login("ediaz");
        var request = mvc.perform(get("/login").session(sesion)).andReturn().getRequest();
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        mvc.perform(post("/logout").session(sesion)
                .param(token.getParameterName(), token.getToken()))
                .andExpect(redirectedUrl("/login?logout"));
        assertTrue(sesion.isInvalid());
    }

    @Test void ventaUsaResponsableAutenticadoYRechazaReenvio() throws Exception {
        MockHttpSession sesion = login("lrojas");
        var formulario = mvc.perform(get("/ventas/crear").session(sesion)).andReturn();
        var csrf = (CsrfToken) formulario.getRequest().getAttribute(CsrfToken.class.getName());
        String token = (String) formulario.getModelAndView().getModel().get("formularioToken");
        int previas = ventas.listaVentas().size();
        for (int i = 0; i < 2; i++) {
            mvc.perform(post("/ventas/crear").session(sesion)
                    .param(csrf.getParameterName(), csrf.getToken()).param("formularioToken", token)
                    .param("idUsuario", "3").param("idProducto", "PR01").param("cantidad", "1.125"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl(i == 0 ? "/ventas/list" : "/ventas/crear"));
        }
        assertEquals(previas + 1, ventas.listaVentas().size());
        assertEquals(2L, ventas.listaVentas().get(previas).getIdUsuario());
    }

    @Test void inventarioIgnoraResponsableEnviadoPorNavegador() throws Exception {
        MockHttpSession sesion = login("atorres");
        var formulario = mvc.perform(get("/inventario/entrada/crear").session(sesion)).andReturn();
        var csrf = (CsrfToken) formulario.getRequest().getAttribute(CsrfToken.class.getName());
        String token = (String) formulario.getModelAndView().getModel().get("formularioToken");
        int previas = inventario.listaMovimientos().size();
        mvc.perform(post("/inventario/entrada/crear").session(sesion)
                .param(csrf.getParameterName(), csrf.getToken()).param("formularioToken", token)
                .param("idUsuario", "3").param("idProducto", "PR01")
                .param("cantidad", "1.125").param("motivo", "Reconteo físico de prueba"))
                .andExpect(redirectedUrl("/inventario/list"));
        assertEquals(previas + 1, inventario.listaMovimientos().size());
        assertEquals(1L, inventario.listaMovimientos().get(previas).getIdUsuario());
    }

    @Test void formatoNumericoInvalidoMuestraAviso() throws Exception {
        MockHttpSession sesion = login("lrojas");
        var formulario = mvc.perform(get("/ventas/crear").session(sesion)).andReturn();
        var csrf = (CsrfToken) formulario.getRequest().getAttribute(CsrfToken.class.getName());
        String token = (String) formulario.getModelAndView().getModel().get("formularioToken");
        mvc.perform(post("/ventas/crear").session(sesion)
                .param(csrf.getParameterName(), csrf.getToken()).param("formularioToken", token)
                .param("idProducto", "PR01").param("cantidad", "texto"))
                .andExpect(redirectedUrl("/ventas/crear"))
                .andExpect(flash().attribute("mensajeTipo", "error"));
    }

    @Test void asistenciaIgnoraEmpleadoYHorarioManipulados() throws Exception {
        MockHttpSession sesion = login("lrojas");
        var request = mvc.perform(get("/asistencia/mi").session(sesion)).andReturn().getRequest();
        var csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        int otras = asistencia.listaAsistenciasDeEmpleado(1L).size();
        mvc.perform(post("/asistencia/entrada").session(sesion)
                .param(csrf.getParameterName(), csrf.getToken()).param("idEmpleado", "1")
                .param("fecha", "2000-01-01").param("horaEntrada", "01:00"))
                .andExpect(redirectedUrl("/asistencia/mi"));
        assertEquals(otras, asistencia.listaAsistenciasDeEmpleado(1L).size());
        var abierta = asistencia.buscarAsistenciaAbierta(2L);
        assertNotNull(abierta);
        assertEquals(java.time.LocalDate.now(com.example.nexo.shared.config.TiempoConfig.LIMA), abierta.getFecha());
        assertNotEquals(java.time.LocalTime.of(1, 0), abierta.getHoraEntrada());
    }

    @Test void proveedorCrudSeGuardaYFiltraEnMySql() throws Exception {
        var sesion=login("ediaz");var form=mvc.perform(get("/proveedores/crear").session(sesion)).andReturn();
        var csrf=(CsrfToken)form.getRequest().getAttribute(CsrfToken.class.getName());
        String ruc="20"+String.format("%09d",new java.security.SecureRandom().nextInt(1000000000));
        mvc.perform(post("/proveedores/crear").session(sesion).param(csrf.getParameterName(),csrf.getToken())
            .param("ruc",ruc).param("razonSocial","Proveedor HTTP de prueba").param("estado","Activo"))
            .andExpect(redirectedUrl("/proveedores/list"));
        Long id=jdbc.queryForObject("select id_proveedor from proveedor where ruc=?",Long.class,ruc);
        mvc.perform(get("/proveedores/list").session(sesion).param("ruc",ruc)).andExpect(status().isOk())
            .andExpect(model().attributeExists("proveedores"));
        mvc.perform(post("/proveedores/editar").session(sesion).param(csrf.getParameterName(),csrf.getToken())
            .param("id",id.toString()).param("ruc",ruc).param("razonSocial","Proveedor HTTP actualizado").param("estado","Activo"))
            .andExpect(redirectedUrl("/proveedores/list"));
        mvc.perform(post("/proveedores/estado").session(sesion).param(csrf.getParameterName(),csrf.getToken())
            .param("id",id.toString()).param("estado","Inactivo")).andExpect(redirectedUrl("/proveedores/list"));
        assertEquals("Inactivo",jdbc.queryForObject("select estado from proveedor where id_proveedor=?",String.class,id));
        assertEquals("Proveedor HTTP actualizado",jdbc.queryForObject("select razon_social from proveedor where id_proveedor=?",String.class,id));
    }

    @Test void compraHttpVariasLineasPersisteUnaSolaOperacion() throws Exception {
        var sesion=login("ediaz");var form=mvc.perform(get("/compras/crear").session(sesion)).andReturn();
        var csrf=(CsrfToken)form.getRequest().getAttribute(CsrfToken.class.getName());
        String token=(String)form.getModelAndView().getModel().get("formularioToken");
        mvc.perform(post("/compras/crear").session(sesion).param(csrf.getParameterName(),csrf.getToken())
            .param("formularioToken",token).param("idProveedor","1").param("idUsuario","2")
            .param("idProducto","PR01").param("cantidad","1.125").param("precioCompra","4")
            .param("lineas[0].idProducto","PR02").param("lineas[0].cantidad","2").param("lineas[0].precioCompra","5"))
            .andExpect(redirectedUrl("/compras/list"));
        String codigo=com.example.nexo.shared.persistence.Codigos.desdeToken(token,"C",12);
        Long id=jdbc.queryForObject("select id_compra from compra where codigo_compra=?",Long.class,codigo);
        assertEquals(2,jdbc.queryForObject("select count(*) from detalle_compra where id_compra=?",Integer.class,id));
        assertEquals(1,jdbc.queryForObject("select count(*) from movimiento_caja where id_compra=?",Integer.class,id));
        assertEquals(3L,jdbc.queryForObject("select id_usuario from compra where id_compra=?",Long.class,id));
    }

    @Test void vendedorNoPuedeCerrarCajaNiModificarProveedor() throws Exception {
        var sesion=login("lrojas");var request=mvc.perform(get("/login").session(sesion)).andReturn().getRequest();
        var csrf=(CsrfToken)request.getAttribute(CsrfToken.class.getName());
        mvc.perform(post("/finanzas/caja/cerrar").session(sesion).param(csrf.getParameterName(),csrf.getToken())).andExpect(status().isForbidden());
        mvc.perform(post("/proveedores/estado").session(sesion).param(csrf.getParameterName(),csrf.getToken()).param("id","1").param("estado","Inactivo")).andExpect(status().isForbidden());
    }

    @Test void proveedorInexistenteNoProduceBucleDeRedireccion() throws Exception {
        mvc.perform(get("/proveedores/editar").session(login("ediaz")).param("id","9223372036854775807"))
            .andExpect(redirectedUrl("/proveedores/list")).andExpect(flash().attribute("mensaje","Proveedor inexistente."));
    }
}
