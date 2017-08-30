package net.ariflaksito.irigasiapp;

import android.graphics.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * Created by ariflaksito on 8/5/17.
 */

public class Tab3 extends Fragment {

    private MapView mapView;
    private GoogleMap gmap;

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
                gmap = googleMap;
                LatLng point1 = new LatLng(-7.7579287, 110.4083342);
                LatLng point2 = new LatLng(-7.754134, 110.413205);

                gmap.setMyLocationEnabled(true);
                CameraPosition cp = new CameraPosition.Builder().target(point1).zoom(14).build();
                gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));

                // Create Marker
                gmap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.placeholder))
                        .title("Lokasi 1").snippet("Sebelah barat Sungai")
                    .position(point1));

                gmap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.placeholder))
                        .title("Lokasi 2").snippet("Sebelah timur Sungai")
                        .position(point2));

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

