package com.digis01.GGarciaProgramacionNCapasMavenCliente.ML;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Colonia {

    private int idColonia;
    private String nombre;
    private String codigoPostal;
    public Municipio municipio;

}
