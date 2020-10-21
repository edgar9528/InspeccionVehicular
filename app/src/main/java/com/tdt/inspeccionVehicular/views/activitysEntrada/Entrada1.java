package com.tdt.inspeccionVehicular.views.activitysEntrada;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.tdt.inspeccionVehicular.Class.DatabaseHelper;
import com.tdt.inspeccionVehicular.R;
import com.tdt.inspeccionVehicular.model.InfoVehiculoEntrada;
import com.tdt.inspeccionVehicular.views.InicioActivity;

import java.util.ArrayList;

public class Entrada1 extends AppCompatActivity {

    Button button_s1,button_reg;
    EditText et_codigo;
    TextView tv_codigo;

    String codigo,marca,caja;
    Spinner spinner,spinner2;

    ArrayList<String> marcas;
    ArrayList<String> marcas_id;

    ArrayList<String> tipocaja;
    ArrayList<String> tipocaja_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada1);

        showToolbar(getString(R.string.tv_regisEnt),false);


        try {

            button_reg = findViewById(R.id.button_regresar1);
            button_s1 = findViewById(R.id.button_siguiente1);
            et_codigo = findViewById(R.id.et_codvehiculo);
            tv_codigo = findViewById(R.id.tv_ent_codigo);

            spinner = (Spinner) findViewById(R.id.spinnerMarca);
            spinner2 = (Spinner) findViewById(R.id.spinnerCaja);

            et_codigo.setInputType(InputType.TYPE_NULL);

            llenarSpinners();

            button_s1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    codigo = tv_codigo.getText().toString();
                    marca = spinner.getSelectedItem().toString();
                    caja = spinner2.getSelectedItem().toString();

                    if (codigo.length() == 17 && !marca.isEmpty() && !caja.isEmpty()) {
                        InfoVehiculoEntrada infoVehiculoEntrada = new InfoVehiculoEntrada();
                        infoVehiculoEntrada.setCodigo(codigo);
                        infoVehiculoEntrada.setMarca(marca);
                        infoVehiculoEntrada.setCaja(caja);
                        int i = spinner.getSelectedItemPosition();
                        infoVehiculoEntrada.setMarcaClave(Integer.parseInt(marcas_id.get(i)));
                        i = spinner2.getSelectedItemPosition();
                        infoVehiculoEntrada.setCajaClave(Integer.parseInt(tipocaja_id.get(i)));

                        Intent intent = new Intent(getApplication(), Entrada2.class);
                        intent.putExtra("infoVehiculoEntrada", infoVehiculoEntrada);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplication(), getString(R.string.msg13), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            button_reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            et_codigo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.d("salida","entro aqui1");
                }

                @Override
                public void afterTextChanged(Editable editable) {

                    Log.d("salida","entro aqui2");

                    String entrada = et_codigo.getText().toString();
                    if (entrada.length() > 0) {
                        if (entrada.charAt(entrada.length() - 1) == ';') {
                            codigo = entrada.substring(0, entrada.length() - 1);
                            tv_codigo.setText(codigo);
                            et_codigo.setText("");
                            et_codigo.requestFocus();
                        }
                    }
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

        Cursor cursor = bd.rawQuery("SELECT * FROM MARCAS", null);

        marcas = new ArrayList<>();
        marcas_id = new ArrayList<>();

        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String descripcion = cursor.getString(1);
            marcas.add(descripcion);
            marcas_id.add(id);
        }
        spinner.setAdapter(new ArrayAdapter<String>( this, R.layout.spinner_item, marcas));


        cursor = bd.rawQuery("SELECT * FROM TCAJAS", null);
        tipocaja = new ArrayList<>();
        tipocaja_id = new ArrayList<>();

        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String descripcion = cursor.getString(1);
            tipocaja.add(descripcion);
            tipocaja_id.add(id);
        }

        spinner2.setAdapter(new ArrayAdapter<String>( this, R.layout.spinner_item, tipocaja));

        bd.close();

    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this,R.style.AlertDialogCustom);
        dialogo1.setTitle(getString(R.string.msg_importante));
        dialogo1.setMessage(getString(R.string.msg_salirSinGuardar));
        dialogo1.setCancelable(false);

        dialogo1.setPositiveButton(getString(R.string.msg_si), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Intent intent = new Intent(getApplication(), InicioActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialogo1.setNegativeButton(getString(R.string.msg_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //cancelar();
            }
        });
        dialogo1.show();

    }


    public void showToolbar(String title, boolean upButton)
    {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
