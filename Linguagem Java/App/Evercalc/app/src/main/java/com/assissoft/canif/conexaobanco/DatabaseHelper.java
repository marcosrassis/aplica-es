package com.assissoft.canif.conexaobanco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Marcos on 11/11/2016.
 *
 */
class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mInstance = null;

    private static final String DATABASE_NAME = "canif";
    private static final int DATABASE_VERSION = 6;

    static synchronized DatabaseHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    private DatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {
        //bd.execSQL("create table cotacao(_id integer primary key autoincrement, data text not null, codigo text not null, tipo text not null, sigla text not null, txCompra real not null, txVenda real not null, parCompra real not null, parVenda real not null);");
        bd.execSQL("create table preferencia(_id integer primary key autoincrement, registro text not null, casasdecimais integer not null, notacao integer not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int arg1, int arg2) {
        //bd.execSQL("drop table cotacao;");
        bd.execSQL("drop table preferencia;");
        onCreate(bd);
    }
}
