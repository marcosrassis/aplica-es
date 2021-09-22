package com.assissoft.canif.conversor.utils;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created by Marcos on 12/08/2016.
 *
 */
public class FormatacaoHelper {

    public static String numero(double valor, int casasDecimais){

        String posicoes = "";
        String mascara;
        DecimalFormat formato;
        String resultado;

        for (int i = 0; i < casasDecimais; i++) {
            posicoes += "0";
        }

        if (casasDecimais > 0) {
            mascara = "#,##0." + posicoes;
        } else {
            mascara = "#,##0";
        }

        formato = new DecimalFormat(mascara);
        resultado = formato.format(valor);

        return resultado;
    }

    public static String nc(double valor, int casasDecimais){

        String posicoes = "";
        String mascara;
        DecimalFormat formato;
        String resultado;

        for (int i = 0; i < casasDecimais; i++) {
            posicoes += "0";
        }

        posicoes = posicoes.replaceAll("0","#");
        mascara = "0." + posicoes + "E0";

        formato = new DecimalFormat(mascara);
        resultado = formato.format(valor);

        return resultado;
    }

    public static String moeda(double valor, int casasDecimais){
        String posicoes = "";
        String mascara;
        DecimalFormat formato;
        String resultado;

        for (int i = 0; i < casasDecimais; i++) {
            posicoes += "0";
        }

        if (casasDecimais > 0) {
            mascara = "#,##0." + posicoes;
        } else {
            mascara = "#,##0";
        }

        formato = new DecimalFormat(mascara);
        resultado = formato.format(valor);

        return resultado;
    }

}
