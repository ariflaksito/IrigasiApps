package net.ariflaksito.irigasiapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.ariflaksito.controls.DataLogic;
import net.ariflaksito.controls.IrigasiLogic;
import net.ariflaksito.lib.AccessApi;
import net.ariflaksito.models.Irigasi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        int uid = pref.getInt("uid", 0);

        if(uid>0){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);

            finish();
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button login = (Button) findViewById(R.id.btn_login);
        final EditText usr = (EditText) findViewById(R.id.input_user);
        final EditText pwd = (EditText) findViewById(R.id.input_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsdata = new JSONObject();
                try {
                    jsdata.put("uid", usr.getText().toString());
                    jsdata.put("pwd", pwd.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new Login().execute(jsdata.toString());
            }
        });

    }

    public class Login extends AsyncTask<String, String, Boolean>{
        String msg;
        private ProgressDialog dialog = new ProgressDialog(
                LoginActivity.this);

        public Login() {
            msg = "ERROR: Tidak dapat mengirim data, periksa koneksi jaringan anda";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean rs = false;

            AccessApi api = new AccessApi(LoginActivity.this);
            rs = api.login(params);
            msg = api.getOutput();

            return rs;
        }

        protected void onPreExecute() {
            dialog.setMessage("Proses login..");
            dialog.show();
        }

        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            boolean status = false;
            String message = "";

            try {
                JSONObject js = new JSONObject(msg);
                status = js.getBoolean("status");
                message = js.getString("msg");

                if(status){
                    JSONObject jsUsr = js.getJSONObject("data");
                    JSONArray jsIrg = js.getJSONArray("irigasi");

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor edit = pref.edit();

                    edit.putInt("uid", jsUsr.getInt("uid"));
                    edit.putString("username", jsUsr.getString("username"));
                    edit.putString("name", jsUsr.getString("nama"));
                    edit.putString("addr", jsUsr.getString("alamat"));
                    edit.putString("hp", jsUsr.getString("hp"));
                    edit.putString("irigasi", jsIrg.toString());

                    edit.commit();

                    IrigasiLogic idata = new IrigasiLogic(getApplicationContext());
                    Irigasi irigasi = new Irigasi(){};

                    idata.remove();

                    JSONArray jsMap = js.getJSONArray("map");
                    for(int i=0; i<jsMap.length(); i++){
                        JSONObject jsDataMap = jsMap.getJSONObject(i);

                        irigasi.setIid(jsDataMap.getInt("irigasiid"));
                        irigasi.setIname(jsDataMap.getString("nama"));
                        irigasi.setAddr(jsDataMap.getString("desa")+", "+jsDataMap.getString("kecamatan"));
                        irigasi.setLat(jsDataMap.getDouble("latitude"));
                        irigasi.setLon(jsDataMap.getDouble("longitude"));

                        idata.add(irigasi);

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(status) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);

                finish();
            }

            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT)
                    .show();
        }

    }
}
