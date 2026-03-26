import { API_BASE_URL, API_ENDPOINTS } from "../Helpers/HelpersUI.js";

export function DireccionByCodigoPostal() {
    $("#CodigoPostal").change(function () {
        var codigoPostal = $("#CodigoPostal").val();

        if (codigoPostal !== "") {
            $.ajax({
                url: `${API_BASE_URL}${API_ENDPOINTS.colonia}/codigoPostal/` + codigoPostal,
                type: "GET",
                dataType: "json",
                success: function (data) {
                    if (data && data.correct && data.objects && data.objects.length > 0) {

                        const primeraColonia = data.objects[0];

                        const idPais = primeraColonia.municipio.estado.pais.idPais;
                        const idEstado = primeraColonia.municipio.estado.idEstado;
                        const nombreEstado = primeraColonia.municipio.estado.nombre;
                        const idMunicipio = primeraColonia.municipio.idMunicipio;
                        const nombreMunicipio = primeraColonia.municipio.nombre;

                        $("#selectPais").val(idPais);

                        $("#selectEstado").empty()
                            .append(`<option value="${idEstado}">${nombreEstado}</option>`)
                            .val(idEstado);

                        $("#selectMunicipio").empty()
                            .append(`<option value="${idMunicipio}">${nombreMunicipio}</option>`)
                            .val(idMunicipio);

                        $("#selectColonia").empty();
                        $("#selectColonia").append('<option value="0">Selecciona una colonia</option>');

                        $.each(data.objects, function (i, colonia) {
                            $("#selectColonia").append(
                                `<option value="${colonia.idColonia}" data-cp="${colonia.codigoPostal}">${colonia.nombre}</option>`
                            );
                        });

                    } else {
                        alert("No se encontró ninguna dirección con este Código Postal.");
                        limpiarSelectsUbicacion();
                    }
                },
                error: function () {
                    alert("Ocurrió un error al buscar el Código Postal.");
                }
            });
        } else {
            console.log("El campo está vacío");
            limpiarSelectsUbicacion();
        }
    });

    function limpiarSelectsUbicacion() {
        $("#selectPais").val("0");
        $("#selectEstado, #selectMunicipio, #selectColonia")
            .empty()
            .append('<option value="0" selected>Selecciona una opción</option>');
    }
}
export function CascadeoUbicacion() {

    $("#selectMunicipio").change(function () {
        var idMunicipio = $(this).val();

        if (idMunicipio != "0") {
            $.ajax({
                url: `${API_BASE_URL}${API_ENDPOINTS.colonia}/` + idMunicipio,
                type: "GET",
                dataType: "json",
                success: function (data) {
                    $("#selectColonia").empty();
                    $("#selectColonia").append('<option value="0" data-cp="">Selecciona una colonia</option>');

                    // CORREGIDO: Validamos que exista data.objects e iteramos sobre él
                    if (data && data.correct && data.objects) {
                        $.each(data.objects, function (i, colonia) {
                            // CORREGIDO: idColonia, codigoPostal y nombre en camelCase
                            $("#selectColonia").append(
                                `<option value="${colonia.idColonia}" data-cp="${colonia.codigoPostal}">${colonia.nombre}</option>`
                            );
                        });
                    }
                },
                error: function () {
                    alert("Error al cargar las colonias.");
                }
            });
        } else {
            $("#selectColonia").empty().append('<option value="0">Selecciona una colonia</option>');
            $("#CodigoPostal").val("");
        }
    });

    $("#selectColonia").change(function () {
        var optionSeleccionado = $(this).find('option:selected');
        var codigoPostalAsignado = optionSeleccionado.data('cp');

        if (codigoPostalAsignado) {
            $("#CodigoPostal").val(codigoPostalAsignado);
        } else if ($(this).val() === "0" || $(this).val() === 0) {
            $("#CodigoPostal").val("");
        }
    });
}