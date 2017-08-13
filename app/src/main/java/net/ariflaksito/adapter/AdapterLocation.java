package net.ariflaksito.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.ariflaksito.irigasiapp.DetailActivity;
import net.ariflaksito.irigasiapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ariflaksito on 8/10/17.
 */

public class AdapterLocation extends BaseAdapter {

    ArrayList<HashMap<String, String>> data;

    public AdapterLocation(ArrayList<HashMap<String, String>> d){
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
    public View getView(int position, View view, final ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.list_location, parent, false);
        }

        final HashMap<String, String> dataList = data.get(position);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(dataList.get("name"));

        TextView addr = (TextView) view.findViewById(R.id.addr);
        addr.setText(dataList.get("addr"));

        TextView desc = (TextView) view.findViewById(R.id.desc);
        desc.setText(dataList.get("desc"));

        LinearLayout button = (LinearLayout) view.findViewById(R.id.id_loc);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String id = dataList.get("id");
                Intent intent = new Intent(parent.getContext(), DetailActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", dataList.get("id"));
                intent.putExtra("name", dataList.get("name"));
                intent.putExtra("addr", dataList.get("addr"));
                intent.putExtra("desc", dataList.get("desc"));
                parent.getContext().startActivity(intent);

            }
        });

        return view;
    }
}
