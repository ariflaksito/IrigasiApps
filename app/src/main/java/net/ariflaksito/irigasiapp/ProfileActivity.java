package net.ariflaksito.irigasiapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView txt1 = (TextView)findViewById(R.id.txt1);
        TextView txt2 = (TextView)findViewById(R.id.txt2);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        txt1.setText(pref.getString("name",""));
        txt2.setText(pref.getString("username",""));
    }
}
