package com.digis01.GGarciaProgramacionNCapasMaven.ML;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class Estado {

    private int IdEstado;
    private String Nombre;
    @Valid
    public Pais Pais;

    public void setIdEstado(int IdEstado) {
        this.IdEstado = IdEstado;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public int getIdEstado() {
        return IdEstado;
    }

    public String getNombre() {
        return Nombre;
    }

    public Pais getPais() {
        return Pais;
    }

    public void setPais(Pais Pais) {
        this.Pais = Pais;
    }

}
