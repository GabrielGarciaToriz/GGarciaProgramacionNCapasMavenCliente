package com.digis01.GGarciaProgramacionNCapasMavenCliente.ML;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Municipio {

    private int idMunicipio;
    private String nombre;
    public Estado estado;


}
