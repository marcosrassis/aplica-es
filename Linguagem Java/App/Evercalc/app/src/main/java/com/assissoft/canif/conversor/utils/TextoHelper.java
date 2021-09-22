package com.assissoft.canif.conversor.utils;

import android.content.Context;

/**
 * Created by Marcos on 01/09/2016.
 *
 */
public class TextoHelper {

    public static String extraiSigla(String texto) {
        String sigla;
        sigla = texto.substring(texto.indexOf("(")+1,texto.indexOf(")"));
        return CaracterHelper.getNewUnit(sigla);
    }

    public static String extraiSiglaMoeda(String texto) {
        String sigla;
        sigla = texto.substring(texto.indexOf("(")+1,texto.indexOf(")"));
        return sigla;
    }

    static String extraiNome(String texto) {
        String nome;
        nome = texto.substring(0,texto.indexOf("("));
        return nome;
    }

    public static String extraiNome(Context contexto, String sigla) {

        TAGHelper tagHelper = new TAGHelper();
        String nomeUnidade = "";

        //Carrega as unidades do conversor vigente ou em uso no momento
        String[] array_unidades = contexto.getResources().getStringArray(contexto.getResources().getIdentifier(tagHelper.getNomeArray(contexto), "array", contexto.getPackageName()));

        for (String texto : array_unidades) {
            String siglaaux = CaracterHelper.getNewUnit(texto.substring(texto.indexOf("(") + 1, texto.indexOf(")")));
            if (siglaaux.equals(sigla)) nomeUnidade = texto.substring(0, texto.indexOf("(")) + " (" + sigla + ")";
        }

        return nomeUnidade;
    }

}


