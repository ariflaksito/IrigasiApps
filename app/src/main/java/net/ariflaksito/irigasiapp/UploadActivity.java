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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import net.ariflaksito.models.Config;

import net.ariflaksito.lib.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UploadActivity extends AppCompatActivity {

    private String filePath = null;
    private String aid, name, tgg, fld, ket;

    private ImageView imgPreview;
    private Button btnUpload;
    public static final String UPLOAD_KEY = "image";

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
                uploadImage();
            }
        });
    }

    private void previewMedia() throws IOException {

        imgPreview.setVisibility(View.VISIBLE);
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 8;

        //bitmap = BitmapFactory.decodeFile(filePath, options);
        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(filePath));

        imgPreview.setImageBitmap(bitmap);

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UploadActivity.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);

                String result = rh.sendPostRequest(new Config().getUploadUrl(),data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }


}
