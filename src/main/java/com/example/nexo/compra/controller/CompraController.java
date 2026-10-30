package com.example.nexo.compra.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import jakarta.servlet.http.HttpSession;
import com.example.nexo.shared.web.FormularioUnico;
import org.springframework.security.core.Authentication;
import com.example.nexo.auth.service.ResponsableSesion;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.nexo.compra.model.Compra;
import com.example.nexo.compra.model.DetalleCompra;
import com.example.nexo.compra.service.CompraService;
import com.example.nexo.empleado.model.Empleado;
import com.example.nexo.empleado.service.EmpleadoService;
import com.example.nexo.producto.model.Producto;
import com.example.nexo.producto.service.ProductoService;
import com.example.nexo.usuario.model.Usuario;
import com.example.nexo.usuario.service.UsuarioService;


/**
 * Compras: consulta del listado, registro con su detalle y vista de detalle.
 * El controller sólo prepara datos para las vistas y delega el registro en
 * CompraService (RN03 y RN04).
 */
@Controller
@RequestMapping("/compras")
public class CompraController {

    /** Formato de la fecha informativa del formulario (dd/MM/yyyy). */
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final com.example.nexo.proveedor.service.ProveedorService proveedores;
    private final CompraService compraService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final EmpleadoService empleadoService;

    public CompraController(CompraService compraService, ProductoService productoService,
            UsuarioService usuarioService, EmpleadoService empleadoService, com.example.nexo.proveedor.service.ProveedorService proveedores) {
        this.compraService = compraService;
        this.proveedores=proveedores;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.empleadoService = empleadoService;
    }

    @GetMapping("/list")
    public String listarCompras(@RequestParam(required=false) Long idProveedor,@RequestParam(required=false) Long idResponsable,
            @RequestParam(required=false) @org.springframework.format.annotation.DateTimeFormat(iso=org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required=false) @org.springframework.format.annotation.DateTimeFormat(iso=org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required=false) String estado,@RequestParam(required=false) String idProducto,Model model) {
        List<Compra> compras = compraService.filtrar(idProveedor,idResponsable,desde,hasta,estado,idProducto);
        Set<String> codigos=compras.stream().map(Compra::getId).collect(java.util.stream.Collectors.toSet());
        model.addAttribute("proveedoresFiltro",proveedores.listar(null,null,null));
        Map<Long,String> responsablesFiltro=new HashMap<>();
        for(var u:usuarioService.listaUsuarios())responsablesFiltro.put(u.getId(),nombreResponsable(u.getId()));
        model.addAttribute("responsablesFiltro",responsablesFiltro);
        model.addAttribute("productosFiltro",productoService.listaProductos());
        List<DetalleCompra> detalles = compraService.listaDetalles().stream().filter(d->codigos.contains(d.getIdCompra())).toList();
        Map<String, Producto> productos = productosPorId();

        // Datos que la vista necesita por compra: sus combustibles y sus litros
        Map<String, String> combustibles = new HashMap<>();
        Map<String, BigDecimal> litros = new HashMap<>();
        Set<String> productosComprados = new LinkedHashSet<>();
        BigDecimal totalMonto = BigDecimal.ZERO;
        BigDecimal totalLitros = BigDecimal.ZERO;

        for (Compra compra : compras) {
            if (compra.getTotal() != null) {
                totalMonto = totalMonto.add(compra.getTotal());
            }
        }
        for (DetalleCompra detalle : detalles) {
            totalLitros = totalLitros.add(detalle.getCantidad());
            Producto producto = productos.get(detalle.getIdProducto());
            String nombre = producto == null ? "" : producto.getNombre();
            if (producto != null) {
                productosComprados.add(nombre);
            }
            String anteriores = combustibles.get(detalle.getIdCompra());
            combustibles.put(detalle.getIdCompra(),
                    (anteriores == null || anteriores.isEmpty()) ? nombre : anteriores + ", " + nombre);
            BigDecimal previos = litros.get(detalle.getIdCompra());
            litros.put(detalle.getIdCompra(),
                    (previos == null ? BigDecimal.ZERO : previos).add(detalle.getCantidad()));
        }

        model.addAttribute("compras", compras);
        model.addAttribute("detalles", detalles);
        model.addAttribute("combustiblesPorCompra", combustibles);
        model.addAttribute("litrosPorCompra", litros);
        model.addAttribute("productosComprados", String.join(", ", productosComprados));
        model.addAttribute("totalMonto", totalMonto);
        model.addAttribute("totalLitros", totalLitros);
        return "compra/lista";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model, HttpSession sesion) {
        // Tras un rechazo conserva lo digitado (los atributos vienen por flash)
        if (!model.containsAttribute("compra")) {
            model.addAttribute("compra", new Compra());
        }
        if (!model.containsAttribute("detalle")) {
            model.addAttribute("detalle", new DetalleCompra());
        }
        // Fecha de registro (sólo informativa en la vista): la fija el servidor
        model.addAttribute("proveedores",proveedores.activos());
        model.addAttribute("fechaRegistro", LocalDate.now(com.example.nexo.shared.config.TiempoConfig.LIMA).format(FORMATO_FECHA));
        // RN02 · Sólo los combustibles con estado Activo pueden comprarse
        model.addAttribute("productos", productosActivos());
        model.addAttribute("responsables", responsables());
        model.addAttribute("formularioToken", FormularioUnico.emitir(sesion, "compra"));
        return "compra/crear";
    }

    @PostMapping("/crear")
    public String crearCompra(@ModelAttribute("compra") Compra compra, BindingResult resultadoCompra,
            @ModelAttribute("detalle") DetalleCompra detalle, BindingResult resultadoDetalle,
            @ModelAttribute("lineasOperacion") com.example.nexo.shared.web.LineasOperacion lineas, BindingResult resultadoLineas,
            Authentication autenticacion, HttpSession sesion,
            @RequestParam("formularioToken") String formularioToken, RedirectAttributes redirect) {
        if (resultadoCompra.hasErrors() || resultadoDetalle.hasErrors() || resultadoLineas.hasErrors()) {
            redirect.addFlashAttribute("mensaje", "Revisa los campos numéricos y los formatos del formulario.");
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/compras/crear";
        }
        // La fecha no viaja en el formulario: la registra el Service con hora del servidor
        compra.setId(com.example.nexo.shared.persistence.Codigos.desdeToken(formularioToken,"C",12));
        compra.setIdUsuario(ResponsableSesion.id(autenticacion, usuarioService));
        String error = FormularioUnico.ejecutar(sesion, "compra", formularioToken,
                () -> compraService.crearCompra(compra, lineas.compras(detalle)));
        if (error != null) {
            // Compra rechazada: se regresa al formulario conservando lo digitado
            redirect.addFlashAttribute("compra", compra);
            redirect.addFlashAttribute("detalle", detalle);
            redirect.addFlashAttribute("mensaje", error);
            redirect.addFlashAttribute("mensajeTipo", "error");
            return "redirect:/compras/crear";
        }
        redirect.addFlashAttribute("mensaje", "Compra " + compra.getId() + " registrada con todos sus detalles y movimientos de inventario y caja.");
        redirect.addFlashAttribute("mensajeTipo", "ok");
        return "redirect:/compras/list";
    }

    @GetMapping("/detalle")
    public String mostrarDetalle(@RequestParam(value = "id", required = false) String id,
            Model model, RedirectAttributes redirect) {
        Compra compra = compraService.buscarCompraPorId(id);
        if (compra == null) {
            // Id inexistente o ausente: nunca un error 500
            redirect.addFlashAttribute("mensaje", "No se encontró la compra solicitada.");
            return "redirect:/compras/list";
        }
        model.addAttribute("compra", compra);
        model.addAttribute("detalles", compraService.listaDetallesPorCompra(compra.getId()));
        model.addAttribute("productosPorId", productosPorId());
        model.addAttribute("responsable", nombreResponsable(compra.getIdUsuario()));
        return "compra/detalle";
    }

    private Map<String, Producto> productosPorId() {
        Map<String, Producto> productos = new HashMap<>();
        for (Producto producto : productoService.listaProductos()) {
            productos.put(producto.getId(), producto);
        }
        return productos;
    }

    /**
     * Responsables del formulario: cada cuenta de usuario con el nombre
     * completo de su empleado (como los empleados Ana Torres, Luis Rojas y
     * Elena Díaz de la versión 1).
     */
    /**
     * Responsables del formulario: las cuentas Activas con rol "Operador /
     * Vendedor" (RN02: una cuenta Inactiva no se elige en operaciones nuevas).
     */
    private Map<Long, String> responsables() {
        Map<Long, String> nombres = new HashMap<>();
        for (Usuario usuario : usuarioService.listaUsuarios()) {
            if ("Vendedor".equals(usuario.getRol())
                    && "Activo".equals(usuario.getEstado())) {
                nombres.put(usuario.getId(), nombreResponsable(usuario.getId()));
            }
        }
        return nombres;
    }

    /**
     * RN02 · Combustibles que pueden comprarse: sólo los productos con
     * estado Activo, como en la versión 1 (P19).
     */
    private List<Producto> productosActivos() {
        List<Producto> activos = new ArrayList<>();
        for (Producto producto : productoService.listaProductos()) {
            if (productoService.disponibleParaOperacion(producto.getId())) {
                activos.add(producto);
            }
        }
        return activos;
    }

    private String nombreResponsable(Long idUsuario) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            return "";
        }
        Empleado empleado = empleadoService.buscarEmpleadoPorId(usuario.getIdEmpleado());
        if (empleado == null) {
            return usuario.getUsername();
        }
        return empleado.getNombres() + " " + empleado.getApellidos();
    }

}
