package com.example.nexo.proveedor.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.nexo.proveedor.model.Proveedor;
import com.example.nexo.proveedor.service.ProveedorService;
import static com.example.nexo.shared.validation.ReglaNegocioException.exigir;
@Controller @RequestMapping("/proveedores")
public class ProveedorController {
    private final ProveedorService servicio;
    public ProveedorController(ProveedorService servicio) {this.servicio=servicio;}
    @GetMapping("/list")
    public String listar(@RequestParam(required=false) String ruc,@RequestParam(required=false) String nombre,@RequestParam(required=false) String estado,Model model) {
        model.addAttribute("proveedores",servicio.listar(ruc,nombre,estado));return "proveedor/lista";
    }
    @GetMapping("/crear") public String crear(Model model) {model.addAttribute("proveedor",new Proveedor());return "proveedor/formulario";}
    @GetMapping("/editar") public String editar(@RequestParam Long id,Model model,RedirectAttributes flash) {
        Proveedor p=servicio.buscar(id);
        if(p==null){flash.addFlashAttribute("mensaje","Proveedor inexistente.");return "redirect:/proveedores/list";}
        model.addAttribute("proveedor",p);return "proveedor/formulario";
    }
    @PostMapping({"/crear","/editar"})
    public String guardar(@ModelAttribute Proveedor proveedor,BindingResult errores,RedirectAttributes flash) {
        exigir(!errores.hasErrors(),"Revisa los datos del proveedor.");servicio.guardar(proveedor);
        flash.addFlashAttribute("mensaje","Proveedor guardado en MySQL.");return "redirect:/proveedores/list";
    }
    @PostMapping("/estado") public String estado(@RequestParam Long id,@RequestParam String estado,RedirectAttributes flash) {
        servicio.cambiarEstado(id,estado);flash.addFlashAttribute("mensaje","Estado actualizado; el historial se conserva.");return "redirect:/proveedores/list";
    }
}
