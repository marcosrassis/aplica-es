package com.assissoft.canif.conversor.model;

import java.util.Locale;

/**
 * Created by Marcos on 16/08/2016.
 *
 */
public class DefConversor {

    //Usado nas conversões de uma unidade de medida para outra unidade de medida
    public static final String ACELERACAO = (Locale.getDefault().getLanguage().equals("en") ? "Acceleration" : "Aceleração");
    public static final String AREA = (Locale.getDefault().getLanguage().equals("en") ? "Area" : "Area");
    public static final String COMPRIMENTO = (Locale.getDefault().getLanguage().equals("en") ? "Length" : "Comprimento");
    public static final String MASSA = (Locale.getDefault().getLanguage().equals("en") ? "Weight" : "Massa");
    public static final String MOEDA = (Locale.getDefault().getLanguage().equals("en") ? "Currency" : "Moeda");
    public static final String TAXAS = (Locale.getDefault().getLanguage().equals("en") ? "Rate" : "Taxas");
    public static final String TEMPERATURA = (Locale.getDefault().getLanguage().equals("en") ? "Temperature" : "Temperatura");
    public static final String VELOCIDADE = (Locale.getDefault().getLanguage().equals("en") ? "Speed" : "Velocidade");
    public static final String VOLUME = (Locale.getDefault().getLanguage().equals("en") ? "Volume" : "Volume");
    public static final String COMBUSTIVEL = (Locale.getDefault().getLanguage().equals("en") ? "Gas" : "Combustível");

    //Usado na chamada da lista de unidades de medida
    public static final String PREFIXO_LISTA = "Selecionar";
    public static final String LISTA_CONVERSOR = "SelecionarConversor";
    public static final String LISTA_ACELERACAO = "Selecionar" + ACELERACAO;
    public static final String LISTA_AREA = "Selecionar" + AREA;
    public static final String LISTA_COMPRIMENTO = "Selecionar" + COMPRIMENTO;
    public static final String LISTA_MASSA = "Selecionar" + MASSA;
    public static final String LISTA_MOEDA = "Selecionar" + MOEDA;
    public static final String LISTA_TAXAS = "Selecionar" + TAXAS;
    public static final String LISTA_TEMPERATURA = "Selecionar" + TEMPERATURA;
    public static final String LISTA_VELOCIDADE = "Selecionar" + VELOCIDADE;
    public static final String LISTA_VOLUME = "Selecionar" + VOLUME;
    public static final String LISTA_COMBUSTIVEL = "Selecionar" + COMBUSTIVEL;

    //Usado na chamada das caixas de Dialogo
    public static final String DIALOG_CONFIGURACAO = "Configuracao";
    public static final String DIALOG_CASAS_DECIMAIS = "DialogCasasDecimais";

    //Usado em outras chamadas
    public static final String SOBRE = "Sobre";
    public static final String REPORTE = "Reporte";
    public static final String TODAS_UNIDADES = "ConverteTodasUnidades";

    //Usado na busca geral
    public static final String BUSCA = "Busca_geral";

    //Exibe ou não os Logs de Debug
    public static final boolean EXIBE_LOG = false;

    //Servidor do Banco Central para cotações de moeda
    public static final String URL_SERVIDOR_COTACOES = "http://www4.bcb.gov.br/Download/fechamento/";

}
