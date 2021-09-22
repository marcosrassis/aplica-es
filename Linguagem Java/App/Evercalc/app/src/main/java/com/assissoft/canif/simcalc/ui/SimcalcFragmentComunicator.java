package com.assissoft.canif.simcalc.ui;

/**
 * Created by Marcos on 24/09/2016.
 *
 */
public interface SimcalcFragmentComunicator
{
    void chamaSimcalcListFragment();
    void chamaCalculoSelecionado(int posicao);
    void atualizaTitulo(String titulo);
    void ocultaBotaoFlutuanteSimcalc();
    void exibeBotaoHome(String titulo);
    void chamaCalculoAntecipadoSelecionado(int posicao);
    void chamaCalculoEmprestimoSelecionado(int posicao);
    void chamaCalculoFinanciamentoSelecionado(int posicao);
    void chamaCalculoEntradaVeiculoSelecionado(int posicao);
    void chamaCalculoAplicacaoSelecionado(int posicao);
    void chamaCalculoCompraSelecionado(int posicao);
    void chamaCalculoRendaSelecionado(int posicao);
    void chamaCalculoDinheiroSelecionado(int posicao);
    int getSIMCALC_VIGENTE_ID();

}
