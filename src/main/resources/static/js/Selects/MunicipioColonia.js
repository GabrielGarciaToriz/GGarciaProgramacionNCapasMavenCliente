import { cargarSelectCascada, API_BASE_URL } from '../Helpers/HelpersUI.js';

export function MunicipioColonia() {
    const $selectMunicipio = $("#selectMunicipio");
    const $selectColonia = $("#selectColonia");

    $selectMunicipio.change(function () {
        cargarSelectCascada(
            $(this).val(),
            `${API_BASE_URL}/api/colonia/`,
            $selectColonia,
            "Selecciona una colonia",
            "IdColonia",
            "Nombre"
            // No hay dependientes más abajo, así que no enviamos el array
        );
    });
}