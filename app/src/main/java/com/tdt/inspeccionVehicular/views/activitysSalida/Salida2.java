package com.tdt.inspeccionVehicular.views.activitysSalida;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tdt.inspeccionVehicular.Class.DatabaseHelper;
import com.tdt.inspeccionVehicular.R;
import com.tdt.inspeccionVehicular.model.InfoVehiculoSalida;
import com.tdt.inspeccionVehicular.views.activitysEntrada.Entrada1;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class Salida2 extends AppCompatActivity {

    InfoVehiculoSalida infoVehiculoSalida;
    boolean seleccionado=false,encontrado=false,buscar=true;
    String retrabajo,retrabajo_id,codAplicador,aplicador_id,aplicadorNombre,CadenaDevuelta;

    EditText et_cod_aplicador;
    TextView tv_aplicador;

    ArrayList<String> retrabajos,retrabajos_id;

    Button button_reg,button_sig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida2);

        showToolbar("Salida",false);

        try {

            infoVehiculoSalida = (InfoVehiculoSalida) getIntent().getSerializableExtra("infoVehiculoSalida");

            et_cod_aplicador = findViewById(R.id.et_sal_aplicador);
            tv_aplicador = findViewById(R.id.tv_aplicador);
            button_reg = findViewById(R.id.button_sal_reg2);
            button_sig = findViewById(R.id.button_sal_sig2);

            et_cod_aplicador.setInputType(InputType.TYPE_NULL);

            llenarRadioButton();

            et_cod_aplicador.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    String entrada = et_cod_aplicador.getText().toString();
                    if (entrada.length() > 0) {
                        if (entrada.charAt(entrada.length() - 1) == ';' && buscar) {
                            buscar = false;
                            codAplicador = entrada.substring(0, entrada.length() - 1);
                            //Log.d("salida", codAplicador);
                            ObtenerAplicador obtenerAplicador = new ObtenerAplicador();
                            obtenerAplicador.execute();
                        }
                    }
                }
            });

            button_reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            button_sig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (seleccionado && !tv_aplicador.getText().toString().isEmpty()) {
                        infoVehiculoSalida.setRetrabajos(retrabajo_id);
                        infoVehiculoSalida.setAplicador(aplicador_id);

                        InsertarSalida insertarSalida = new InsertarSalida();
                        insertarSalida.execute();


                    } else {
                        Toast.makeText(getApplication(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
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


    public void llenarRadioButton()
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplication(), "baseLocal", null, 1);
        SQLiteDatabase bd = databaseHelper.getWritableDatabase();

        Cursor cursor = bd.rawQuery("SELECT * FROM RETRABAJO", null);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_retrabajos);
        RadioGroup radioGroup = new RadioGroup(this);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layout.addView(radioGroup, p);

        retrabajos = new ArrayList<>();
        retrabajos_id = new ArrayList<>();

        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String descripcion = cursor.getString(1);
            retrabajos.add(descripcion);
            retrabajos_id.add(id);

            RadioButton radioButtonView = new RadioButton(this);
            radioButtonView.setText(descripcion);
            radioGroup.addView(radioButtonView, p);
            radioButtonView.setOnClickListener(rbListener);
        }

        bd.close();
    }

    private class ObtenerAplicador extends AsyncTask<String,Integer,Boolean> {

        private ProgressDialog progreso;

        @Override protected void onPreExecute() {
            progreso = new ProgressDialog(Salida2.this);
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
                String SOAP_ACTION = "http://tempuri.org/GetInfoAplicador";
                String METODO = "GetInfoAplicador";

                SoapObject Solicitud = new SoapObject(NAMESPACE, METODO);

                PropertyInfo PrimerParametro = new PropertyInfo();
                PrimerParametro.setName("apl_cve_n");
                PrimerParametro.setValue(codAplicador);
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
                        aplicador_id = s_deals_1.getProperty(0).toString();
                        aplicadorNombre = s_deals_1.getProperty(1).toString();
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
                    Toast.makeText(getApplication(), "No se encontro el aplicador", Toast.LENGTH_SHORT).show();
                    et_cod_aplicador.setText("");
                    tv_aplicador.setText("");
                }
                else
                {
                    et_cod_aplicador.setText("");
                    tv_aplicador.setText(aplicadorNombre);
                }
            }
            else
            {
                Toast.makeText(getApplication(), "Error en la comunicación", Toast.LENGTH_SHORT).show();
            }

            et_cod_aplicador.requestFocus();

            buscar=true;

        }
    }


    private class InsertarSalida extends AsyncTask<String,Integer,Boolean> {

        private ProgressDialog progreso;

        @Override protected void onPreExecute() {
            progreso = new ProgressDialog(Salida2.this);
            progreso.setMessage("Guardando información...");
            progreso.setCancelable(false);
            progreso.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            boolean result = true;

            SharedPreferences sharedPref = getSharedPreferences("ServidorPreferences", Context.MODE_PRIVATE);
            String URL= sharedPref.getString("servidor","null");
            String NAMESPACE = "http://tempuri.org/";
            String SOAP_ACTION = "http://tempuri.org/Alta_Salida";
            String METODO = "Alta_Salida";


            ArrayList<String> parametros = new ArrayList<>();
            parametros.add("CodBar"); parametros.add("persona");
            parametros.add("turno"); parametros.add("ubicacion");

            SoapObject Solicitud = new SoapObject(NAMESPACE, METODO);
            String valor="";

            for(int i=0; i<parametros.size(); i++)
            {
                switch (i)
                {
                    case 0: valor= infoVehiculoSalida.getCodigo(); break;
                    case 1: valor= infoVehiculoSalida.getAplicador(); break;
                    case 2: valor= infoVehiculoSalida.getTurno(); break;
                    case 3: valor= infoVehiculoSalida.getUbicacion(); break;
                }

                PropertyInfo PrimerParametro = new PropertyInfo ();
                PrimerParametro.setName (parametros.get(i));
                PrimerParametro.setValue (valor);

                Solicitud.addProperty (PrimerParametro);

            }

            SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope (SoapEnvelope.VER11);

            Envoltorio.dotNet = true;
            Envoltorio.setOutputSoapObject (Solicitud);

            HttpTransportSE TransporteHttp = new HttpTransportSE(URL);

            try{
                TransporteHttp.call(SOAP_ACTION, Envoltorio);
                CadenaDevuelta= Envoltorio.getResponse().toString();
            }catch (Exception e)
            {
                result=false;
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
                Toast.makeText(getApplication(), CadenaDevuelta, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplication(), Salida1.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(getApplication(), "Error en la comunicación", Toast.LENGTH_LONG).show();
            }
        }
    }

    private View.OnClickListener rbListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            seleccionado=true;
            retrabajo = ((RadioButton) view).getText().toString();
            for(int i=0; i<retrabajos.size(); i++)
            {
                if(retrabajos.get(i).equals(retrabajo))
                {
                    retrabajo_id = retrabajos_id.get(i) ;
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
