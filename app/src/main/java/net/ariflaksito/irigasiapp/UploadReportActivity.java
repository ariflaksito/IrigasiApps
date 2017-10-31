package net.ariflaksito.irigasiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.File;
import java.io.IOException;

public class UploadReportActivity extends AppCompatActivity {

    private String filePath = null;
    private String dataJson;

    private ImageView imgPreview;
    private Bitmap bitmap;
    long totalSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_report);

        Intent i = getIntent();
        filePath = i.getStringExtra("filePath");
        dataJson = i.getStringExtra("dataJson");

        Button btnUpload = (Button) findViewById(R.id.btnUpload);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);

        TextView txtName = (TextView) findViewById(R.id.txtName);
        TextView txtDesc = (TextView) findViewById(R.id.txtDesc);

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

        try{
            JSONObject js = new JSONObject(dataJson);
            txtName.setText(js.getString("name"));
            txtDesc.setText(js.getString("report"));

        }catch (JSONException e){
            e.printStackTrace();
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            loading = ProgressDialog.show(UploadReportActivity.this, "Uploading Image", "Please wait...",true,true);
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

                try{
                    JSONObject js = new JSONObject(dataJson);
                    entity.addPart("uid", new StringBody(js.getString("uid")));
                    entity.addPart("report", new StringBody(js.getString("report")));
                    entity.addPart("iid", new StringBody(js.getString("iid")));

                }catch (JSONException e){
                    e.printStackTrace();
                }

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

            loading.dismiss();

            String msg = null;
            try {
                JSONObject jsObj = new JSONObject(result);
                boolean status = jsObj.getBoolean("status");

                if(status) finish();

                msg = jsObj.getString("msg");
            }catch (JSONException e){
                msg = "ERROR: Tidak dapat mengirim data, terjadi kesalahan di server";
                e.printStackTrace();
            }

            Toast.makeText(UploadReportActivity.this, msg, Toast.LENGTH_SHORT).show();

            super.onPostExecute(result);
        }

    }
}
