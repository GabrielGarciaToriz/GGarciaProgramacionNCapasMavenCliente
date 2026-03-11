import {
    SoloLetras, SoloLetrasBlur, validarCorreo, validarCorreoBlur,
    validarPassword, validarPasswordBlur, validarCurp, validarCurpBlur,
    validarCelular, validarCelularBlur, validarTelefono, validarTelefonoBlur,
    abrirCalendario, soloCalendario, validarDirecciones, validarDireccionesBlur, Usuario, UsuarioBlur
} from "./Validaciones/index.js";

import {
    PaisEstado, EstadoMunicipio, DireccionByCodigoPostal, CascadeoUbicacion
} from "./Selects/index.js";

import { confirmarEliminacionDireccionUsuario, confirmarEliminacionDireccion, verificarAlertasServidor, abrirModalEdicionDireccion } from "./Helpers/index.js";

import { initCargaMasiva } from "./Files/Archivos.js";

const reglasValidacion = [
    // Letras
    { selector: ".validar-letras", evento: "keypress", accion: SoloLetras },
    { selector: ".validar-letras-blur", evento: "blur", accion: SoloLetrasBlur },
    // Correo
    { selector: ".validar-correo", evento: "keypress", accion: validarCorreo },
    { selector: ".validar-correo-blur", evento: "blur", accion: validarCorreoBlur },
    // Password
    { selector: ".validar-password", evento: "keypress", accion: validarPassword },
    { selector: ".validar-password-blur", evento: "blur", accion: validarPasswordBlur },
    // Curp
    { selector: ".validar-curp", evento: "keypress", accion: validarCurp },
    { selector: ".validar-curp-blur", evento: "blur", accion: validarCurpBlur },
    // Celular y Teléfono
    { selector: ".validar-celular", evento: "keypress", accion: validarCelular },
    { selector: ".validar-celular-blur", evento: "blur", accion: validarCelularBlur },
    { selector: ".validar-telefono", evento: "keypress", accion: validarTelefono },
    { selector: ".validar-telefono-blur", evento: "blur", accion: validarTelefonoBlur },
    // Direcciones
    { selector: ".validar-direccion", evento: "keypress", accion: validarDirecciones },
    { selector: ".validar-direccion-blur", evento: "blur", accion: validarDireccionesBlur },
    // Calendario
    { selector: ".solo-calendario", evento: "keydown", accion: soloCalendario },
    { selector: ".abrir-calendario", evento: "click", accion: abrirCalendario },
    // Usuario
    { selector: ".validar-usuario", evento: "keypress", accion: Usuario },
    { selector: ".validar-usuario-blur", evento: "blur", accion: UsuarioBlur }
];

const aplicarValidaciones = () => {
    reglasValidacion.forEach(({ selector, evento, accion }) => {
        $(selector).on(evento, (event) => {
            accion(event.currentTarget, event);
        });
    });
};

const inicializarSelectores = () => {
    PaisEstado();
    EstadoMunicipio();
    DireccionByCodigoPostal();
    CascadeoUbicacion();
};

const initDirectorioUsuarios = () => {
    $(".btn-eliminar-direccion-usuario").on("click", function (event) {
        event.preventDefault();
        const url = $(this).data("url");
        console.log("Se va a ejecutar la ruta: ", url);
        confirmarEliminacionDireccionUsuario(url);
    });
    $(".form-check-input[role='switch']").on("change", function () {
        const checkbox = $(this);
        const urlPeticionBase = checkbox.data("url");
        const nuevoEstatus = checkbox.is(":checked") ? 1 : 0;
        const urlFinal = urlPeticionBase + nuevoEstatus;

        $.ajax({
            type: "POST",
            url: urlFinal,
            success: function (response) {
                if (response.correct) {
                    console.log("Estatus actualizado con éxito para el usuario:", urlFinal);
                } else {
                    console.error("Error en BD:", response.errorMessage);
                    checkbox.prop("checked", nuevoEstatus === 0);
                    alert("No se pudo actualizar el estatus: " + response.errorMessage);
                }
            },
            error: function (xhr, status, error) {
                console.error("Error AJAX:", xhr.status, error);
                checkbox.prop("checked", nuevoEstatus === 0);
                alert("Error de comunicación. Posiblemente la ruta no se encontró o hay problemas en el servidor.");
            }
        });
    });
};

const initFormularioUsuario = () => {
    inicializarSelectores();

    $(".btn-eliminar-direccion").on("click", function (event) {
        event.preventDefault();
        console.log("Me estas apretando");
        const url = $(this).data("url");
        console.log("Se va a ejecutar la ruta: ", url);
        confirmarEliminacionDireccion(url);
    });

    $("#formRegistroUsuario").on("submit", function () {
        const btn = $("#btnGuardar");
        btn.prop("disabled", true);
        btn.html('<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Guardando..');
        return true;
    });

    const fechaHoy = new Date();
    const anioMaximo = fechaHoy.getFullYear() - 18;
    const mes = String(fechaHoy.getMonth() + 1).padStart(2, '0');
    const dia = String(fechaHoy.getDate()).padStart(2, '0');
    const fechaMaxima = `${anioMaximo}-${mes}-${dia}`;
    $("#FechaNacimiento").attr("max", fechaMaxima);

    if (typeof idDireccion !== 'undefined' && idDireccion !== null) {
        abrirModalEdicionDireccion(idDireccion);
    }
};


$(document).ready(() => {

    verificarAlertasServidor();
    aplicarValidaciones();


    const paginaActual = document.body.getAttribute('data-page');

    switch (paginaActual) {
        case 'Usuario':
            initDirectorioUsuarios();
            break;

        case 'UsuarioForm':
            initFormularioUsuario();
            break;

        case 'UsuarioDetail':
            inicializarSelectores();
            $(".btn-eliminar-direccion").on("click", function (event) {
                event.preventDefault();
                const url = $(this).data("url");
                confirmarEliminacionDireccion(url);
            });
            break;

        case 'UsuarioCargaMasiva':
            initCargaMasiva();
            break;

        default:
            console.log("Página cargada sin scripts específicos asignados o falta el atributo data-page.");
            break;
    }
});