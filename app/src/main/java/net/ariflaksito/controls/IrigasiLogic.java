package net.ariflaksito.controls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ariflaksito.lib.DBClass;
import net.ariflaksito.models.Irigasi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ariflaksito on 10/11/17.
 */

public class IrigasiLogic implements InIrigasi {

    DBClass database;

    public IrigasiLogic(Context c){
        database = new DBClass(c);
    }

    @Override
    public void add(Irigasi i) {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("iid", i.getIid());
        values.put("iname", i.getIname());
        values.put("addr", i.getAddr());
        values.put("lat", i.getLat());
        values.put("lon", i.getLon());

        db.insert("irigasi", null, values);
        db.close();
    }

    @Override
    public void update(String key, Irigasi i) {

    }

    @Override
    public void delete(String key) {

    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public List<Irigasi> get() {
        List<Irigasi> list = new ArrayList<Irigasi>();

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cur = db.rawQuery("Select _id, iid, iname, addr, lat, lon from irigasi " +
                "Order by iid", null);

        if (cur.moveToFirst()) {
            do {
                Irigasi i = new Irigasi() {};
                i.set_id(cur.getInt(cur.getColumnIndex("_id")));
                i.setIid(cur.getInt(cur.getColumnIndex("iid")));
                i.setIname(cur.getString(cur.getColumnIndex("iname")));
                i.setAddr(cur.getString(cur.getColumnIndex("addr")));
                i.setLat(cur.getDouble(cur.getColumnIndex("lat")));
                i.setLon(cur.getDouble(cur.getColumnIndex("lon")));

                list.add(i);
            } while (cur.moveToNext());
        }

        cur.close();
        db.close();

        return list;
    }

    public void remove() {
        SQLiteDatabase db = database.getWritableDatabase();
        String str = "Delete From irigasi";

        db.execSQL(str);
        db.close();

    }
}
