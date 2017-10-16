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

import net.ariflaksito.lib.AndroidMultiPartEntity;
import net.ariflaksito.models.Config;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class UploadActivity extends AppCompatActivity {

    private String filePath = null;
    private String aid, name, tgg, fld, ket;

    private ImageView imgPreview;
    private Button btnUpload;
    public static final String UPLOAD_KEY = "image";
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

        android.util.Log.d("--irigasiApp--", filePath);

        btnUpload = (Button) findViewById(R.id.btnUpload);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);

        TextView txtName = (TextView) findViewById(R.id.txtName);
        TextView txtHeight = (TextView) findViewById(R.id.txtHeight);
        TextView txtBanjir = (TextView) findViewById(R.id.txtBanjir);
        TextView txtKet = (TextView) findViewById(R.id.txtKet);

        txtName.setText("Irigasi "+name);
        txtHeight.setText("Ketinggian "+tgg);
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

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
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
                //entity.addPart("website",
                //        new StringBody("www.androidhive.info"));
                //entity.addPart("email", new StringBody("abc@gmail.com"));

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
            Log.e("--irigasiApp--", "Response from server: " + result);
            loading.dismiss();

            super.onPostExecute(result);
        }

    }
}
