function copiarCBU(elemento) {
    const texto = elemento.textContent;

    navigator.clipboard.writeText(texto).then(() => {
        const original = elemento.textContent;
        elemento.textContent = 'Copiado ✅';

        setTimeout(() => {
            elemento.textContent = original;
        }, 1000);
    }).catch(err => {
        console.error('Error al copiar: ', err);
    });
}
