package com.digis01.GGarciaProgramacionNCapasMaven.ML;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Direccion {

    private int IdDireccion;
    @NotBlank(message = "Este campo es obligatorio")
    private String Calle;
    @NotBlank(message = "Este campo es obligatorio")
    private String NumeroExterior;
    private String NumeroInterior;
    @NotNull(message = "Este campo es obligatorio")
    @Valid
    public Colonia Colonia;

    public int getIdDireccion() {
        return IdDireccion;
    }

    public void setIdDireccion(int IdDireccion) {
        this.IdDireccion = IdDireccion;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public String getCalle() {
        return Calle;
    }

    public void setIdColonia(int IdColonia) {
        this.IdDireccion = IdColonia;
    }

    public void setNumeroInterior(String NumeroInterior) {
        this.NumeroInterior = NumeroInterior;
    }

    public void setNumeroExterior(String NumeroExterior) {
        this.NumeroExterior = NumeroExterior;
    }

    public int IdColonia() {
        return IdDireccion;
    }

    public String getNumeroInterior() {
        return NumeroInterior;
    }

    public String getNumeroExterior() {
        return NumeroExterior;
    }

    public Colonia getColonia() {
        return Colonia;
    }

    public void setColonia(Colonia Colonia) {
        this.Colonia = Colonia;
    }

}
