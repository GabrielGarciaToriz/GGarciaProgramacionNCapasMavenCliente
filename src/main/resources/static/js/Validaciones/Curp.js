import { limpiarEstilos, marcarExito, mostrarError } from "../Helpers/HelpersUI.js";
const regexCurp = /^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$/;

const msgCorrect = "CURP CORRECTO"
const msgError = "CURP INCORRECTO"

export function validarCurp(input, event) {
    const tecla = event.key
    const regexTeclasPermitidas = /^[A-Z0-9]$/;
    if (tecla.length === 1 && !regexTeclasPermitidas.test(tecla)) {
        event.oreventDefault();
    }
    limpiarEstilos(input);
}

export function validarCurpBlur(input) {
    var curp = $(input).val().toUpperCase().trim();
    $(input).val(curp)
    if (curp === "") {
        limpiarEstilos(input);
        return;
    }

    if (regexCurp.test(curp)) {
        marcarExito(input, msgCorrect)
    } else {
        mostrarError(input, msgError)
    }
}