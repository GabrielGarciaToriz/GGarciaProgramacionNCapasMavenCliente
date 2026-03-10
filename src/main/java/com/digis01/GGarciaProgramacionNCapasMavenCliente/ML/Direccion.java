package com.digis01.GGarciaProgramacionNCapasMavenCliente.ML;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {

    private int idDireccion;
    private String calle;
    private String numeroExterior;
    private String numeroInterior;
    public Colonia colonia;

 

}
