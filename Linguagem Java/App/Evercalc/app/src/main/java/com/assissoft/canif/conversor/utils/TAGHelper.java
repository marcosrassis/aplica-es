package com.assissoft.canif.conversor.utils;

import android.content.Context;

import com.assissoft.canif.R;
import com.assissoft.canif.conversor.model.DefConversor;
import com.assissoft.canif.conversor.ui.ConversorFragmentComunicator;

/**
 * Created by Marcos on 13/08/2016.
 *
 */
public class TAGHelper {

    private String[] colecao;
    private ConversorFragmentComunicator fc = null;

    public String getTAG(Context context)
    {
        fc = (ConversorFragmentComunicator) context;

        String TAG = "";
        if (fc.getSelectedTabPosition() == 1) {
            colecao = context.getResources().getStringArray(R.array.titulo_conversores);
            TAG = colecao[fc.getCONVERSOR_VIGENTE_ID()];
        }

        return TAG;
    }

    public String getNomeArray(Context context) {

        fc = (ConversorFragmentComunicator) context;

        colecao = context.getResources().getStringArray(R.array.array_nomes_conversores);
        return colecao[fc.getCONVERSOR_VIGENTE_ID()];
    }

    public void setTAGLista(Context context){
        fc = (ConversorFragmentComunicator) context;

        String fragment_vigente = DefConversor.PREFIXO_LISTA + fc.getCONVERSOR_VIGENTE_TAG();
        fc.setCONVERSOR_VIGENTE_TAG(fragment_vigente);
   }

    public void setTAG(Context context, String novaTAG){
        fc = (ConversorFragmentComunicator) context;
        fc.setCONVERSOR_VIGENTE_TAG(novaTAG);
    }

}
