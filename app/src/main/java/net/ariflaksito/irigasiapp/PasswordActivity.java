package net.ariflaksito.irigasiapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.ariflaksito.lib.AccessApi;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        final EditText pwd1 = (EditText)findViewById(R.id.input_password1);
        final EditText pwd2 = (EditText)findViewById(R.id.input_password2);

        Button btn = (Button)findViewById(R.id.btn_pwd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(pwd1.getText().toString().length()<4){
                    Toast.makeText(PasswordActivity.this,"Error: Password minimal 4 karakter!",
                            Toast.LENGTH_SHORT).show();
                }else if(!pwd1.getText().toString().equals(pwd2.getText().toString())){
                    Toast.makeText(PasswordActivity.this,"Error: Password Baru dan Konfirmasi harus sama!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                    JSONObject jsdata = new JSONObject();
                    try {
                        jsdata.put("uid", pref.getInt("uid",0));
                        jsdata.put("pwd", pwd1.getText().toString());

                        new PostPasswd().execute(jsdata.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    public class PostPasswd extends AsyncTask<String, String, Boolean> {
        String msg;
        String[] vars;
        AccessApi api;
        private ProgressDialog dialog = new ProgressDialog(
                PasswordActivity.this);

        @Override
        protected Boolean doInBackground(String... params) {
            boolean rs = false;
            vars = params;

            api = new AccessApi(PasswordActivity.this);
            rs = api.postPwd(params);
            return rs;
        }

        protected void onPreExecute() {
            dialog.setMessage("Proses update data..");
            dialog.show();
        }

        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            if (result) {

                try {
                    JSONObject jsOut = new JSONObject(api.getOutput().toString());
                    boolean status = jsOut.getBoolean("sts");

                    Toast.makeText(PasswordActivity.this,jsOut.getString("msg"), Toast.LENGTH_SHORT)
                            .show();

                    if(status){
                        finish();
                    }
                } catch (JSONException e) {
                    Toast.makeText(PasswordActivity.this, "ERROR: Tidak dapat mengirim data, terjadi kesalahan di server",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(PasswordActivity.this, "ERROR: Tidak dapat mengirim data, terjadi kesalahan di server",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
