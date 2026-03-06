export function DireccionByCodigoPostal() {
    $("#CodigoPostal").change(function () {
        var codigoPostal = $("#CodigoPostal").val();

        if (codigoPostal !== "") {
            $.ajax({
                url: "/usuario/getDireccionByCodigoPostal/" + codigoPostal,
                type: "GET",
                dataType: "json",
                success: function (data) {
                    // Verificamos que el JSON traiga resultados
                    if (data.objects && data.objects.length > 0) {

                        var primeraColonia = data.objects[0];

                        var idPais = primeraColonia.Municipio.Estado.Pais.IdPais;
                        var idEstado = primeraColonia.Municipio.Estado.IdEstado;
                        var nombreEstado = primeraColonia.Municipio.Estado.Nombre;
                        var idMunicipio = primeraColonia.Municipio.IdMunicipio;
                        var nombreMunicipio = primeraColonia.Municipio.Nombre;

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
                                `<option value="${colonia.IdColonia}" data-cp="${colonia.CodigoPostal}">${colonia.Nombre}</option>`
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
                url: "/usuario/getColoniabyMunicipio/" + idMunicipio,
                type: "GET",
                dataType: "json",
                success: function (data) {
                    $("#selectColonia").empty();
                    $("#selectColonia").append('<option value="0" data-cp="">Selecciona una colonia</option>');

                    $.each(data.objects, function (i, colonia) {
                        $("#selectColonia").append(
                            `<option value="${colonia.IdColonia}" data-cp="${colonia.CodigoPostal}">${colonia.Nombre}</option>`
                        );
                    });
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