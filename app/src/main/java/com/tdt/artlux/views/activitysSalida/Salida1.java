package com.tdt.artlux.views.activitysSalida;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tdt.artlux.Class.DatabaseHelper;
import com.tdt.artlux.R;
import com.tdt.artlux.model.InfoVehiculoSalida;
import com.tdt.artlux.views.InicioActivity;
import com.tdt.artlux.views.activitysEntrada.Entrada1;
import com.tdt.artlux.views.activitysEntrada.Entrada2;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class Salida1 extends AppCompatActivity {

    Button button_reg,button_sig;
    Spinner spinner,spinner2;
    EditText et_codigo;
    String codigo,color,destino,ubicacion,turno;
    TextView tv_codigo,tv_color,tv_destino;

    boolean encontrado=false,buscar=true;
    ArrayList<String> lineas,turnos;
    ArrayList<String> lineas_id,turnos_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida1);
        showToolbar("Salida",false);

        try {

            button_reg = findViewById(R.id.button_reg1_sal);
            button_sig = findViewById(R.id.button_sig1_sal);
            et_codigo = findViewById(R.id.et_sal_cod);

            et_codigo.setInputType(InputType.TYPE_NULL);

            tv_codigo = findViewById(R.id.tv_sal_codigo);
            tv_color = findViewById(R.id.tv_sal_color);
            tv_destino = findViewById(R.id.tv_sal_destino);

            spinner = (Spinner) findViewById(R.id.spinnerUbicacion);
            spinner2 = (Spinner) findViewById(R.id.spinnerTurno);
            llenarSpinners();

            et_codigo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    String entrada = et_codigo.getText().toString();
                    if (entrada.length() > 0) {
                        if (entrada.charAt(entrada.length() - 1) == ';' && buscar) {
                            buscar = false;
                            codigo = entrada.substring(0, entrada.length() - 1);
                            ObtenerVehiculo obtenerVehiculo = new ObtenerVehiculo();
                            obtenerVehiculo.execute();
                        }
                    }
                }
            });

            button_sig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    codigo = tv_codigo.getText().toString();
                    color = tv_color.getText().toString();
                    destino = tv_destino.getText().toString();
                    ubicacion = lineas_id.get(spinner.getSelectedItemPosition());
                    turno = turnos_id.get(spinner2.getSelectedItemPosition());

                    if (!codigo.isEmpty() && !color.isEmpty() && !destino.isEmpty() && !ubicacion.isEmpty() && !turno.isEmpty()) {
                        InfoVehiculoSalida infoVehiculoSalida = new InfoVehiculoSalida();

                        infoVehiculoSalida.setCodigo(codigo);
                        infoVehiculoSalida.setUbicacion(ubicacion);
                        infoVehiculoSalida.setTurno(turno);

                        Intent intent = new Intent(getApplication(), Salida2.class);
                        intent.putExtra("infoVehiculoSalida", infoVehiculoSalida);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplication(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            button_reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    advertenciaSalir(view.getContext());
                }
            });

        }catch (Exception e)
        {
            Toast.makeText(getApplication(), "Error: "+e.toString(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplication(), Salida1.class);
            startActivity(intent);
            finish();
        }

    }


    public void llenarSpinners()
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplication(), "baseLocal", null, 1);
        SQLiteDatabase bd = databaseHelper.getWritableDatabase();

        Cursor cursor = bd.rawQuery("SELECT * FROM LINEAS", null);

        lineas = new ArrayList<>();
        lineas_id = new ArrayList<>();

        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String descripcion = cursor.getString(1);
            lineas.add(descripcion);
            lineas_id.add(id);
        }
        spinner.setAdapter(new ArrayAdapter<String>( this, R.layout.spinner_item, lineas));


        cursor = bd.rawQuery("SELECT * FROM TURNOS", null);
        turnos = new ArrayList<>();
        turnos_id = new ArrayList<>();

        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String descripcion = cursor.getString(1);
            turnos.add(descripcion);
            turnos_id.add(id);
        }

        spinner2.setAdapter(new ArrayAdapter<String>( this, R.layout.spinner_item, turnos));

        bd.close();

    }


    private class ObtenerVehiculo extends AsyncTask<String,Integer,Boolean> {

        private ProgressDialog progreso;

        @Override protected void onPreExecute() {
            progreso = new ProgressDialog(Salida1.this);
            progreso.setMessage("Buscando información...");
            progreso.setCancelable(false);
            progreso.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            boolean result = true;

            try {

                SharedPreferences sharedPref = getSharedPreferences("ServidorPreferences", Context.MODE_PRIVATE);
                String URL = sharedPref.getString("servidor", "null");
                String NAMESPACE = "http://tempuri.org/";
                String SOAP_ACTION = "http://tempuri.org/GetInfoAuto";
                String METODO = "GetInfoAuto";


                SoapObject Solicitud = new SoapObject(NAMESPACE, METODO);

                PropertyInfo PrimerParametro = new PropertyInfo();
                PrimerParametro.setName("apl_cve_n");
                PrimerParametro.setValue(codigo);
                Solicitud.addProperty(PrimerParametro);

                SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                Envoltorio.dotNet = true;
                Envoltorio.setOutputSoapObject(Solicitud);

                HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
                try {
                    TransporteHttp.call(SOAP_ACTION, Envoltorio);

                    SoapObject response = (SoapObject) Envoltorio.getResponse();
                    SoapObject response2 = (SoapObject) response.getProperty(1);

                    try {
                        SoapObject s_deals = (SoapObject) response2.getProperty("NewDataSet");
                        SoapObject s_deals_1 = (SoapObject) s_deals.getProperty(0);
                        color = s_deals_1.getProperty(0).toString();
                        destino = s_deals_1.getProperty(1).toString();
                        encontrado = true;
                    } catch (Exception e) {
                        encontrado = false;
                    }

                } catch (Exception e) {
                    //Log.d("Salida","Error"+e.toString());
                    result = false;
                }


            }catch (Exception e)
            {
                Toast.makeText(getApplication(), "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
            }


            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progreso.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            progreso.dismiss();

            if(aBoolean)
            {
                if(!encontrado) {
                    Toast.makeText(getApplication(), "No se encontro el vehiculo", Toast.LENGTH_SHORT).show();
                    et_codigo.setText("");
                    tv_codigo.setText("");
                    tv_color.setText("");
                    tv_destino.setText("");
                }
                else
                {
                    et_codigo.setText("");
                    tv_codigo.setText(codigo);
                    tv_color.setText(color);
                    tv_destino.setText(destino);
                }
            }
            else
            {
                Toast.makeText(getApplication(), "Error en la comunicación", Toast.LENGTH_SHORT).show();
            }

            et_codigo.requestFocus();
            buscar=true;

        }
    }


    @Override
    public void onBackPressed() {

        advertenciaSalir(this );
    }

    public void advertenciaSalir(Context context)
    {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context,R.style.AlertDialogCustom);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿Desea salir sin guardar?");
        dialogo1.setCancelable(false);

        dialogo1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

                Intent intent = new Intent(getApplication(), InicioActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
