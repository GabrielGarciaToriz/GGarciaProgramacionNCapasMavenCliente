import { limpiarEstilos, marcarExito, mostrarError } from "../Helpers/HelpersUI.js";
const regexTelefono = /^(\+\d{1,3}\s?)?(\(?\d{2,3}\)?[\s.-]?)?\d{3,4}[\s.-]?\d{4}$/;
const msgError = "Formato no válido";
const msgCorrect = "Correcto"
export function validarTelefono(input, event) {
    const tecla = event.key;
    const regexTeclasPermitidas = /^[0-9\s\+\-\.\(\)]$/;
    if (tecla.length === 1 && !regexTeclasPermitidas.test(tecla)) {
        event.preventDefault();
    }
    limpiarEstilos(input)
}

export function validarTelefonoBlur(input) {
    const  telefono = $(input).val().trim();
    if (telefono === "") {
        limpiarEstilos(input);
        return;
    }

    if (regexTelefono.test(telefono)) {
        marcarExito(input, msgCorrect)
    } else {
        mostrarError(input, msgError)
    }
}