package com.tdt.inspeccionVehicular.views;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tdt.inspeccionVehicular.Class.CifrarDescifrar;
import com.tdt.inspeccionVehicular.R;
import com.tdt.inspeccionVehicular.model.RegistroTerminal;
import com.tdt.inspeccionVehicular.views.administrar.LicenciaActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PrincipalActivity extends AppCompatActivity {

    FloatingActionButton fab;

    private String archivoRegistro, archivoLicencia;
    private Boolean licenciaValida;
    TextView tv_infoLicencia;
    TextView tv_numSerie;

    Button button_entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        try {

            archivoRegistro = "Registro.properties";
            archivoLicencia = "Licencia.properties";

            fab = findViewById(R.id.fab);
            tv_infoLicencia = findViewById(R.id.tv_estado_licencia);
            button_entrar = findViewById(R.id.button_entrar);
            tv_numSerie = findViewById(R.id.tv_num_serie);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplication(), LicenciaActivity.class);
                    startActivity(intent);
                }
            });

            button_entrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if (licenciaValida) {
                        Intent intent = new Intent(getApplication(), InicioActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplication(), getString(R.string.msg1)  , Toast.LENGTH_SHORT).show();
                    }
                }
            });

            generaRegistroTerminal();
            validaLicencia();

        }catch (Exception e)
        {
            Toast.makeText(getApplication(), "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    private void generaRegistroTerminal() {

        RegistroTerminal registroTerminal = new RegistroTerminal();

        Properties propiedades;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            registroTerminal.setSerie(Build.SERIAL);
            registroTerminal.setModelo(Build.MODEL);
            registroTerminal.setMarca(Build.BRAND);
            registroTerminal.setFabricante(Build.MANUFACTURER);
            registroTerminal.setDispositivo(Build.DEVICE);
            registroTerminal.setProducto(Build.PRODUCT);

            tv_numSerie.setText(registroTerminal.getSerie());

        } catch (Exception ex) {
        }
        propiedades = new Properties();
        propiedades.setProperty("SERIE", registroTerminal.getSerie());
        propiedades.setProperty("MODELO", registroTerminal.getModelo());
        propiedades.setProperty("MARCA", registroTerminal.getMarca());
        propiedades.setProperty("FABRICANTE", registroTerminal.getFabricante());
        propiedades.setProperty("DISPOSITIVO", registroTerminal.getDispositivo());
        propiedades.setProperty("PRODUCTO", registroTerminal.getProducto());
        // Borra El archivo de Propiedades Actual
        try {
            this.deleteFile(archivoRegistro);
        } catch (Exception ex) {
        }
        // Guarda la Informacion del Archivo - Opcional -
        try {
            fos = this.openFileOutput(archivoRegistro, Context.MODE_APPEND);
            propiedades.store(fos, "");
            //Toast.makeText(this, "Datos Guardados con Exito", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, "[EX GA] " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                fis.close();
            } catch (Exception ex) {
            }
        }
    }


    private void validaLicencia() {
        Properties propiedades = new Properties();
        FileInputStream fis = null;
        String strSerieL, strModeloL, strMarcaL;
        String strSerieR, strModeloR, strMarcaR;

        try {
            fis = openFileInput(archivoLicencia);
            propiedades.load(fis);

            strSerieL= CifrarDescifrar.descifrar(propiedades.getProperty("SERIE"), "TdTm901016");
            strModeloL= CifrarDescifrar.descifrar(propiedades.getProperty("MODELO"), "TdTm901016");
            strMarcaL= CifrarDescifrar.descifrar(propiedades.getProperty("MARCA"), "TdTm901016");

            fis = openFileInput(archivoRegistro);
            propiedades.load(fis);
            strSerieR = propiedades.getProperty("SERIE");
            strModeloR = propiedades.getProperty("MODELO");
            strMarcaR = propiedades.getProperty("MARCA");

            if( strSerieL.equals(strSerieR) && strMarcaL.equals(strMarcaR) && strModeloL.equals(strModeloR) )
            {
                tv_infoLicencia.setText( getString(R.string.tv_estadoLic1) );
                licenciaValida = true;
            }
            else
            {
                tv_infoLicencia.setText( getString(R.string.tv_estadoLic2) );
                licenciaValida = false;
            }

        } catch (Exception ex) {
            //Toast.makeText(this, "[EX LA] " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            tv_infoLicencia.setText( getString(R.string.tv_estadoLic3) );
            licenciaValida = false;

        } finally {
            try {
                fis.close();
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public void onBackPressed() {

    }


}
