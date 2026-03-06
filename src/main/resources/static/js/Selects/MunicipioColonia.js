import { cargarSelectCascada } from '../Helpers/HelpersUI.js';

export function MunicipioColonia() {
    const $selectMunicipio = $("#selectMunicipio");
    const $selectColonia = $("#selectColonia");

    $selectMunicipio.change(function () {
        cargarSelectCascada(
            $(this).val(),
            "/usuario/getColoniabyMunicipio/",
            $selectColonia,
            "Selecciona una colonia",
            "IdColonia",
            "Nombre"
            // No hay dependientes más abajo, así que no enviamos el array
        );
    });
}