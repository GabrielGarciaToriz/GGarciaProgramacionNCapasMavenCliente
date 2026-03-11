package com.digis01.GGarciaProgramacionNCapasMavenCliente.ML;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private int idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String celular;
    private String curp;
    private String userName;
    private String email;
    private String password;
    private String sexo;
    private String telefono;
    private int estatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;
    private String imagen;
    public Rol rol;
    public List<Direccion> direcciones;

}
