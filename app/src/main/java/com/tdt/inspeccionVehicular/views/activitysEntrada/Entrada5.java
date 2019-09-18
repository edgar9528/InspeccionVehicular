package com.tdt.inspeccionVehicular.views.activitysEntrada;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tdt.inspeccionVehicular.R;
import com.tdt.inspeccionVehicular.model.InfoVehiculoEntrada;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class Entrada5 extends AppCompatActivity {

    InfoVehiculoEntrada infoVehiculoEntrada;

    TextView codigo,marca,caja,danLeves,danGraves,destino,color;
    Button button_reg,button_finalizar;
    String CadenaDevuelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada5);
        showToolbar("Resumen",false);

        try {

            infoVehiculoEntrada = (InfoVehiculoEntrada) getIntent().getSerializableExtra("infoVehiculoEntrada");
            button_reg = findViewById(R.id.button_regresar5);
            button_finalizar = findViewById(R.id.button_finalizar);

            marca = findViewById(R.id.tv_marca);
            caja = findViewById(R.id.tv_caja);
            danLeves = findViewById(R.id.tv_danLeves);
            danGraves = findViewById(R.id.tv_danGraves);
            destino = findViewById(R.id.tv_destino);
            color = findViewById(R.id.tv_color);

            marca.setText(infoVehiculoEntrada.getMarca());
            caja.setText(infoVehiculoEntrada.getCaja());
            danLeves.setText(String.valueOf(infoVehiculoEntrada.getDanLeves()));
            danGraves.setText(String.valueOf(infoVehiculoEntrada.getDanGraves()));
            destino.setText(infoVehiculoEntrada.getDestino());
            color.setText(infoVehiculoEntrada.getColor());

            button_reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            button_finalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InsertarEntrada insertarEntrada = new InsertarEntrada();
                    insertarEntrada.execute();
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

    private class InsertarEntrada extends AsyncTask<String,Integer,Boolean> {

        private ProgressDialog progreso;

        @Override protected void onPreExecute() {
            progreso = new ProgressDialog(Entrada5.this);
            progreso.setMessage("Guardando información...");
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
                String SOAP_ACTION = "http://tempuri.org/Alta_Entrada";
                String METODO = "Alta_Entrada";


                ArrayList<String> parametros = new ArrayList<>();
                parametros.add("CodBar");
                parametros.add("color");
                parametros.add("destino");
                parametros.add("ubicacion");
                parametros.add("turno");
                parametros.add("col_desc");
                parametros.add("dest_desc");
                parametros.add("aplicador_cve");
                parametros.add("danosleves");
                parametros.add("danosgraves");
                parametros.add("retrabajos");
                parametros.add("tcaja");
                parametros.add("marca");

                SoapObject Solicitud = new SoapObject(NAMESPACE, METODO);
                String valor = "";

                for (int i = 0; i < parametros.size(); i++) {
                    switch (i) {
                        case 0:
                            valor = infoVehiculoEntrada.getCodigo();
                            break;
                        case 1:
                            valor = infoVehiculoEntrada.getColor_id();
                            break;
                        case 2:
                            valor = infoVehiculoEntrada.getDestino_id();
                            break;
                        case 3:
                            valor = "1";
                            break;
                        case 4:
                            valor = "1";
                            break;
                        case 5:
                            valor = infoVehiculoEntrada.getColor();
                            break;
                        case 6:
                            valor = infoVehiculoEntrada.getDestino();
                            break;
                        case 7:
                            valor = "1";
                            break;
                        case 8:
                            valor = String.valueOf(infoVehiculoEntrada.getDanLeves());
                            break;
                        case 9:
                            valor = String.valueOf(infoVehiculoEntrada.getDanGraves());
                            break;
                        case 10:
                            valor = "";
                            break;
                        case 11:
                            valor = String.valueOf(infoVehiculoEntrada.getCajaClave());
                            break;
                        case 12:
                            valor = String.valueOf(infoVehiculoEntrada.getMarcaClave());
                            break;
                    }

                    PropertyInfo PrimerParametro = new PropertyInfo();
                    PrimerParametro.setName(parametros.get(i));
                    PrimerParametro.setValue(valor);

                    Solicitud.addProperty(PrimerParametro);

                }

                SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                Envoltorio.dotNet = true;
                Envoltorio.setOutputSoapObject(Solicitud);

                HttpTransportSE TransporteHttp = new HttpTransportSE(URL);

                try {
                    TransporteHttp.call(SOAP_ACTION, Envoltorio);
                    CadenaDevuelta = Envoltorio.getResponse().toString();
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
                Toast.makeText(getApplication(), CadenaDevuelta, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplication(), Entrada1.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(getApplication(), "Error en la comunicación", Toast.LENGTH_LONG).show();
            }

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
