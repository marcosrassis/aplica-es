package com.assissoft.canif.conversor.utils;

import android.content.Context;
import android.content.res.TypedArray;
import com.assissoft.canif.R;
import com.assissoft.canif.conversor.model.Conversor;
import com.assissoft.canif.conversor.model.DefConversor;
import com.assissoft.canif.conversor.model.ListaTodasUnidades;
import com.assissoft.canif.conversor.model.Unidade;
import com.assissoft.canif.conversor.ui.ConversorFragmentComunicator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcos on 01/12/2016.
 *
 */
public class ListasHelper {
    private final List<Conversor> listAux = new ArrayList<>();
    private String[] titulos;
    private String[] descricoes;
    private TypedArray imagens;

    public List<Conversor> getConversorList(Context contexto) {
        titulos = contexto.getResources().getStringArray(R.array.titulo_conversores);
        descricoes = contexto.getResources().getStringArray(R.array.descricao_conversores);
        imagens = contexto.getResources().obtainTypedArray(R.array.icons_conversores);

        for(int i = 0; i < titulos.length; i++) {

            Conversor c = new Conversor();
            c.setTitulo(titulos[i]);
            c.setDescricao(descricoes[i]);
            c.setImagem(imagens.getResourceId(i,0));

            listAux.add(c);
        }

        return listAux;
    }

    private int getIdIconMoeda(String siglaMoeda, Context context){
        int resId = 0;
        String resName ="";

        String[] array_icones_moedas;
        array_icones_moedas = context.getResources().getStringArray(R.array.icons_moedas);

        try {

            for (String array_icones_moeda : array_icones_moedas) {
                if (siglaMoeda.equals(TextoHelper.extraiSiglaMoeda(array_icones_moeda))) {
                    resName = TextoHelper.extraiNome(array_icones_moeda).trim();
                }
            }

            resId = context.getResources().getIdentifier(resName,"mipmap",context.getPackageName());

        } catch (Exception e) {
            LogHelper.e(DefConversor.LISTA_MOEDA, e ,"Falhou o getIdIconMoeda(" + siglaMoeda+ ")");
        }

        return resId;
    }

    public List<Conversor> getMoedaList(Context contexto) {
        titulos = contexto.getResources().getStringArray(R.array.moedas);
        descricoes = contexto.getResources().getStringArray(R.array.moedas);

        for(int i = 0; i < titulos.length; i++) {

            Conversor c = new Conversor();
            c.setTitulo(TextoHelper.extraiNome(titulos[i]));
            c.setDescricao(TextoHelper.extraiSiglaMoeda(descricoes[i]));
            c.setImagem(getIdIconMoeda(TextoHelper.extraiSiglaMoeda(titulos[i]),contexto));

            listAux.add(c);
        }

        return listAux;
    }

    public List<Conversor> getUnidadeList(Context context) {
        TAGHelper tagHelper = new TAGHelper();

        titulos = context.getResources().getStringArray(context.getResources().getIdentifier(tagHelper.getNomeArray(context),"array",context.getPackageName()));
        descricoes = context.getResources().getStringArray(context.getResources().getIdentifier(tagHelper.getNomeArray(context),"array",context.getPackageName()));
        imagens = context.getResources().obtainTypedArray(R.array.icons_conversores);

        //Inicializa a Interdace de comunição entre fragments
        ConversorFragmentComunicator fc = (ConversorFragmentComunicator) context;

        for(int i = 0; i < titulos.length; i++) {

            Conversor c = new Conversor();
            c.setTitulo(TextoHelper.extraiNome(titulos[i]));
            c.setDescricao(TextoHelper.extraiSigla(descricoes[i]));
            c.setImagem(imagens.getResourceId(fc.getCONVERSOR_VIGENTE_ID(),0));

            listAux.add(c);
        }

        return listAux;
    }

    public ListaTodasUnidades getUnidadesTodas(Context context, List<Unidade> unidades) {
        imagens = context.getResources().obtainTypedArray(R.array.icons_conversores);
        ListaTodasUnidades listaTodasUnidades;

        //Inicializa a Interdace de comunição entre fragments
        ConversorFragmentComunicator fc = (ConversorFragmentComunicator) context;

        for(int i = 0; i < unidades.size(); i++) {

            Conversor c = new Conversor();
            c.setTitulo(unidades.get(i).getDescricao() + " (" + CaracterHelper.getNewUnit(unidades.get(i).getSigla()) + ")");
            c.setDescricao(unidades.get(i).getValor());

            if (unidades.size() >= 150) {
                c.setImagem(getIdIconMoeda(unidades.get(i).getSigla(),context));
            } else {
                c.setImagem(imagens.getResourceId(fc.getCONVERSOR_VIGENTE_ID(),0));
            }

            listAux.add(c);
        }

        listaTodasUnidades = new ListaTodasUnidades(listAux);

        return listaTodasUnidades;
    }


}
