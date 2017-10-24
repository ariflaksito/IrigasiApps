package net.ariflaksito.irigasiapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import net.ariflaksito.controls.IrigasiLogic;
import net.ariflaksito.models.Irigasi;

import java.util.ArrayList;
import java.util.List;

public class LaporActivity extends AppCompatActivity{

    MaterialBetterSpinner irigasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor);

        final EditText dataReport = (EditText)findViewById(R.id.input_lapor);

        loadSpinnerData();

        Button btn = (Button) findViewById(R.id.btnReport);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.util.Log.d("--irigasiApp--", irigasi.getText().toString());
                android.util.Log.d("--irigasiApp--", dataReport.getText().toString());
            }
        });
    }

    private void loadSpinnerData() {
        IrigasiLogic logic = new IrigasiLogic(getApplicationContext());
        List<Irigasi> lables = logic.get();
        String[] label = new String[lables.size()];

        for(int i = 0; i<lables.size(); i++){
            Irigasi ir = lables.get(i);
            label[i] = ir.getIid()+" - "+ir.getIname();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, label);

        irigasi = (MaterialBetterSpinner) findViewById(R.id.spinner_irigasi);
        irigasi.setAdapter(dataAdapter);
    }


}
