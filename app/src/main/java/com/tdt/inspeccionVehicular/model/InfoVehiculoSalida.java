package com.tdt.inspeccionVehicular.model;

import java.io.Serializable;

public class InfoVehiculoSalida implements Serializable {
    private String codigo;
    private String ubicacion;
    private String turno;
    private String retrabajos;
    private String aplicador;

    public InfoVehiculoSalida() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getRetrabajos() {
        return retrabajos;
    }

    public void setRetrabajos(String retrabajos) {
        this.retrabajos = retrabajos;
    }

    public String getAplicador() {
        return aplicador;
    }

    public void setAplicador(String aplicador) {
        this.aplicador = aplicador;
    }
}
