package net.ariflaksito.controls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ariflaksito.lib.DBClass;
import net.ariflaksito.models.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ariflaksito on 10/11/17.
 */

public class DataLogic implements InData {

    DBClass database;

    public DataLogic(Context c){
        database = new DBClass(c);
    }

    @Override
    public void add(Data d) {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("aid", d.getAid());
        values.put("name", d.getName());
        values.put("img", d.getImg());
//        values.put("postdate", String.valueOf(d.getPostDate()));
        values.put("tinggi", d.getTinggi());
        values.put("banjir", d.getBanjir());
        values.put("desc", d.getDesc());

        db.insert("data", null, values);
        db.close();

    }

    @Override
    public void update(String key, Data i) {

    }

    @Override
    public void delete(String key) {

    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public List<Data> get() {

        List<Data> list = new ArrayList<Data>();

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cur = db.rawQuery("Select aid, name, postdate, tinggi, banjir, desc " +
                "from data", null);

        if (cur.moveToFirst()) {
            do {
                Data d = new Data() {};

                d.setAid(cur.getInt(cur.getColumnIndex("aid")));
                d.setName(cur.getString(cur.getColumnIndex("name")));
                d.setBanjir(cur.getInt(cur.getColumnIndex("banjir")));
                d.setTinggi(cur.getDouble(cur.getColumnIndex("tinggi")));
                d.setPostDate(Timestamp.valueOf(cur.getString(2)));
                d.setDesc(cur.getString(cur.getColumnIndex("desc")));

                list.add(d);
            } while (cur.moveToNext());
        }

        cur.close();
        db.close();

        return list;
    }

    public void remove() {
        SQLiteDatabase db = database.getWritableDatabase();
        String str = "Delete From data";

        db.execSQL(str);
        db.close();

    }
}
