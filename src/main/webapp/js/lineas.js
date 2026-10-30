document.addEventListener("DOMContentLoaded", () => {
 const boton = document.getElementById("agregar-linea"), plantilla = document.getElementById("plantilla-linea"), destino = document.getElementById("lineas-adicionales");
 if (!boton || !plantilla || !destino) return;
 let indice = 0;
 boton.addEventListener("click", () => {
  const nodo = plantilla.content.cloneNode(true);
  nodo.querySelectorAll("[name]").forEach(campo => campo.name = campo.name.replace("__indice__", String(indice)));
  nodo.querySelector("[data-quitar-linea]").addEventListener("click", e => e.target.closest("fieldset").remove());
  destino.appendChild(nodo); indice++;
 });
});
