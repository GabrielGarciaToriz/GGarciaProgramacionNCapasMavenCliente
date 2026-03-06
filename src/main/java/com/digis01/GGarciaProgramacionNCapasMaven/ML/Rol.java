package com.digis01.GGarciaProgramacionNCapasMaven.ML;

import jakarta.validation.constraints.Min;

public class Rol {
    
    @Min(value = 1, message = "Debes de seleccionar un Rol")
    private int IdRol;
    private String Nombre;

    public Rol() {

    }

    /*GETTERS*/
    public int getIdRol() {
        return IdRol;
    }

    public String getNombre() {
        return Nombre;
    }

    /*SETTERS*/
    public void setIdRol(int IdRol) {
        this.IdRol = IdRol;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

}
