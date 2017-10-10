package net.ariflaksito.lib;

/**
 * Created by ariflaksito on 10/11/17.
 */



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBClass extends SQLiteOpenHelper {

    public DBClass(Context context) {
        super(context, "dbirigasi", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE if not exists irigasi "
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "iid INTEGER, iname TEXT, addr TEXT, lat DOUBLE, lon DOUBLE);");

        db.execSQL("CREATE TABLE if not exists data "
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "aid INTEGER, name TEXT, img TEXT, "
                + "postdate DATETIME, tinggi DOUBLE, banjir INT, desc TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

}