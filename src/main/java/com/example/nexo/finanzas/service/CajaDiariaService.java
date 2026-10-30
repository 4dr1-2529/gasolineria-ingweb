package com.example.nexo.finanzas.service;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.nexo.auth.service.ActorActual;
import com.example.nexo.finanzas.model.*;
import com.example.nexo.finanzas.repository.*;
import static com.example.nexo.shared.validation.ReglaNegocioException.exigir;
import com.example.nexo.shared.config.TiempoConfig;

@Service @Transactional
public class CajaDiariaService {
    private final CajaDiariaRepository cajas;
    private final MovimientoCajaRepository movimientos;
    private final ActorActual actor;
    private final Clock reloj;
    public CajaDiariaService(CajaDiariaRepository cajas,MovimientoCajaRepository movimientos,ActorActual actor,Clock reloj) {
        this.cajas=cajas;this.movimientos=movimientos;this.actor=actor;this.reloj=reloj.withZone(TiempoConfig.LIMA);
    }
    public CajaDiaria actual() { return cajas.findByFecha(LocalDate.now(reloj)).orElse(null); }
    /** Debe invocarse dentro de la transacción de la operación. El cierre toma el mismo bloqueo. */
    public CajaDiaria abiertaBloqueada() {
        CajaDiaria c=cajas.bloquear(LocalDate.now(reloj)).orElse(null);
        exigir(c!=null && "ABIERTA".equals(c.getEstado()),"Debes abrir la caja de hoy antes de registrar compras o ventas.");return c;
    }
    @PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public void abrir(BigDecimal saldo) {
        exigir(saldo!=null && saldo.signum()>=0 && saldo.stripTrailingZeros().scale()<=2 && saldo.compareTo(new BigDecimal("9999999999.99"))<=0,"Saldo de apertura inválido.");
        exigir(actual()==null,"Ya existe una caja para esta fecha; no puede abrirse dos veces.");
        CajaDiaria c=new CajaDiaria();c.setFecha(LocalDate.now(reloj));c.setSaldoApertura(saldo);c.setEstado("ABIERTA");c.setUsuarioApertura(actor.usuario());c.setFechaApertura(LocalDateTime.now(reloj).withNano(0));cajas.saveAndFlush(c);
    }
    @PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public void cerrar() {
        CajaDiaria c=abiertaBloqueada();c.setSaldoCierre(saldo(c));c.setEstado("CERRADA");c.setUsuarioCierre(actor.usuario());c.setFechaCierre(LocalDateTime.now(reloj).withNano(0));
    }
    public List<CajaDiaria> listar() { return cajas.findAll(); }
    public BigDecimal saldo(CajaDiaria c) {
        if(c==null)return BigDecimal.ZERO;
        return c.getSaldoApertura().add(movimientos.findAll().stream().filter(m->m.getCaja().getId().equals(c.getId()))
                .map(m->"Ingreso".equals(m.getTipo())?m.getMonto():m.getMonto().negate()).reduce(BigDecimal.ZERO,BigDecimal::add));
    }
}
