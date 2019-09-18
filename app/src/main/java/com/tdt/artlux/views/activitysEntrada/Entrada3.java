package com.tdt.artlux.views.activitysEntrada;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tdt.artlux.Class.DatabaseHelper;
import com.tdt.artlux.R;
import com.tdt.artlux.model.InfoVehiculoEntrada;

import java.util.ArrayList;

public class Entrada3 extends AppCompatActivity {

    InfoVehiculoEntrada infoVehiculoEntrada;
    Button button_siguiente,button_ret;
    boolean seleccionado=false;
    String destino;
    String destino_id="";

    ArrayList<String> destinos;
    ArrayList<String> destinos_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada3);

        showToolbar("Destino",false);

        try {

            infoVehiculoEntrada = (InfoVehiculoEntrada) getIntent().getSerializableExtra("infoVehiculoEntrada");
            button_siguiente = findViewById(R.id.button_siguiente3);
            button_ret = findViewById(R.id.button_regresar3);

            llenarSpinners();

            button_siguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (seleccionado) {
                        infoVehiculoEntrada.setDestino(destino);
                        infoVehiculoEntrada.setDestino_id(destino_id);

                        Intent intent = new Intent(getApplication(), Entrada4.class);
                        intent.putExtra("infoVehiculoEntrada", infoVehiculoEntrada);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplication(), "Seleccione un destino", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            button_ret.setOnClickListener(new View.OnClickListener() {
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

    public void llenarSpinners()
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplication(), "baseLocal", null, 1);
        SQLiteDatabase bd = databaseHelper.getWritableDatabase();

        Cursor cursor = bd.rawQuery("SELECT * FROM PAIS", null);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        RadioGroup radioGroup = new RadioGroup(this);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layout.addView(radioGroup, p);

        destinos = new ArrayList<>();
        destinos_id = new ArrayList<>();

        while(cursor.moveToNext())
        {
            String id = cursor.getString(0);
            String descripcion = cursor.getString(1);
            destinos.add(descripcion);
            destinos_id.add(id);

            RadioButton radioButtonView = new RadioButton(this);
            radioButtonView.setText(descripcion);
            radioGroup.addView(radioButtonView, p);
            radioButtonView.setOnClickListener(rbListener);
        }

        bd.close();
    }

    private View.OnClickListener rbListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            seleccionado=true;
            destino = ((RadioButton) view).getText().toString();
            for(int i=0; i<destinos.size(); i++)
            {
                if(destinos.get(i).equals(destino))
                {
                    destino_id = destinos_id.get(i) ;
                    break;
                }
            }
        }
    };

    public void showToolbar(String title, boolean upButton)
    {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

}
