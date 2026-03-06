package com.digis01.GGarciaProgramacionNCapasMaven.ML;

import jakarta.validation.constraints.NotBlank;

public class Pais {

    private int IdPais;
    private String Nombre;

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public void setIdPais(int IdPais) {
        this.IdPais = IdPais;
    }

    public int getIdPais() {
        return IdPais;
    }

    public String getNombre() {
        return Nombre;
    }

}
