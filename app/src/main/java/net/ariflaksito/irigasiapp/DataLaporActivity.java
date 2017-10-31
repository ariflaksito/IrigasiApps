package net.ariflaksito.irigasiapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import net.ariflaksito.adapter.AdapterLocation;
import net.ariflaksito.lib.AccessApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DataLaporActivity extends AppCompatActivity {

    private int uid;
    private ListView listView;
    private ArrayList<HashMap<String, String>> dataReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_lapor);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        uid = pref.getInt("uid", 0);

        listView = (ListView) findViewById(R.id.list);
        new FetchData().execute();

    }

    public class FetchData extends AsyncTask<String, String, Boolean> {
        String msg;
        private ProgressDialog dialog = new ProgressDialog(
                DataLaporActivity.this);

        @Override
        protected Boolean doInBackground(String... params) {
            boolean rs = false;

            AccessApi api = new AccessApi(DataLaporActivity.this);
            rs = api.report(uid);
            msg = api.getOutput();

            return rs;
        }

        protected void onPreExecute() {
            dialog.setMessage("Proses Ambil Data..");
            dialog.show();
        }

        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            boolean status = false;
            String data = "";

            if(result) {
                try {
                    JSONObject js = new JSONObject(msg);
                    status = js.getBoolean("status");

                    if (status) {

                        dataReport = new ArrayList<>();

                        JSONArray jsReport = js.getJSONArray("data");
                        for (int i = 0; i < jsReport.length(); i++) {
                            JSONObject jsData = jsReport.getJSONObject(i);
                            HashMap<String, String> report = new HashMap<>();

                            report.put("name", jsData.getString("irigasi"));
                            report.put("addr", jsData.getString("report"));
                            report.put("desc", jsData.getString("postdate"));

                            dataReport.add(report);
                        }

                        AdapterLocation adapter = new AdapterLocation(dataReport);
                        listView.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(DataLaporActivity.this, "ERROR: Tidak dapat mengambil data, terjadi kesalahan di server",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }
}
