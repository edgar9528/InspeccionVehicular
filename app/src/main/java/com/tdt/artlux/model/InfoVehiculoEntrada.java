package com.tdt.artlux.model;

import java.io.Serializable;

public class InfoVehiculoEntrada implements Serializable {

    private String codigo;
    private String marca;
    private String caja;
    private int danLeves;
    private int danGraves;
    private String destino;
    private String color;
    private int marcaClave;
    private int cajaClave;
    private String destino_id;
    private String color_id;

    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
    }

    public InfoVehiculoEntrada() {
    }

    public String getDestino_id() {
        return destino_id;
    }

    public void setDestino_id(String destino_id) {
        this.destino_id = destino_id;
    }

    public int getMarcaClave() {
        return marcaClave;
    }

    public void setMarcaClave(int marcaClave) {
        this.marcaClave = marcaClave;
    }

    public int getCajaClave() {
        return cajaClave;
    }

    public void setCajaClave(int cajaClave) {
        this.cajaClave = cajaClave;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public int getDanLeves() {
        return danLeves;
    }

    public void setDanLeves(int danLeves) {
        this.danLeves = danLeves;
    }

    public int getDanGraves() {
        return danGraves;
    }

    public void setDanGraves(int danGraves) {
        this.danGraves = danGraves;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
