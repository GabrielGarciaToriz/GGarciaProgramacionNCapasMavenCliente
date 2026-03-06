import { limpiarEstilos, marcarExito, mostrarError } from "../Helpers/HelpersUI.js";
//EXPRESION REFULAR PARA VALIDAR EL CORREO
const regexCorreo = /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
const msgError = "El correo no tiene un formato valido (ejemplo@dominio.com)";
const msgCorrect = "El correo tiene el formato correcto";

export function validarCorreo(input, event) {
    const tecla = event.key;
    if (tecla === " ") {
        event.preventDefault();
    }
    limpiarEstilos(input);

}

export function validarCorreoBlur(input) {
    const correo = $(input).val().trim().toLowerCase();
    if (correo === "") {
        limpiarEstilos(input);
        return;
    }
    if (regexCorreo.test(correo)) {
        marcarExito(input, msgCorrect)
    } else {
        mostrarError(input, msgError)
    }

}