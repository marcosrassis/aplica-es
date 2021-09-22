package com.assissoft.canif.conexaobanco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.assissoft.canif.simcalc.model.Preferencia;

/**
 * Created by Marcos on 12/11/2016.
 *
 */
public class ComandosBD {
    private final SQLiteDatabase bd;

    public ComandosBD(Context context) {
        DatabaseHelper auxBD = DatabaseHelper.getInstance(context);
        this.bd = auxBD.getWritableDatabase();
    }

    public void inserirPreferencia(Preferencia preferencia){
        ContentValues valores = new ContentValues();
        valores.put("registro",preferencia.getRegistro());
        valores.put("casasdecimais",preferencia.getCasasdecimais());
        valores.put("notacao",preferencia.getNotacao());

        bd.insert("preferencia", null, valores);

    }

    public void atualizarPreferencia(Preferencia preferencia){
        ContentValues valores = new ContentValues();
        valores.put("registro",preferencia.getRegistro());
        valores.put("casasdecimais",preferencia.getCasasdecimais());
        valores.put("notacao",preferencia.getNotacao());

        bd.update("preferencia", valores, "_id = ?", new String[] {""+preferencia.getId()});
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        bd.close();
    }

    public Preferencia buscarPreferencia() {
        Preferencia p = new Preferencia();

        String[] colunas = new String[] {"_id", "registro", "casasdecimais", "notacao"};
        Cursor cursor = bd.query("preferencia", colunas,"registro = ?", new String[] {""+"'configuracao'"},null,null,null,null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            do{
                p.setId(cursor.getLong(0));
                p.setRegistro(cursor.getString(1));
                p.setCasasdecimais(cursor.getInt(2));
                p.setNotacao(cursor.getInt(3));

            }while(cursor.moveToNext());
        }

        cursor.close();
        return p;
    }

}
