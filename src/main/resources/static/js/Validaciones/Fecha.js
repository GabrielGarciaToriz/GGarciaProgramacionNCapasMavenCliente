import { teclasPermitidas } from "../Helpers/HelpersUI.js";
export function soloCalendario(input, event) {
    if (teclasPermitidas.includes(event.key)) {
        event.preventDefault()
    }
    try {
        if (typeof input.showPicker === "function") {
            input.showPicker();
        }
    } catch (error) {
        console.log("No es soportado el calendario")
    }
}
export function abrirCalendario(input, event) {
    event.preventDefault()
    try {
        if (typeof input.showPicker === "function") {
            input.showPicker();
        } else {
            input.focus();
            input.click();
        }
    } catch (error) {
        console.log("No es soportado el calendario")
    }
}
