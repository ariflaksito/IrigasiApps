package net.ariflaksito.lib;

/**
 * Created by ariflaksito on 10/11/17.
 */



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBClass extends SQLiteOpenHelper {

    public DBClass(Context context) {
        super(context, "dbirigasi", null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE if not exists irigasi "
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "iid INTEGER, iname VARCHAR, addr VARCHAR, lat DOUBLE, lon DOUBLE);");

        db.execSQL("CREATE TABLE if not exists data "
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "aid INTEGER, name VARCHAR, img VARCHAR, "
                + "postdate DATETIME DEFAULT (datetime('now','localtime')), "
                + "tinggi DOUBLE, banjir INT, type VARCHAR, desc VARCHAR);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS irigasi");
        db.execSQL("DROP TABLE IF EXISTS data");
        onCreate(db);

    }

}