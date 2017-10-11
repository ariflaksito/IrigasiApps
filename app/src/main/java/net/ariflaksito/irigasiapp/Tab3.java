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

import net.ariflaksito.controls.IrigasiLogic;
import net.ariflaksito.lib.AccessApi;
import net.ariflaksito.models.Irigasi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                IrigasiLogic idata = new IrigasiLogic(cx);
                List<Irigasi> data = idata.get();

                    gmap = googleMap;
                    for(int i = 0; i< data.size(); i++) {
                        Irigasi irigasi = data.get(i);
                        LatLng point = new LatLng(irigasi.getLat(), irigasi.getLon());

                        gmap.setMyLocationEnabled(true);
                        CameraPosition cp = new CameraPosition.Builder().target(point).zoom(12).build();
                        gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));

                        // Create Marker
                        gmap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.placeholder))
                                .title(irigasi.getIname()).snippet(irigasi.getAddr())
                                .position(point));
                    }



            }
        });

        return rootView;
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

