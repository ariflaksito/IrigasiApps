package net.ariflaksito.irigasiapp;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.ariflaksito.adapter.AdapterHistory;
import net.ariflaksito.adapter.AdapterLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ariflaksito on 8/5/17.
 */

public class Tab2 extends ListFragment {

    private String data = "[{'id': 1, 'loc': 'Lokasi 1', 'date': '08/13/2017 13:51', 'data1': '25.3'}," +
            "{'id': 2, 'loc': 'Lokasi 1', 'date': '08/13/2017 17:04', 'data1': '25.52'}," +
            "{'id': 3, 'loc': 'Lokasi 2', 'date': '08/13/2017 17:23', 'data1': '17.29'}]";

    private ArrayList<HashMap<String, String>> dataHistory;
    private ListView lview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        dataHistory = new ArrayList<>();

        if(data != null){
            try {
                JSONArray jsArray = new JSONArray(data);
                for(int i = 0; i< jsArray.length(); i++){
                    JSONObject jsObj = jsArray.getJSONObject(i);

                    HashMap<String, String> loc = new HashMap<>();
                    loc.put("id", jsObj.getString("id"));
                    loc.put("loc", jsObj.getString("loc"));
                    loc.put("date", jsObj.getString("date"));
                    loc.put("data1", jsObj.getString("data1"));

                    dataHistory.add(loc);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        View rootView = inflater.inflate(R.layout.list_fragment, null);
        AdapterHistory adapter = new AdapterHistory(dataHistory);

        setListAdapter(adapter);

        return rootView;
    }
}
