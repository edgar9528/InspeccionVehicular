package com.tdt.inspeccionVehicular.model;

public class ValorRetorno {
    public int retorno;
    public String mensaje;

    @Override
    public String toString() {
        return "Retorno:" + retorno + "|Mensaje:" + mensaje;
    }
}
