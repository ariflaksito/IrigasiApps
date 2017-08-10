package net.ariflaksito.irigasiapp;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import net.ariflaksito.adapter.AdapterLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ariflaksito on 8/5/17.
 */

public class Tab1 extends ListFragment {

    private String data = "[{'id': 1, 'name': 'Lokasi 1', 'addr': 'Sebelah barat Sungai', 'desc': 'Info ketinggian Dam'}," +
            "{'id': 2, 'name': 'Lokasi 2', 'addr': 'Sebelah timur Sungai', 'desc': 'Info tambahan lainnya'}]";

    private ArrayList<HashMap<String, String>> dataLocation;
    private ListView lview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        dataLocation = new ArrayList<>();

        if(data != null){
            try {
                JSONArray jsArray = new JSONArray(data);
                for(int i = 0; i< jsArray.length(); i++){
                    JSONObject jsObj = jsArray.getJSONObject(i);

                    HashMap<String, String> loc = new HashMap<>();
                    loc.put("id", jsObj.getString("id"));
                    loc.put("name", jsObj.getString("name"));
                    loc.put("addr", jsObj.getString("addr"));
                    loc.put("desc", jsObj.getString("desc"));

                    dataLocation.add(loc);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        View rootView = inflater.inflate(R.layout.list_fragment, null);
        AdapterLocation adapter = new AdapterLocation(dataLocation);

        setListAdapter(adapter);

        return rootView;
    }

}
