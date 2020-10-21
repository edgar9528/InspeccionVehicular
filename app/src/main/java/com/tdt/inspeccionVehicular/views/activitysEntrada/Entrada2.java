package com.tdt.inspeccionVehicular.views.activitysEntrada;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.tdt.inspeccionVehicular.R;
import com.tdt.inspeccionVehicular.model.InfoVehiculoEntrada;


public class Entrada2 extends AppCompatActivity {

    CheckBox leves,graves;
    EditText et_leves,et_graves;
    Button buttonSig;
    Button buttonRet;
    InfoVehiculoEntrada infoVehiculoEntrada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada2);
        showToolbar(getString(R.string.tv_danos),false);

        try {

            infoVehiculoEntrada = (InfoVehiculoEntrada) getIntent().getSerializableExtra("infoVehiculoEntrada");

            buttonRet = findViewById(R.id.button_regresar2);
            buttonSig = findViewById(R.id.button_siguiente2);
            leves = findViewById(R.id.checkLeves);
            graves = findViewById(R.id.checkGraves);

            et_leves = findViewById(R.id.tv_leve);
            et_graves = findViewById(R.id.tv_grave);

            et_leves.setEnabled(false);
            et_graves.setEnabled(false);

            leves.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (leves.isChecked()) {
                        et_leves.setEnabled(true);
                        et_leves.setText("");
                        et_leves.requestFocus();
                    } else {
                        et_leves.setText("0");
                        et_leves.setEnabled(false);
                    }
                }
            });

            graves.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (graves.isChecked()) {
                        et_graves.setEnabled(true);
                        et_graves.setText("");
                        et_graves.requestFocus();
                    } else {
                        et_graves.setText("0");
                        et_graves.setEnabled(false);
                    }
                }
            });

            buttonSig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String dLeves = et_leves.getText().toString();
                    String dGraves = et_graves.getText().toString();
                    if (!dLeves.isEmpty() && !dGraves.isEmpty()) {
                        int dl = Integer.parseInt(dLeves);
                        int dg = Integer.parseInt(dGraves);
                        infoVehiculoEntrada.setDanLeves(dl);
                        infoVehiculoEntrada.setDanGraves(dg);
                        Intent intent = new Intent(getApplication(), Entrada3.class);
                        intent.putExtra("infoVehiculoEntrada", infoVehiculoEntrada);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplication(), getString(R.string.msg_rellenaCampos), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            buttonRet.setOnClickListener(new View.OnClickListener() {
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

    public void showToolbar(String title, boolean upButton)
    {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
