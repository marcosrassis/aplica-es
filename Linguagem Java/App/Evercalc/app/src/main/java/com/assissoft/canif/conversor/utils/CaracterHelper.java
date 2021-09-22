package com.assissoft.canif.conversor.utils;

/**
 * Created by Marcos on 31/08/2016.
 *
 */
class CaracterHelper {
    private static final String zero = "0";
    private static final String dois = "2";
    private static final String três = "3";
    private static final String exp_zero = "°";
    private static final String exp_dois = "²";
    private static final String exp_tres = "³";

    static String getNewUnit(String sigla) {
        String nova;

        int number = sigla.length();
        String ultimo = sigla.substring(number-1);

        switch (ultimo) {
            case "0":
                nova = sigla.replace(zero, exp_zero);
                break;
            case "2":
                nova = sigla.replace(dois, exp_dois);
                break;
            case "3":
                nova = sigla.replace(três, exp_tres);
                break;
            default:
                nova = sigla;
                break;
        }

        return nova;
    }

}
