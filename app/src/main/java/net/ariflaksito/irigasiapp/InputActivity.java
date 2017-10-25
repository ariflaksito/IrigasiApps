package net.ariflaksito.irigasiapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ariflaksito on 8/12/17.
 */

public class InputActivity extends ActionBarActivity {

    private ActionBar actionBar;
    private static String IMAGE_DIRECTORY_NAME = "IrigasiApp";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    private String id, name, type;
    private EditText tgg, ket;
    private CheckBox bjr;
    private Uri fileUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar = getSupportActionBar();
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

        setContentView(R.layout.activity_input);

        id = getIntent().getExtras().getString("id");
        name = getIntent().getExtras().getString("name");
        type = getIntent().getExtras().getString("desc");

        TextView textName = (TextView) findViewById(R.id.textName);
        textName.setText(name);

        tgg = (EditText) findViewById(R.id.input_data1);
        ket = (EditText) findViewById(R.id.input_data2);
        bjr = (CheckBox) findViewById(R.id.banjir);

        ImageView imgFoto = (ImageView) findViewById(R.id.imgFoto);
        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!tgg.getText().toString().equals(""))
                    captureImage();
                else{
                    Toast.makeText(InputActivity.this,"Error: Tinggi harus anda isikan dahulu!", Toast.LENGTH_SHORT)
                            .show();
                }

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
                    jsdata.put("type", type);
                    jsdata.put("img", "");
                    jsdata.put("tgg", tgg.getText().toString());
                    jsdata.put("fld", (bjr.isChecked())?"1":"0");
                    jsdata.put("ket", ket.getText().toString());

                    new PostData().execute(jsdata.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public class PostData extends AsyncTask<String, String, Boolean>{
        String msg;
        String[] vars;
        AccessApi api;
        private ProgressDialog dialog = new ProgressDialog(
                InputActivity.this);

        public PostData() {
            msg = "ERROR: Tidak dapat mengirim data, periksa koneksi jaringan anda";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean rs = false;
            vars = params;

            api = new AccessApi(InputActivity.this);
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

                try {
                    JSONObject jsOut = new JSONObject(api.getOutput().toString());
                    boolean status = jsOut.getBoolean("sts");

                    if(status){
                        DataLogic ldata = new DataLogic(getApplicationContext());
                        JSONObject jsObj = new JSONObject(vars[0]);

                        Data d = new Data() {};

                        d.setAid(jsObj.getInt("aid"));
                        d.setName(jsObj.getString("name"));
                        d.setTinggi(Double.parseDouble(jsObj.getString("tgg")));
                        d.setBanjir(jsObj.getInt("fld"));
                        d.setDesc(jsObj.getString("ket"));
                        d.setType(jsObj.getString("type"));

                        ldata.add(d);

                        finish();
                    }

                    Toast.makeText(InputActivity.this,jsOut.getString("msg"), Toast.LENGTH_SHORT)
                            .show();


                }catch (JSONException e){
                    e.printStackTrace();
                }

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

        Intent i = new Intent(InputActivity.this, UploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("aid", id);
        i.putExtra("name", name);
        i.putExtra("tgg", tgg.getText().toString());
        i.putExtra("fld", (bjr.isChecked())?"1":"0");
        i.putExtra("type", type);
        i.putExtra("ket", ket.getText().toString());

        startActivity(i);
        finish();
    }
}
