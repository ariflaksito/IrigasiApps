package net.ariflaksito.irigasiapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.ariflaksito.lib.AccessApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by ariflaksito on 8/5/17.
 */

public class Tab3 extends Fragment {

    private MapView mapView;
    private GoogleMap gmap;
    private Context cx;

    public Tab3(Context c){
        cx = c;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        View rootView = inflater.inflate(R.layout.map_fragment, container, false);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        GetData loc = new GetData();
        try {
            String out = loc.execute().get();




        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gmap = googleMap;
                LatLng point1 = new LatLng(-7.8099, 110.448);
                LatLng point2 = new LatLng(-7.8204, 110.452);

                gmap.setMyLocationEnabled(true);
                CameraPosition cp = new CameraPosition.Builder().target(point1).zoom(14).build();
                gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));

                // Create Marker
                gmap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.placeholder))
                        .title("Irigasi Semoyo").snippet("Semoyo, Berbah, Sleman")
                    .position(point1));

                gmap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.placeholder))
                        .title("Irigasi Kucir").snippet("Pendem, Berbah, Sleman")
                        .position(point2));

            }
        });

        return rootView;
    }

    public class GetData extends AsyncTask<String, String, String>{
        String msg;
        private ProgressDialog dialog = new ProgressDialog(cx);

        public GetData() {
            msg = "ERROR: Tidak dapat mengirim data, periksa koneksi jaringan anda";
        }

        @Override
        protected String doInBackground(String... params) {
            AccessApi api = new AccessApi(cx);
            api.irigasi();

            return api.getOutput();
        }

        protected void onPreExecute() {
            dialog.setMessage("Proses ambil data..");
            dialog.show();
        }

        protected void onPostExecute(String result) {
            dialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}

