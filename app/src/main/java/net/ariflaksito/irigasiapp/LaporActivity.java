package net.ariflaksito.irigasiapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LaporActivity extends AppCompatActivity{

    MaterialBetterSpinner irigasi;
    private static String IMAGE_DIRECTORY_NAME = "IrigasiApp";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Uri fileUri;
    private String dataJson;

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

        ImageView imgFoto = (ImageView) findViewById(R.id.imgFoto);
        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!irigasi.getText().toString().equals("") && !dataReport.getText().toString().equals("")) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                    JSONObject jsdata = new JSONObject();
                    try {
                        jsdata.put("uid", pref.getInt("uid",0));
                        jsdata.put("report", dataReport.getText().toString());

                        String[] txtIrigasi = irigasi.getText().toString().split("-");
                        jsdata.put("name", txtIrigasi[1].trim());
                        jsdata.put("iid", txtIrigasi[0].trim());

                        dataJson = jsdata.toString();
                        captureImage();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(LaporActivity.this,"Error: Irigasi dan Laporan Detail harus diisikan!",
                            Toast.LENGTH_SHORT).show();
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

    private void captureImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri();

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri() {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }else{
                uri =  Uri.fromFile(getOutputMediaFile());
            }
        }else{

            uri =  Uri.fromFile(getOutputMediaFile());
        }

        return uri;

    }

    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                android.util.Log.d("--irigasiApp--", "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");


        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                launchUploadActivity();

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

    private void launchUploadActivity(){

        Intent i = new Intent(LaporActivity.this, UploadReportActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("dataJson", dataJson);

        startActivity(i);
        finish();
    }
}
