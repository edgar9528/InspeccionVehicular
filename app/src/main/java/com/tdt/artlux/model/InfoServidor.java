package com.tdt.artlux.model;

public class InfoServidor {

    private String servidor;
    private String tipoApp;

    public InfoServidor(String servidor, String tipoApp) {
        this.servidor = servidor;
        this.tipoApp = tipoApp;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public String getTipoApp() {
        return tipoApp;
    }

    public void setTipoApp(String tipoApp) {
        this.tipoApp = tipoApp;
    }
}
