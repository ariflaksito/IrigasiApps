package net.ariflaksito.irigasiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.ariflaksito.controls.DataLogic;
import net.ariflaksito.lib.AndroidMultiPartEntity;
import net.ariflaksito.models.Config;
import net.ariflaksito.models.Data;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class UploadActivity extends AppCompatActivity {

    private String filePath = null;
    private String aid, name, tgg, fld, ket ,type;

    private ImageView imgPreview;
    private Button btnUpload;
    long totalSize = 0;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Intent i = getIntent();
        filePath = i.getStringExtra("filePath");
        aid = i.getStringExtra("aid");
        name = i.getStringExtra("name");
        tgg = i.getStringExtra("tgg");
        fld = i.getStringExtra("fld");
        ket = i.getStringExtra("ket");
        type = i.getStringExtra("type");

        btnUpload = (Button) findViewById(R.id.btnUpload);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);

        TextView txtName = (TextView) findViewById(R.id.txtName);
        TextView txtHeight = (TextView) findViewById(R.id.txtHeight);
        TextView txtBanjir = (TextView) findViewById(R.id.txtBanjir);
        TextView txtKet = (TextView) findViewById(R.id.txtKet);

        txtName.setText("Irigasi "+name);
        txtHeight.setText("Ketinggian "+tgg+" cm");
        txtKet.setText(ket);
        txtBanjir.setText((fld.equals("1"))?"Terjadi Banjir":"Tidak Banjir");

        if (filePath != null) {
            try {
                previewMedia();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new UploadFileToServer().execute();
            }
        });
    }

    private void previewMedia() throws IOException {

        imgPreview.setVisibility(View.VISIBLE);
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 8;

        bitmap = BitmapFactory.decodeFile(filePath, options);
        //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(filePath));

        imgPreview.setImageBitmap(bitmap);

    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(UploadActivity.this, "Uploading Image", "Please wait...",true,true);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(new Config().getUploadUrl());

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {

                            }
                        });

                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("aid", new StringBody(aid));
                entity.addPart("tinggi", new StringBody(tgg));
                entity.addPart("flood", new StringBody(fld));
                entity.addPart("ket", new StringBody(ket));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {

            String msg = null;
            loading.dismiss();

            try {
                JSONObject jsObj = new JSONObject(result);
                boolean status = jsObj.getBoolean("status");
                msg = jsObj.getString("msg");

                if(status){
                    DataLogic ldata = new DataLogic(getApplicationContext());
                    Data d = new Data() {};

                    d.setAid(Integer.parseInt(aid));
                    d.setName(name);
                    d.setTinggi(Double.parseDouble(tgg));
                    d.setBanjir(Integer.parseInt(fld));
                    d.setType(type);
                    d.setDesc(ket);

                    List<Data> dx = ldata.get();
                    if(dx.size()>20) ldata.remove();

                    ldata.add(d);
                    finish();
                }

            } catch (JSONException e) {
                msg = "ERROR: Tidak dapat mengirim data, terjadi kesalahan di server";
                e.printStackTrace();
            }

            Toast.makeText(UploadActivity.this, msg, Toast.LENGTH_SHORT).show();

            super.onPostExecute(result);
        }

    }

}
