package com.tdt.artlux.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.tdt.artlux.R;

public class ParametrosWS {

    String URL;
    String METODO;
    String SOAP_ACTION;
    String NAMESPACES;

    public ParametrosWS(String METODO, Context context) {
        this.METODO = METODO;
        SharedPreferences sharedPref = context.getSharedPreferences("ServidorPreferences", Context.MODE_PRIVATE);
        URL= sharedPref.getString("servidor","null");
        NAMESPACES = context.getResources().getString(R.string.NAMESPACE) + METODO;
        SOAP_ACTION=context.getResources().getString(R.string.NAMESPACE) + METODO;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getMETODO() {
        return METODO;
    }

    public void setMETODO(String METODO) {
        this.METODO = METODO;
    }

    public String getSOAP_ACTION() {
        return SOAP_ACTION;
    }

    public void setSOAP_ACTION(String SOAP_ACTION) {
        this.SOAP_ACTION = SOAP_ACTION;
    }

    public String getNAMESPACES() {
        return NAMESPACES;
    }

    public void setNAMESPACES(String NAMESPACES) {
        this.NAMESPACES = NAMESPACES;
    }
}
