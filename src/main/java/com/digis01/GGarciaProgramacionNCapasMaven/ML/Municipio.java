package com.digis01.GGarciaProgramacionNCapasMaven.ML;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class Municipio {

    private int IdMunicipio;
    private String Nombre;
    @Valid
    public Estado Estado;

    public void setIdMunicipio(int IdMunicipio) {
        this.IdMunicipio = IdMunicipio;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public int getIdMunicipio() {
        return IdMunicipio;
    }

    public String getNombre() {
        return Nombre;
    }

    public Estado getEstado() {
        return Estado;
    }

    public void setEstado(Estado Estado) {
        this.Estado = Estado;
    }

}
