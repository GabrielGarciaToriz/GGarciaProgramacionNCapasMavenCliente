import { cargarSelectCascada, API_BASE_URL, API_ENDPOINTS } from '../Helpers/HelpersUI.js';

export function EstadoMunicipio() {
    const $selectEstado = $("#selectEstado");
    const $selectMunicipio = $("#selectMunicipio");
    const $selectColonia = $("#selectColonia");

    $selectEstado.change(function () {
        cargarSelectCascada(
            $(this).val(),
            `${API_BASE_URL}${API_ENDPOINTS.municipio}/`,
            $selectMunicipio,
            "Selecciona un municipio",
            "idMunicipio", 
            "nombre",      
            [
                { $el: $selectColonia, texto: "Selecciona una colonia" }
            ]
        );
    });
}