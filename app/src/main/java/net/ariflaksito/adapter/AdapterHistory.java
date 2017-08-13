package net.ariflaksito.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.ariflaksito.irigasiapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ariflaksito on 8/13/17.
 */

public class AdapterHistory extends BaseAdapter {

    ArrayList<HashMap<String, String>> data;

    public AdapterHistory(ArrayList<HashMap<String, String>> d){
        data = d;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.list_history, parent, false);
        }

        final HashMap<String, String> dataList = data.get(position);

        TextView name = (TextView) view.findViewById(R.id.locname);
        name.setText(dataList.get("loc"));

        TextView date = (TextView) view.findViewById(R.id.date);
        date.setText(dataList.get("date"));

        TextView dt1 = (TextView) view.findViewById(R.id.data1);
        dt1.setText(dataList.get("data1"));

        LinearLayout button = (LinearLayout) view.findViewById(R.id.id_his);

        return view;
    }
}
