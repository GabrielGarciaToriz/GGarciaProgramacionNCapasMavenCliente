package com.digis01.GGarciaProgramacionNCapasMavenCliente.ML;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioVista {

    private Integer idUsaurio;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Date fechaNacimiento;
    private String celular;
    private String curp;
    private String usuario;
    private String correo;
    private String password;
    private String sexo;
    private String telefono;
    private Integer estatus;
    private String imagen;
    private Integer idRol;
    private String rolAsignado;
    private Integer idDireccion;
    private String calle;
    private String numeroExterior;
    private String numeroInterior;
    private Integer idColonia;
    private String colonia;
    private String cp;
    private Integer idMunicipio;
    private String municipio;
    private Integer idEstado;
    private String estado;
    private Integer idPais;
    private String Pais;
}
