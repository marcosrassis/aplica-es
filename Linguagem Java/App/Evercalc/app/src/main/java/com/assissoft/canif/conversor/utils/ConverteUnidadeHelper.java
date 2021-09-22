package com.assissoft.canif.conversor.utils;

import java.util.Locale;

/**
 * Created by Marcos on 10/08/2016.
 *
 */
public class ConverteUnidadeHelper {

    public static double calcula(String TAG, String chave, String montante){

        String sinal;
        double  resultado = 0;
        double fator;
        String[][] tabCalculo;

        //Aplica os caracteres gráficos para centímetro quadrado, cúbico, etc.
        chave = novaChave(chave);

        //Recupera o vetor de unidades que vai ser utilizado no cálculo da conversão
        tabCalculo = ArrayUnidadesHelper.getArrayUnidades(TAG);

        //Se a variável for nula ou não tiver valor, atribui zero
        if (montante.equals("")) montante = "0";
        if (montante.equals(".")) montante = "0.";

        try {

            for (String[] aTabCalculo : tabCalculo) {
                if (chave.equals(novaChave(aTabCalculo[0]))) { //Caracteres gráficos
                    fator = Double.parseDouble(aTabCalculo[1]);
                    sinal = aTabCalculo[2];

                    if (sinal.equals("M"))
                        resultado = Double.parseDouble(montante) * fator;
                    else
                        resultado = Double.parseDouble(montante) / fator;

                }
            }

        } catch (Exception ignored) {

        }

        return resultado;
    }

    public static double calculaEquivTaxas(String chave, String montante){
        String priUni;
        String segUni;
        double resultado = 0;

        priUni = chave.substring(0, chave.indexOf("-"));
        segUni = chave.substring(chave.indexOf("-")+1);

        //Se a variável for nula ou não tiver valor, atribui zero
        if (montante.equals("")) montante = "0";
        if (montante.equals(".")) montante = "0.";

        try {

            if(priUni.equals(segUni)) { //ok
                if (!montante.equals(""))
                    resultado = Double.parseDouble(montante);
            }

            if (Locale.getDefault().getLanguage().equals("en")) {
                if(("month").equals(priUni) && ("day").equals(segUni)) { //ok
                    double exp = (1.0/30);
                    double base = (1+(Double.parseDouble(montante)/100));
                    resultado = ((Math.pow(base,exp)) - 1) * 100;
                }
            } else {
                if(("mês").equals(priUni) && ("dia").equals(segUni)) { //ok
                    double exp = (1.0/30);
                    double base = (1+(Double.parseDouble(montante)/100));
                    resultado = ((Math.pow(base,exp)) - 1) * 100;
                }
            }

            if (Locale.getDefault().getLanguage().equals("en")) {
                if(("year").equals(priUni) && ("day").equals(segUni)) { //ok
                    double exp = (1.0/365);
                    double base = (1+(Double.parseDouble(montante)/100));
                    resultado = ((Math.pow(base,exp)) - 1) * 100;
                }
            } else {
                if(("ano").equals(priUni) && ("dia").equals(segUni)) { //ok
                    double exp = (1.0/365);
                    double base = (1+(Double.parseDouble(montante)/100));
                    resultado = ((Math.pow(base,exp)) - 1) * 100;
                }
            }

            if (Locale.getDefault().getLanguage().equals("en")) {
                if(("day").equals(priUni) && ("month").equals(segUni)) { //ok
                    double base = (1+(Double.parseDouble(montante)/100));
                    resultado = ((Math.pow(base,30)) - 1) * 100;
                }
            } else {
                if(("dia").equals(priUni) && ("mês").equals(segUni)) { //ok
                    double base = (1+(Double.parseDouble(montante)/100));
                    resultado = ((Math.pow(base,30)) - 1) * 100;
                }
            }

            if (Locale.getDefault().getLanguage().equals("en")) {
                if(("year").equals(priUni) && ("month").equals(segUni)) { //ok
                    double exp = (1.0/12);
                    double base = (1+(Double.parseDouble(montante)/100));
                    resultado = ((Math.pow(base,exp)) - 1) * 100;
                }
            } else {
                if(("ano").equals(priUni) && ("mês").equals(segUni)) { //ok
                    double exp = (1.0/12);
                    double base = (1+(Double.parseDouble(montante)/100));
                    resultado = ((Math.pow(base,exp)) - 1) * 100;
                }
            }

            if (Locale.getDefault().getLanguage().equals("en")) {
                if(("day").equals(priUni) && ("year").equals(segUni)) { //ok
                    double base = (1+(Double.parseDouble(montante)/100));
                    resultado = ((Math.pow(base,365)) - 1) * 100;
                }
            } else {
                if(("dia").equals(priUni) && ("ano").equals(segUni)) { //ok
                    double base = (1+(Double.parseDouble(montante)/100));
                    resultado = ((Math.pow(base,365)) - 1) * 100;
                }
            }

            if (Locale.getDefault().getLanguage().equals("en")) {
                if(("month").equals(priUni) && ("year").equals(segUni)) { //ok
                    double base = (1+(Double.parseDouble(montante)/100));
                    resultado = ((Math.pow(base,12)) - 1) * 100;
                }
            } else {
                if(("mês").equals(priUni) && ("ano").equals(segUni)) { //ok
                    double base = (1+(Double.parseDouble(montante)/100));
                    resultado = ((Math.pow(base,12)) - 1) * 100;
                }
            }


        } catch (Exception ignored){

        }

        return resultado;

    }

    public static double calculaTemperatura(String chave, String montante){
        String priUni;
        String segUni;
        double resultado = 0;

        priUni = chave.trim().substring(0, chave.indexOf("-"));
        segUni = chave.trim().substring(chave.indexOf("-")+1);

        //Se a variável for nula ou não tiver valor, atribui zero
        if (montante.equals("")) montante = "0";
        if (montante.equals(".")) montante = "0.";

        try {

            if(("°C").equals(priUni) && ("°F").equals(segUni))
                resultado = ((Double.parseDouble(montante) / 5) * 9) + 32;

            if(("°F").equals(priUni) && ("°C").equals(segUni))
                resultado = (5* ((Double.parseDouble(montante) - 32) / 9));

            if(("°C").equals(priUni) && ("°C").equals(segUni))
                resultado = Double.parseDouble(montante);

            if(("°F").equals(priUni) && ("°F").equals(segUni))
                resultado = Double.parseDouble(montante);

            if(("K").equals(priUni) && ("K").equals(segUni))
                resultado = Double.parseDouble(montante);

            if(("°C").equals(priUni) &&  ("K").equals(segUni))
                resultado = Double.parseDouble(montante) + 273;

            if(("K").equals(priUni) && ("°C").equals(segUni))
                resultado = Double.parseDouble(montante) - 273;

            if(("K").equals(priUni) && ("°F").equals(segUni)) {
                double celsius;
                double fahrenheit;
                celsius = Double.parseDouble(montante) - 273;
                fahrenheit = ((celsius / 5) * 9) + 32;
                resultado = fahrenheit;
            }

            if(("°F").equals(priUni) && ("K").equals(segUni)) {
                double celsius;
                double kelvin;
                celsius = (5* ((Double.parseDouble(montante) - 32) / 9));
                kelvin = celsius + 273;
                resultado = kelvin;
            }

        } catch (Exception ignored) {

        }

        return resultado;

    }

    private static String novaChave(String chave){
        String novaChave;
        String priUni;
        String segUni;

        priUni = CaracterHelper.getNewUnit(chave.substring(0, chave.indexOf("-")));
        segUni = CaracterHelper.getNewUnit(chave.substring(chave.indexOf("-")+1));

        novaChave = priUni + "-" + segUni;

        return novaChave;
    }

}
