<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <form id="confirmModalForm" method="post">
        <div class="modal-header">
          <h5 class="modal-title" id="confirmModalLabel">Confirmación</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body" id="confirmModalBody">
          ¿Estás seguro que deseas continuar?
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
          <button type="submit" class="btn btn-primary" id="confirmModalBtn">Sí, continuar</button>
        </div>
      </form>
    </div>
  </div>
</div>

<script>
  let confirmForm = document.getElementById('confirmModalForm');
  let confirmModalBody = document.getElementById('confirmModalBody');
  let confirmModalBtn = document.getElementById('confirmModalBtn');

  function abrirConfirmacion({ action, method = 'post', mensaje, botonClase = 'btn-primary', botonTexto = 'Sí, continuar', inputs = [] }) {
    const modal = new bootstrap.Modal(document.getElementById('confirmModal'));
    confirmForm.action = action;
    confirmForm.method = method;
    confirmModalBody.innerText = mensaje;
    confirmModalBtn.className = `btn ${botonClase}`;
    confirmModalBtn.innerText = botonTexto;

    // Eliminar inputs previos
    confirmForm.querySelectorAll('input[name]').forEach(e => e.remove());

    // Agregar nuevos inputs ocultos
    inputs.forEach(({ name, value }) => {
      const hidden = document.createElement('input');
      hidden.type = 'hidden';
      hidden.name = name;
      hidden.value = value;
      confirmForm.appendChild(hidden);
    });

    modal.show();
  }
</script>
