import { limpiarEstilos, marcarExito, mostrarError } from "../Helpers/HelpersUI.js";
const regexDirecciones = /^[a-zA-Z0-9\s]+$/;
const msgError = "Incorrecto ";
const msgCorrect = "Correcto";
export function validarDirecciones(input, event) {
    var direccion = $(input).val() + event.key;
    if (regexDirecciones.test(direccion)) {
        marcarExito(input, msgCorrect)
    } else {
        mostrarError(input, msgError)
    }
}

export function validarDireccionesBlur(input) {
    var direccion = $(input).val();
    if (direccion === "") {
        limpiarEstilos(input);
        return;
    }
    if (regexDirecciones.test(direccion)) {
        marcarExito(input, msgCorrect)
    } else {
        mostrarError(input, msgError)
    }
};