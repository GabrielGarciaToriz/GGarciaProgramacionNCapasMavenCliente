import { limpiarEstilos, marcarExito, mostrarError } from "../Helpers/HelpersUI.js";
//EXPRESION REGULAR PARA SOLO USAR LETRAS
const regexLetras = /^[a-zA-Z\s]+$/
const msgCorrect = "Validado";
const msgError = "Solo letras";
export function SoloLetras(input, event) {
    var valorCompleto = $(input).val() + event.key;
    //EXPRESON REGULAR PARA SOLO ACEPTAR CARACTERES Y ESPACIOS
    if (!regexLetras.test(valorCompleto)) {
        event.preventDefault();
        marcarExito(input, msgCorrect)
    } else {
        limpiarEstilos(input);
    }
}

export function SoloLetrasBlur(input) {
    const valor = $(input).val();
    if (valor === "") {
        limpiarEstilos(input);
        return;
    }
    if (regexLetras.test(valor)) {
        marcarExito(input, msgCorrect)
    } else {
        mostrarError(input, msgError)
    }
}