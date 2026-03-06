import { limpiarEstilos, marcarExito, mostrarError } from "../Helpers/HelpersUI.js";
//EXPRESION REGULAR PARA SOLO USAR LETRAS
const regexUsuario = /^[a-zA-Z0-9]+$/
const msgCorrect = "Validado";
const msgError = "Usuario no valido";
export function Usuario(input, event) {
    const tecla = key.event;
    //EXPRESON REGULAR PARA SOLO ACEPTAR CARACTERES Y ESPACIOS
    if (tecla.length === 1 && !regexUsuario.test(tecla)) {
        event.preventDefault();
    }
    limpiarEstilos(input);

}

export function UsuarioBlur(input) {
    const valor = $(input).val().trim();
    if (valor === "") {
        limpiarEstilos(input);
        return;
    }
    if (regexUsuario.test(valor)) {
        marcarExito(input, msgCorrect)
    } else {
        mostrarError(input, msgError)
    }
}