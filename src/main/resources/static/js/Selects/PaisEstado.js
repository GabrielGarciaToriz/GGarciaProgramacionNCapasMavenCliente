import { cargarSelectCascada, API_BASE_URL, API_ENDPOINTS } from '../Helpers/HelpersUI.js';

export function PaisEstado() {
    const $selectPais = $("#selectPais");
    const $selectEstado = $("#selectEstado");
    const $selectMunicipio = $("#selectMunicipio");
    const $selectColonia = $("#selectColonia");

    $selectPais.change(function () {
        cargarSelectCascada(
            $(this).val(), 
            `${API_BASE_URL}${API_ENDPOINTS.estado}/`, 
            $selectEstado, 
            "Selecciona un estado", 
            "idEstado",   
            "nombre",    
            [
                { $el: $selectMunicipio, texto: "Selecciona un municipio" },
                { $el: $selectColonia, texto: "Selecciona una colonia" }
            ]
        );
    });
}