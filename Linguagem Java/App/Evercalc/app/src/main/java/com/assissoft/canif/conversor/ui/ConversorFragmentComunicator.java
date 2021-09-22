package com.assissoft.canif.conversor.ui;

import android.os.Bundle;

/**
 * Created by Marcos on 11/05/2016.
 *
 */
public interface ConversorFragmentComunicator
{
    void chamaConversorSelecionado(int conversor);
    void chamaConversorDeMoeda(Bundle args);
    void chamaListaDeMoeda(Bundle args);
    void chamaConversorDeUnidade(Bundle args);
    void chamaListaDeUnidade(Bundle args);
    void chamaConversorListFragment();
    void atualizaTitulo(String titulo);
    void exibeBotaoFlutuante(int visibilidade);
    void defineBotaoTelaReporte();
    void exibeDialogoCasasDecimais();
    void exibeOuNaoMenuConversaoTodas(boolean valor);
    void exibeBotaoHome(String titulo);
    void chamaSobreConf();
    void chamaReporteConf();
    String getCONVERSOR_VIGENTE_TAG();
    void setCONVERSOR_VIGENTE_TAG(String tag);
    int getCONVERSOR_VIGENTE_ID();
    int getSelectedTabPosition();

}
