package net.ariflaksito.irigasiapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import net.ariflaksito.controls.DataLogic;
import net.ariflaksito.controls.IrigasiLogic;
import net.ariflaksito.lib.AccessApi;
import net.ariflaksito.models.Data;
import net.ariflaksito.models.Irigasi;

import org.json.JSONException;
import org.json.JSONObject;

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
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

                JSONObject jsdata = new JSONObject();
                try {
                    jsdata.put("uid", pref.getInt("uid",0));
                    jsdata.put("report", dataReport.getText().toString());
                    jsdata.put("img", "");

                    String[] txtIrigasi = irigasi.getText().toString().split("-");
                    jsdata.put("iid", txtIrigasi[0].trim());

                    new PostReport().execute(jsdata.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    public class PostReport extends AsyncTask<String, String, Boolean> {
        String msg;
        String[] vars;
        AccessApi api;
        private ProgressDialog dialog = new ProgressDialog(
                LaporActivity.this);

        public PostReport() {
            msg = "ERROR: Tidak dapat mengirim laporan, periksa koneksi jaringan anda";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean rs = false;
            vars = params;

            api = new AccessApi(LaporActivity.this);
            rs = api.postReport(params);
            return rs;
        }

        protected void onPreExecute() {
            dialog.setMessage("Proses mengirim data..");
            dialog.show();
        }

        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            if (result) {

                try {
                    JSONObject jsOut = new JSONObject(api.getOutput().toString());
                    boolean status = jsOut.getBoolean("sts");

                    Toast.makeText(LaporActivity.this,jsOut.getString("msg"), Toast.LENGTH_SHORT)
                            .show();

                    if(status){
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(LaporActivity.this, msg, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}
