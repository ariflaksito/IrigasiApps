package net.ariflaksito.controls;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.ariflaksito.lib.DBClass;
import net.ariflaksito.models.Data;

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
    public void add(Data i) {

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
        return null;
    }

    public void remove() {
        SQLiteDatabase db = database.getWritableDatabase();
        String str = "Delete From data";

        db.execSQL(str);
        db.close();

    }
}
