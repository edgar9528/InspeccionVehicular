package com.tdt.inspeccionVehicular.views.administrar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tdt.inspeccionVehicular.Class.DatabaseHelper;
import com.tdt.inspeccionVehicular.R;
import com.tdt.inspeccionVehicular.model.InfoServidor;
import com.tdt.inspeccionVehicular.model.ParametrosWS;
import com.tdt.inspeccionVehicular.views.InicioActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class ModuloComunicacionActivity extends AppCompatActivity {

    Button button_tipo,button_guardar,button_recibir,button_probar;
    EditText et_servidor,et_tipo,et_comunicacion;
    InfoServidor infoServidor;
    String CadenaDevuelta;

    boolean conexionExitosa=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo_comunicacion);

        showToolbar(getString(R.string.tv_comunicaciones),true);

        try {

            et_servidor = findViewById(R.id.et_servidor);
            et_tipo = findViewById(R.id.et_tipo);
            button_tipo = findViewById(R.id.button_tipo);
            button_guardar = findViewById(R.id.button_guardar);
            button_probar = findViewById(R.id.button_probar);
            button_recibir = findViewById(R.id.button_recibir);
            et_comunicacion = findViewById(R.id.et_comunicacion);

            SharedPreferences sharedPref = getSharedPreferences("ServidorPreferences", Context.MODE_PRIVATE);
            String servidor = sharedPref.getString("servidor","null");

            if(!servidor.equals("null")) {
                et_servidor.setText(servidor);
            }

            button_tipo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tipo = et_tipo.getText().toString();
                    if (tipo.equals("Salida"))
                        et_tipo.setText("Entrada");
                    else
                        et_tipo.setText("Salida");
                }
            });

            button_guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!et_servidor.getText().toString().isEmpty()) {
                        String servidor, tipo;
                        servidor = et_servidor.getText().toString();
                        tipo = et_tipo.getText().toString();
                        infoServidor = new InfoServidor(servidor, tipo);
                        guardarServidor();
                        Toast.makeText(getApplication(), getString(R.string.msg_infoGuardada) , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), getString(R.string.msg8), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            button_probar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (infoServidor != null) {
                        ConsultaConexion consultaConexion = new ConsultaConexion();
                        consultaConexion.execute();
                    } else {
                        Toast.makeText(getApplication(), getString(R.string.msg9) , Toast.LENGTH_SHORT).show();
                    }
                }
            });

            button_recibir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (conexionExitosa) {
                        RecibirBase recibirBase = new RecibirBase();
                        recibirBase.execute();
                    } else {
                        Toast.makeText(getApplication(), getString(R.string.msg10), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (Exception e)
        {
            Toast.makeText(getApplication(), "Error: "+e.toString(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplication(), InicioActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void guardarServidor()
    {
            SharedPreferences sharedPref = getSharedPreferences("ServidorPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("servidor",infoServidor.getServidor());
            editor.putString("tipo",infoServidor.getTipoApp());
            editor.apply();
    }

    public void salir(View view)
    {
        onBackPressed();
    }

    public void showToolbar(String title, boolean upButton)
    {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }


    private class ConsultaConexion extends AsyncTask<String,Integer,Boolean> {

        private ProgressDialog progreso;

        @Override protected void onPreExecute() {
            progreso = new ProgressDialog(ModuloComunicacionActivity.this);
            progreso.setMessage(getString(R.string.msg_wsVerificando));
            progreso.setCancelable(false);
            progreso.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean result = true;
            try {

                ParametrosWS parametrosWS = new ParametrosWS("PruebaComunicacion", getApplication());

                SoapObject Solicitud = new SoapObject(parametrosWS.getNAMESPACES(), parametrosWS.getMETODO());

                SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                Envoltorio.dotNet = true;
                Envoltorio.setOutputSoapObject(Solicitud);

                HttpTransportSE TransporteHttp = new HttpTransportSE(parametrosWS.getURL());

                try {
                    TransporteHttp.call(parametrosWS.getSOAP_ACTION(), Envoltorio);

                } catch (Exception e) {

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
                et_comunicacion.setText( getString(R.string.msg5) );
                Toast.makeText(getApplication(), getString(R.string.msg5), Toast.LENGTH_SHORT).show();
                conexionExitosa=true;
            }
            else
            {
                et_comunicacion.setText( getString(R.string.msg6) );
                Toast.makeText(getApplication(), getString(R.string.msg6), Toast.LENGTH_SHORT).show();
                conexionExitosa=false;
            }
        }
    }

    private class RecibirBase extends AsyncTask<String,Integer,Boolean> {

        private ProgressDialog progreso;

        @Override protected void onPreExecute() {
            progreso = new ProgressDialog(ModuloComunicacionActivity.this);
            progreso.setMessage(getString(R.string.msg_wsVerificando));
            progreso.setCancelable(false);
            progreso.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            boolean result = true;

            try {

                ArrayList<String> metodos1 = new ArrayList<>();
                ArrayList<String> tablas = new ArrayList<>();
                metodos1.add("GetPais");
                metodos1.add("GetColores");
                metodos1.add("GetDanos");
                metodos1.add("GetLineas");
                metodos1.add("GetRetrabajo");
                metodos1.add("GetTurnos");
                metodos1.add("GetTcajas");
                metodos1.add("GetMarcas");

                tablas.add("PAIS");
                tablas.add("COLORES");
                tablas.add("DANOS");
                tablas.add("LINEAS");
                tablas.add("RETRABAJO");
                tablas.add("TURNOS");
                tablas.add("TCAJAS");
                tablas.add("MARCAS");

                getApplication().deleteDatabase("baseLocal");
                DatabaseHelper databaseHelper = new DatabaseHelper(getApplication(), "baseLocal", null, 1);
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                for (int j = 0; j < tablas.size(); j++) {
                    ParametrosWS parametrosWS = new ParametrosWS(metodos1.get(j), getApplication());
                    SoapObject Solicitud = new SoapObject(parametrosWS.getNAMESPACES(), parametrosWS.getMETODO());
                    SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    Envoltorio.dotNet = true;
                    Envoltorio.setOutputSoapObject(Solicitud);
                    HttpTransportSE TransporteHttp = new HttpTransportSE(parametrosWS.getURL());
                    try {
                        TransporteHttp.call(parametrosWS.getSOAP_ACTION(), Envoltorio);

                        SoapObject response = (SoapObject) Envoltorio.getResponse();
                        SoapObject response2 = (SoapObject) response.getProperty(1);
                        SoapObject s_deals = (SoapObject) response2.getProperty("NewDataSet");
                        for (int i = 0; i < s_deals.getPropertyCount(); i++) {
                            SoapObject s_deals_1 = (SoapObject) s_deals.getProperty(i);

                            String id = s_deals_1.getProperty("ID").toString();
                            String descripcion = s_deals_1.getProperty("DESCRIPCION").toString();
                            //Log.d("salida",id+" "+descripcion);

                            if (db != null) {
                                if (j < 2) {
                                    String consulta = "INSERT INTO " + tablas.get(j) + " VALUES('" + id + "','" + descripcion + "')";
                                    db.execSQL(consulta);
                                } else {
                                    String consulta = "INSERT INTO " + tablas.get(j) + " VALUES(" + id + ",'" + descripcion + "')";
                                    db.execSQL(consulta);
                                }
                            }
                        }
                    } catch (Exception e) {
                        result = false;
                    }
                }

                db.close();

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
                et_comunicacion.setText( getString(R.string.msg7) );
                Toast.makeText(getApplication(), getString(R.string.msg7), Toast.LENGTH_SHORT).show();
            }
            else
            {
                et_comunicacion.setText(getString(R.string.msg6));
                Toast.makeText(getApplication(), getString(R.string.msg6), Toast.LENGTH_SHORT).show();
            }
        }
    }



}


