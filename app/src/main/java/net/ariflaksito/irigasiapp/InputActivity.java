package net.ariflaksito.irigasiapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

}
