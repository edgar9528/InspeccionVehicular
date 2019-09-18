package com.tdt.artlux.views.activitysEntrada;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.tdt.artlux.R;
import com.tdt.artlux.model.InfoVehiculoEntrada;

public class Entrada4 extends AppCompatActivity {

    InfoVehiculoEntrada infoVehiculoEntrada;
    boolean seleccionado=false;
    String color_auto;
    String color_auto_id;
    ImageView iv_color;
    Button button_siguiente,button_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada4);

        showToolbar("Color",false);

        try {

            infoVehiculoEntrada = (InfoVehiculoEntrada) getIntent().getSerializableExtra("infoVehiculoEntrada");

            button_siguiente = findViewById(R.id.button_siguiente4);
            button_reg = findViewById(R.id.button_regresar4);

            iv_color = findViewById(R.id.iv_vehiculo);

            button_siguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (seleccionado) {
                        infoVehiculoEntrada.setColor(color_auto);
                        infoVehiculoEntrada.setColor_id(color_auto_id);
                        Intent intent = new Intent(getApplication(), Entrada5.class);
                        intent.putExtra("infoVehiculoEntrada", infoVehiculoEntrada);
                        startActivity(intent);
                    } else
                        Toast.makeText(getApplication(), "Seleccione un color", Toast.LENGTH_SHORT).show();
                }
            });

            button_reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

        }catch (Exception e)
        {
            Toast.makeText(getApplication(), "Error: "+e.toString(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplication(), Entrada1.class);
            startActivity(intent);
            finish();
        }

    }

    public void cambiarColor(View view)
    {
        String color = view.getTag().toString();
        seleccionado=true;
        switch (color)
        {
            case "Negro":
                iv_color.setImageResource(R.drawable.auto);
                color_auto="NEGRO KH3";
                color_auto_id="C1";
                break;
            case "Blanco":
                iv_color.setImageResource(R.drawable.auto_blanco);
                color_auto="BLANCO QM1";
                color_auto_id="C2";
                break;
            case "Azul":
                iv_color.setImageResource(R.drawable.auto_azul);
                color_auto="AZUL B23";
                color_auto_id="C3";
                break;
            case "GrisOsc":
                iv_color.setImageResource(R.drawable.auto_grisosc);
                color_auto="GRISOBSCURO KAD";
                color_auto_id="C4";
                break;
            case "GrisCla":
                iv_color.setImageResource(R.drawable.auto_griscla);
                color_auto="GRISCLARO K23";
                color_auto_id="C5";
                break;
            case "Rojo":
                iv_color.setImageResource(R.drawable.auto_rojo);
                color_auto="ROJO A20";
                color_auto_id="C6";
                break;
            case "Amarillo":
                iv_color.setImageResource(R.drawable.auto_amarillo);
                color_auto="DORADO EAU";
                color_auto_id="C7";
                break;
            case "Cyan":
                iv_color.setImageResource(R.drawable.auto_cyan);
                color_auto="CYAN CYAN";
                color_auto_id="C8";
                break;
        }
    }

    public void showToolbar(String title, boolean upButton)
    {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
