export function confirmarEliminacionDireccionUsuario(urlEliminacion) {
    Swal.fire({
        title: '¿Esta seguro de eliminar a este usuario?',
        text: 'Esta acción no se puede deshacer',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = urlEliminacion;
            document.body.appendChild(form);
            form.submit();
        }
    });
}

export function confirmarEliminacionDireccion(urlEliminacion) {
    Swal.fire({
        title: '¿Esta seguro de eliminar esta direccion?',
        text: 'Esta acción no se puede deshacer',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = urlEliminacion;
            document.body.appendChild(form);
            form.submit();
        }
    });
}

export function mostrarNotificacion(icono, mensaje) {
    const Toast = Swal.mixin({
        toast: true,
        positon: 'top-end',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer)
            toast.addEventListener('mouseleave', Swal.resumeTimer)
        }
    });
    Toast.fire({
        icon: icono,
        title: mensaje
    })
}

export function verificarAlertasServidor() {
    const msjExito = document.getElementById('mensajeExito');
    const msjError = document.getElementById('mensajeError');

    if (msjExito && msjExito.value) {
        mostrarNotificacion('success', msjExito.value)
    }
    if (msjError && msjError.value) {
        mostrarNotificacion('error', msjError.value)
    }
}