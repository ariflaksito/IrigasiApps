package net.ariflaksito.irigasiapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import net.ariflaksito.controls.IrigasiLogic;
import net.ariflaksito.models.Irigasi;

import java.util.ArrayList;
import java.util.List;

import static net.ariflaksito.irigasiapp.R.id.spinner;

public class LaporActivity extends AppCompatActivity{

    Spinner irigasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor);

        irigasi = (Spinner) findViewById(spinner);
        loadSpinnerData();

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void loadSpinnerData() {
        IrigasiLogic logic = new IrigasiLogic(getApplicationContext());
        List<Irigasi> lables = logic.get();
        List<String> label = new ArrayList<>();

        for(int i = 0; i<lables.size(); i++){
            Irigasi ir = lables.get(i);
            String l = ir.getIid()+" - "+ir.getIname();

            label.add(l);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, label);

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        irigasi.setAdapter(dataAdapter);
    }


}
