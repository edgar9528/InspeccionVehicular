package com.tdt.inspeccionVehicular.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tdt.inspeccionVehicular.R;
import com.tdt.inspeccionVehicular.views.activitysEntrada.Entrada1;
import com.tdt.inspeccionVehicular.views.activitysSalida.Salida1;
import com.tdt.inspeccionVehicular.views.administrar.ModuloComunicacionActivity;

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
                        Toast.makeText(getApplication(), getString(R.string.msg4), Toast.LENGTH_SHORT).show();
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

}

