package net.ariflaksito.irigasiapp;

import android.content.Context;
import android.content.SharedPreferences;
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

    private String data;
    private ArrayList<HashMap<String, String>> dataLocation;
    private ListView lview;
    private Context cx;

    public Tab1(Context cx) {
        this.cx = cx;
        SharedPreferences pref = cx.getSharedPreferences("MyPref", 0);
        data = pref.getString("irigasi","");
    }

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
                    loc.put("id", jsObj.getString("aid"));
                    loc.put("name", jsObj.getString("irigasi"));
                    loc.put("addr", jsObj.getString("desa")+", "+jsObj.getString("kecamatan"));
                    loc.put("desc", (jsObj.getString("type").equals("1"))?"Pintu Irigasi":"Saluran Irigasi");

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
