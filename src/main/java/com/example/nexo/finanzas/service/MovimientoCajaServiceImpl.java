package com.example.nexo.finanzas.service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.nexo.finanzas.model.*;
import com.example.nexo.finanzas.repository.*;
import com.example.nexo.shared.config.TiempoConfig;
@Service @Transactional
public class MovimientoCajaServiceImpl implements MovimientoCajaService {
 private final ConceptoMovimientoRepository conceptos; private final MovimientoCajaRepository movimientos; private final CajaDiariaService cajas;
 public MovimientoCajaServiceImpl(ConceptoMovimientoRepository conceptos,MovimientoCajaRepository movimientos,CajaDiariaService cajas) {this.conceptos=conceptos;this.movimientos=movimientos;this.cajas=cajas;}
 public List<MovimientoCaja> listaMovimientos() {return movimientos.findAll();}
 public List<MovimientoCaja> listaIngresos() {return listaMovimientos().stream().filter(m->"Ingreso".equals(m.getTipo())).toList();}
 public List<MovimientoCaja> listaEgresos() {return listaMovimientos().stream().filter(m->"Egreso".equals(m.getTipo())).toList();}
 public MovimientoCaja buscarMovimientoPorId(String id) {return id==null?null:movimientos.buscarCodigo(id).orElse(null);}
    public List<ConceptoMovimiento> listaConceptos() {
        return conceptos.findAll();
    }

    public ConceptoMovimiento buscarConceptoPorId(String id) {
        if (id == null) {
            return null;
        }
        for (ConceptoMovimiento concepto : conceptos.findAll()) {
            if (id.equals(concepto.getId())) {
                return concepto;
            }
        }
        return null;
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public String crearConcepto(ConceptoMovimiento concepto) {
        String error = validarConcepto(concepto, null);
        if (error != null) {
            return error;
        }
        normalizarConcepto(concepto);
        concepto.setClave(null);
        concepto.setId(com.example.nexo.shared.persistence.Codigos.nuevo("CE",10));
        conceptos.save(concepto);
        return null; // alta completada
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public String editarConcepto(ConceptoMovimiento concepto) {
        ConceptoMovimiento existente = buscarConceptoPorId(concepto == null ? null : concepto.getId());
        if (existente == null) {
            return "No se encontró el concepto solicitado.";
        }
        String error = validarConcepto(concepto, concepto.getId());
        if (error != null) {
            return error;
        }
        if (!existente.getTipo().equals(concepto.getTipo()) && movimientos.findAll().stream().anyMatch(m->m.getIdConcepto().equals(existente.getId()))) {
            return "El tipo de un concepto con movimientos históricos no puede cambiarse.";
        }
        normalizarConcepto(concepto);
        existente.setNombre(concepto.getNombre());
        existente.setTipo(concepto.getTipo());
        existente.setEstado(concepto.getEstado());
        return null; // edición completada: se conserva el código y el historial
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public String cambiarEstadoConcepto(String id, String estado) {
        ConceptoMovimiento concepto = buscarConceptoPorId(id);
        if (concepto == null) {
            return "No se encontró el concepto solicitado.";
        }
        if (!esEstadoValido(estado)) {
            return "El estado debe ser Activo o Inactivo.";
        }
        // RN02: el concepto se conserva con su nuevo estado; no se elimina
        concepto.setEstado(estado);
        return null;
    }

    /**
     * Reglas comunes de alta y edición (F23 y F25): nombre obligatorio de 3
     * a 40 caracteres, único ignorando mayúsculas y espacios; tipo del
     * catálogo (Ingreso / Egreso); estado del catálogo (Activo / Inactivo).
     * 'idExcluido' es el código que no compite consigo mismo al unicidad.
     */
    private String validarConcepto(ConceptoMovimiento concepto, String idExcluido) {
        if (concepto == null || concepto.getNombre() == null
                || com.example.nexo.shared.validation.TextoNormalizado.nombre(concepto.getNombre()).isEmpty()) {
            return "El nombre del concepto es obligatorio.";
        }
        String nombre = com.example.nexo.shared.validation.TextoNormalizado.nombre(concepto.getNombre());
        if (nombre.length() < 3 || nombre.length() > 40) {
            return "El nombre del concepto debe tener entre 3 y 40 caracteres.";
        }
        // Unicidad ignorando mayúsculas y espacios (como en los demás catálogos)
        String normalizado = normalizarTexto(nombre);
        for (ConceptoMovimiento existente : conceptos.findAll()) {
            boolean mismo = idExcluido != null && idExcluido.equals(existente.getId());
            if (!mismo && normalizado.equals(normalizarTexto(existente.getNombre()))) {
                return "Ya existe un concepto con el nombre «" + existente.getNombre() + "».";
            }
        }
        if (!"Ingreso".equals(concepto.getTipo()) && !"Egreso".equals(concepto.getTipo())) {
            return "El tipo debe ser Ingreso o Egreso.";
        }
        if (!esEstadoValido(concepto.getEstado())) {
            return "El estado debe ser Activo o Inactivo.";
        }
        return null; // sin errores
    }

    private boolean esEstadoValido(String estado) {
        return "Activo".equals(estado) || "Inactivo".equals(estado);
    }

    /** Guarda el nombre sin los espacios sobrantes. */
    private void normalizarConcepto(ConceptoMovimiento concepto) {
        concepto.setNombre(com.example.nexo.shared.validation.TextoNormalizado.nombre(concepto.getNombre()));
    }

    private String normalizarTexto(String texto) {
        return com.example.nexo.shared.validation.TextoNormalizado.clave(texto);
    }

    /** Continúa la numeración de códigos: CE01, CE02 → CE03. */



 public BigDecimal apertura() {var c=cajas.actual();return c==null?BigDecimal.ZERO:c.getSaldoApertura();}
 private BigDecimal hoy(List<MovimientoCaja> datos) {return datos.stream().filter(m->m.getFechaHora().toLocalDate().equals(LocalDate.now(TiempoConfig.LIMA))).map(MovimientoCaja::getMonto).reduce(BigDecimal.ZERO,BigDecimal::add);}
 public BigDecimal totalIngresos() {return hoy(listaIngresos());}
 public BigDecimal totalEgresos() {return hoy(listaEgresos());}
 public BigDecimal saldoActual() {return cajas.saldo(cajas.actual());}
}
