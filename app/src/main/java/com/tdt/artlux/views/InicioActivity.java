package com.tdt.artlux.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tdt.artlux.Class.CifrarDescifrar;
import com.tdt.artlux.R;
import com.tdt.artlux.model.RegistroTerminal;
import com.tdt.artlux.views.activitysEntrada.Entrada1;
import com.tdt.artlux.views.activitysSalida.Salida1;
import com.tdt.artlux.views.administrar.LicenciaActivity;
import com.tdt.artlux.views.administrar.ModuloComunicacionActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class InicioActivity extends AppCompatActivity {

    Button buttonCom,button_entrar;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        try {

            button_entrar = findViewById(R.id.button_entrar);
            buttonCom = findViewById(R.id.Bcom);


            button_entrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences sharedPref = getSharedPreferences("ServidorPreferences", Context.MODE_PRIVATE);
                    String servidor = sharedPref.getString("servidor", "null");
                    String tipo = sharedPref.getString("tipo", "null");

                    if (!servidor.equals("null")) {
                        if (tipo.equals("Entrada")) {
                            Intent intent = new Intent(InicioActivity.this, Entrada1.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(InicioActivity.this, Salida1.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplication(), "Primero configura en COMMS", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            buttonCom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent modulo = new Intent(getApplicationContext(), ModuloComunicacionActivity.class);
                    startActivity(modulo);
                }
            });

        }catch (Exception e)
        {
            Toast.makeText(getApplication(), "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void salir(View view)
    {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Desea salir de la app?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                /*finish();
                System.exit(0);*/
            }
        });
        dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //cancelar();
            }
        });
        dialogo1.show();
    }

}

