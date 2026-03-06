let ui = {};
let tipoArchivoEsperado = "";

function configurarSubida(extension, claseAgregar, claseRemover) {
    tipoArchivoEsperado = extension;
    ui.formularioArchivo.style.display = "block";
    ui.inputArchivo.setAttribute("accept", `.${extension}`);
    ui.inputArchivo.value = "";
    ui.btnCargaMasiva.classList.replace(claseRemover, claseAgregar);
}

export function initCargaMasiva() {

    ui = {
        formularioArchivo: document.getElementById("formularioArchivo"),
        inputArchivo: document.getElementById("archivo"),
        btnCargaMasiva: document.getElementById("cargaMasiva"),
        btnExcel: document.getElementById("btnExcel"),
        btnTxt: document.getElementById("btnTxt")
    };

    // 2. Asignamos los eventos
    ui.btnExcel.addEventListener("click", () => configurarSubida("xlsx", "btn-success", "btn-primary"));
    ui.btnTxt.addEventListener("click", () => configurarSubida("txt", "btn-primary", "btn-success"));

    ui.inputArchivo.addEventListener("change", function () {
        const archivoSeleccionado = this.files[0];
        if (!archivoSeleccionado) return;

        if (!archivoSeleccionado.name.toLowerCase().endsWith(`.${tipoArchivoEsperado}`)) {
            alert(`Por favor, seleccione un archivo ${tipoArchivoEsperado.toUpperCase()} válido.`);
            this.value = "";
        }
    });
}