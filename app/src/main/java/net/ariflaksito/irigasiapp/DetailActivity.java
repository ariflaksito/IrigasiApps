package net.ariflaksito.irigasiapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.ariflaksito.lib.GPSTracker;

import static android.view.View.VISIBLE;

/**
 * Created by ariflaksito on 8/12/17.
 */

public class DetailActivity extends ActionBarActivity {

    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar = getSupportActionBar();
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

        setContentView(R.layout.activity_detail);
        double latitude;
        double longitude;
        float len = 100;

        final String id = getIntent().getExtras().getString("id");
        final String name = getIntent().getExtras().getString("name");
        final String addr = getIntent().getExtras().getString("addr");
        final String desc = getIntent().getExtras().getString("desc");
        final String lat = getIntent().getExtras().getString("lat");
        final String lon = getIntent().getExtras().getString("lon");

        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Location loc = new Location("");
            loc.setLatitude(latitude);
            loc.setLongitude(longitude);

            Location dst = new Location("");
            dst.setLatitude(Double.parseDouble(lat));
            dst.setLongitude(Double.parseDouble(lon));

            len = loc.distanceTo(dst);

        }else{
            gps.showSettingsAlert();
        }

        TextView textName = (TextView) findViewById(R.id.textName);
        textName.setText(name);

        TextView textAddr = (TextView) findViewById(R.id.textAddr);
        textAddr.setText(addr);

        TextView textDesc = (TextView) findViewById(R.id.textDesc);
        textDesc.setText(desc);

        final TextView textLoading = (TextView) findViewById(R.id.textLoading);

        final Button btnGo = (Button) findViewById(R.id.btnGo);
        final ProgressBar loading = (ProgressBar) findViewById(R.id.progressBar);

        final Button btnCek = (Button) findViewById(R.id.btnCek);
        final float finalLen = len;
        btnCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCek.setVisibility(View.GONE);
                loading.setVisibility(VISIBLE);
                textLoading.setVisibility(View.GONE);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textLoading.setVisibility(VISIBLE);
                        boolean a = true;
                        if(a){
//                        if(finalLen <=5000) {
                            loading.setVisibility(View.GONE);
                            btnGo.setVisibility(VISIBLE);
                            textLoading.setText("Anda sudah berada di lokasi dengan jarak "+ finalLen +" meter");
                        }else{
                            btnCek.setVisibility(VISIBLE);
                            loading.setVisibility(View.GONE);
                            textLoading.setTextColor(Color.RED);
                            textLoading.setText("Anda berada pada jarak "+finalLen+" meter yang belum sesuai dengan lokasi, " +
                                    "silahkan menuju lokasi sampai dengan 50 meter dan tekan tombol Cek Lokasi");
                        }
                    }
                }, 3000);
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InputActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("addr", addr);
                intent.putExtra("desc", desc);
                getApplicationContext().startActivity(intent);

                finish();
            }
        });

    }

}
