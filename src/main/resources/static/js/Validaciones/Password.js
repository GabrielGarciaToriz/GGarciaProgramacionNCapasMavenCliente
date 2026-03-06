import { limpiarEstilos, marcarExito, mostrarError } from "../Helpers/HelpersUI.js";
const regexPassword = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&.])[A-Za-z\d@$!%*?&.]{8,16}$/;
const msgError = "Debe tener 8-16 caracteres, 1 mayúscula, 1 número y 1 símbolo (@$!%*?&.)";
const msgCorrect = "La contraseña cumple los parametros de seguridad";
export function validarPassword(input, event) {
    const tecla = event.key;
    if(tecla === " "){
        event.preventDefault();
    }
    limpiarEstilos(input);
}

export function validarPasswordBlur(input) {
    var password = $(input).val().trim()
    if (password === "") {
        limpiarEstilos(input)
    }
    if (regexPassword.test(password)) {
        marcarExito(input, msgCorrect)
    } else {
        mostrarError(input, msgError)
    }
}
