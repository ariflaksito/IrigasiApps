package net.ariflaksito.irigasiapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.ariflaksito.adapter.AdapterHistory;
import net.ariflaksito.adapter.AdapterLocation;
import net.ariflaksito.controls.DataLogic;
import net.ariflaksito.models.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ariflaksito on 8/5/17.
 */

public class Tab2 extends ListFragment {

    private Context cx;

    private ArrayList<HashMap<String, String>> dataHistory;
    private ListView lview;

    public Tab2(Context c){
        cx = c;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        DataLogic ldata = new DataLogic(cx);
        List<Data> dx = ldata.get();

        dataHistory = new ArrayList<>();

        for(Data d : dx){
            HashMap<String, String> loc = new HashMap<>();

            loc.put("id", d.getAid()+"");
            loc.put("loc", d.getName());

            Date dd = new Date();
            dd.setTime(d.getPostDate().getTime());
            loc.put("date", new SimpleDateFormat("dd MMM - HH:mm").format(dd));
            loc.put("type", d.getType().toString());
            loc.put("data1", d.getTinggi()+"");

            dataHistory.add(loc);
        }

        View rootView = inflater.inflate(R.layout.list_fragment, null);
        AdapterHistory adapter = new AdapterHistory(dataHistory);

        setListAdapter(adapter);

        return rootView;
    }
}
