import { cargarSelectCascada } from '../Helpers/HelpersUI.js'; // Ajusta la ruta según tu proyecto

export function PaisEstado() {
    const $selectPais = $("#selectPais");
    const $selectEstado = $("#selectEstado");
    const $selectMunicipio = $("#selectMunicipio");
    const $selectColonia = $("#selectColonia");

    $selectPais.change(function () {
        cargarSelectCascada(
            $(this).val(), 
            "/usuario/getEstadoByPais/", 
            $selectEstado, 
            "Selecciona un estado", 
            "IdEstado",   
            "Nombre",    
            [
                { $el: $selectMunicipio, texto: "Selecciona un municipio" },
                { $el: $selectColonia, texto: "Selecciona una colonia" }
            ]
        );
    });
}