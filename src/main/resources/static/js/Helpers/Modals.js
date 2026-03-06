export function abrirModalEdicionDireccion(idDireccion) {
   
    const modalElement = document.getElementById('ModalEditarDireccionDinamico');
    const myModal = new bootstrap.Modal(modalElement);
    myModal.show();

    // 2. Solicitamos el fragmento al servidor
    fetch('/usuario/direccion/editar/' + idDireccion)
        .then(response => {
            if (!response.ok) throw new Error("Error de red");
            return response.text(); 
        })
        .then(html => {

            document.getElementById('contenedorModalEditarDireccion').innerHTML = html;
        })
        .catch(error => {
            console.error('Hubo un problema con la petición Fetch:', error);
            document.getElementById('contenedorModalEditarDireccion').innerHTML =
                '<div class="modal-body text-danger">Error al cargar los datos.</div>';
        });
}