package net.ariflaksito.lib;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * Created by ariflaksito on 9/27/17.
 */

public class AccessApi {

    String uri = "http://192.168.8.101/irigasi-web/api/";
    Context context;
    String out;

    public AccessApi(Context c) {
        context = c;
    }

    public boolean postData(String[] data) {
        HttpConnect conn = new HttpConnect(uri + "indata", 1);
        conn.addVar("data", data[0]);

        boolean rs = conn.postData();
        return rs;
    }

    public boolean login(String[] data){
        HttpConnect conn = new HttpConnect(uri + "login", 1);
        conn.addVar("data", data[0]);

        boolean rs = conn.postData();
        out = conn.getOutput();
        return rs;
    }

    public boolean irigasi(){
        HttpConnect conn = new HttpConnect(uri + "irigasi");

        boolean rs = conn.getData();
        out = conn.getOutput();
        return rs;
    }

    public String getOutput(){
        return out;
    }

}