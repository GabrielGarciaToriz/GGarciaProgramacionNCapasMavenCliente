import { cargarSelectCascada } from '../Helpers/HelpersUI.js';

export function EstadoMunicipio() {
    const $selectEstado = $("#selectEstado");
    const $selectMunicipio = $("#selectMunicipio");
    const $selectColonia = $("#selectColonia");

    $selectEstado.change(function () {
        cargarSelectCascada(
            $(this).val(),
            "/usuario/getMunicipioByEstado/",
            $selectMunicipio,
            "Selecciona un municipio",
            "IdMunicipio",
            "Nombre",
            [
                { $el: $selectColonia, texto: "Selecciona una colonia" }
            ]
        );
    });
}