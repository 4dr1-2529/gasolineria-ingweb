package com.example.nexo.proveedor.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.nexo.proveedor.model.Proveedor;
import com.example.nexo.proveedor.repository.ProveedorRepository;
import static com.example.nexo.shared.validation.ReglaNegocioException.exigir;
import com.example.nexo.shared.validation.TextoNormalizado;

@Service @Transactional
public class ProveedorService {
    private final ProveedorRepository repo;
    public ProveedorService(ProveedorRepository repo) { this.repo=repo; }
    @PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public List<Proveedor> listar(String ruc,String nombre,String estado) {
        return repo.findAll().stream().filter(p->p.getRuc().contains(TextoNormalizado.nombre(ruc)))
                .filter(p->TextoNormalizado.clave(p.getRazonSocial()).contains(TextoNormalizado.clave(nombre)))
                .filter(p->estado==null || estado.isBlank() || estado.equals(p.getEstado())).toList();
    }
    public Proveedor buscar(Long id) { return id==null?null:repo.findById(id).orElse(null); }
    public List<Proveedor> activos() { return repo.findAll().stream().filter(p->"Activo".equals(p.getEstado())).toList(); }
    @PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public void guardar(Proveedor formulario) {
        exigir(formulario.getRuc()!=null && formulario.getRuc().matches("[0-9]{11}"),"El RUC debe tener 11 dígitos.");
        String nombre=TextoNormalizado.nombre(formulario.getRazonSocial());
        exigir(nombre.length()>=3 && nombre.length()<=120,"La razón social debe tener entre 3 y 120 caracteres.");
        exigir(List.of("Activo","Inactivo").contains(String.valueOf(formulario.getEstado())),"Estado no válido.");
        exigir(repo.findAll().stream().noneMatch(p->p.getRuc().equals(formulario.getRuc()) && !p.getId().equals(formulario.getId())),"El RUC ya está registrado.");
        exigir(formulario.getContacto()==null || formulario.getContacto().length()<=80,"El contacto no debe superar 80 caracteres.");
        exigir(formulario.getDireccion()==null || formulario.getDireccion().length()<=200,"La dirección no debe superar 200 caracteres.");
        exigir(formulario.getTelefono()==null || formulario.getTelefono().isBlank() || formulario.getTelefono().matches("[+0-9 ()-]{7,15}"),"Teléfono no válido.");
        exigir(formulario.getCorreo()==null || formulario.getCorreo().isBlank() || (formulario.getCorreo().length()<=120 && formulario.getCorreo().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")),"Correo no válido.");
        Proveedor p=formulario.getId()==null?new Proveedor():buscar(formulario.getId());
        exigir(p!=null,"El proveedor no existe.");
        p.setRuc(formulario.getRuc());p.setRazonSocial(nombre);p.setContacto(formulario.getContacto());
        p.setTelefono(formulario.getTelefono());p.setCorreo(formulario.getCorreo());p.setDireccion(formulario.getDireccion());p.setEstado(formulario.getEstado());
        repo.saveAndFlush(p);
    }
    @PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public void cambiarEstado(Long id,String estado) {
        Proveedor p=buscar(id);exigir(p!=null,"Proveedor inexistente.");
        exigir(List.of("Activo","Inactivo").contains(String.valueOf(estado)),"Estado no válido.");p.setEstado(estado);
    }
}
