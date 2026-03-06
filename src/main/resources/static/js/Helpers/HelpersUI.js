export const mostrarError = (input, mensaje) => {
    const errorSpan = $(`#error${input.id}`);
    errorSpan.text(mensaje).css("color", "red");
    $(input).addClass("border border-danger").removeClass("border border-success")
}
export const limpiarEstilos = (input) => {
    const errorSpan = $(`#error${input.id}`);
    errorSpan.text("");
    $(input).removeClass("border border-danger border-success");
};
export const marcarExito = (input, mensaje) => {
    const errorSpan = $(`#error${input.id}`);
    errorSpan.text("");
    errorSpan.text(mensaje).css("color", "green")
    $(input).removeClass("border border-danger").addClass("border border-success");
};
export const teclasPermitidas = [
    "Tab", "Enter", "ArrowLeft", "ArrowRight"
]

export const resetSelect = ($el, texto) => {
    $el.html(`<option value="0" selected>${texto}</option>`);
}

export const cargarSelectCascada = (id, urlBase, $target, targetTexto, idKey, nameKey, dependientes = []) => {
    dependientes.forEach(dep => resetSelect(dep.$el, dep.texto));
    if (id != 0) {
        $.ajax({
            url: `${urlBase}${id}`,
            type: "GET",
            dataType: "json",
            success: (data) => {
                let opciones = `<option value="0" selected>${targetTexto}</option>`;
                data.objects.forEach(item => {
                    opciones += `<option value="${item[idKey]}"> ${item[nameKey]}</option>`
                });
                $target.html(opciones)
            },
            error: () => alert("No se pudo completar la tarea")

        })
    } else {
        resetSelect($target, targetTexto);
    }
}