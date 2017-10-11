package net.ariflaksito.irigasiapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.ariflaksito.controls.DataLogic;
import net.ariflaksito.lib.AccessApi;
import net.ariflaksito.models.Data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ariflaksito on 8/12/17.
 */

public class InputActivity extends ActionBarActivity {

    private static final int MY_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST = 100;
    private ActionBar actionBar;
    private Camera camera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar = getSupportActionBar();
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

        setContentView(R.layout.activity_input);

        final String id = getIntent().getExtras().getString("id");
        final String name = getIntent().getExtras().getString("name");
        final String addr = getIntent().getExtras().getString("addr");
        final String desc = getIntent().getExtras().getString("desc");

        TextView textName = (TextView) findViewById(R.id.textName);
        textName.setText(name);

        final EditText tgg = (EditText) findViewById(R.id.input_data1);
        final EditText ket = (EditText) findViewById(R.id.input_data2);
        final CheckBox bjr = (CheckBox) findViewById(R.id.banjir);

        ImageView imgFoto = (ImageView) findViewById(R.id.imgFoto);
        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hasCamera = "check";
                if(checkCameraHardware(getApplicationContext())){
                    checksCameraPermission();

                }
                    
                android.util.Log.i(getApplicationContext().getPackageName(), hasCamera);


            }
        });

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsdata = new JSONObject();
                try {
                    jsdata.put("aid", id);
                    jsdata.put("name", name);
                    jsdata.put("tgg", tgg.getText().toString());
                    jsdata.put("fld", (bjr.isChecked())?"1":"0");
                    jsdata.put("ket", ket.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new PostData().execute(jsdata.toString());
            }
        });

    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }

    private void checksCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (this.checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_REQUEST_CODE);

                if (! shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    showMessageOKCancel("You need to allow camera usage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(InputActivity.this, new String[] {Manifest.permission.CAMERA},
                                            MY_REQUEST_CODE);
                                }
                            });
                }
            }
            else {
                accessCamera();

            }
        }
        else {
            accessCamera();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void accessCamera(){
        try {
            Intent cameraIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class PostData extends AsyncTask<String, String, Boolean>{
        String msg;
        String[] vars;
        private ProgressDialog dialog = new ProgressDialog(
                InputActivity.this);

        public PostData() {
            msg = "ERROR: Tidak dapat mengirim data, periksa koneksi jaringan anda";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean rs = false;
            vars = params;

            AccessApi api = new AccessApi(InputActivity.this);
            rs = api.postData(params);
            return rs;
        }

        protected void onPreExecute() {
            dialog.setMessage("Proses mengirim data..");
            dialog.show();
        }

        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            if (result) {
                Toast.makeText(InputActivity.this,
                        "Data berhasil dikirim ke server", Toast.LENGTH_SHORT)
                        .show();
                finish();

                try {
                    DataLogic ldata = new DataLogic(getApplicationContext());
                    JSONObject jsObj = new JSONObject(vars[0]);

                    Data d = new Data() {};

                    d.setAid(jsObj.getInt("aid"));
                    d.setName(jsObj.getString("name"));
                    d.setTinggi(Double.parseDouble(jsObj.getString("tgg")));
                    d.setBanjir(jsObj.getInt("fld"));
                    d.setDesc(jsObj.getString("ket"));

                    ldata.add(d);

                }catch (JSONException e){
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(InputActivity.this, msg, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}
