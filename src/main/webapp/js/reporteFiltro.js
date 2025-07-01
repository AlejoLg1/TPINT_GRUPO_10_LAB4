document.addEventListener("DOMContentLoaded", function () {
    const input = document.getElementById("filtroTexto");
    const tabla = document.querySelector(".tabla-cuentas tbody");

    if (!input || !tabla) return;

    input.addEventListener("keyup", function () {
        const filtro = input.value.toLowerCase();
        const filas = tabla.getElementsByTagName("tr");

        Array.from(filas).forEach(fila => {
            const texto = fila.textContent.toLowerCase();
            fila.style.display = texto.includes(filtro) ? "" : "none";
        });
    });
});