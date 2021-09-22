package com.assissoft.canif;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.assissoft.canif.conversor.model.DefConversor;
import com.assissoft.canif.conversor.ui.ConverteTodasUnidades;
import com.assissoft.canif.conversor.ui.MoedaFragment;
import com.assissoft.canif.conversor.ui.MoedaListFragment;
import com.assissoft.canif.conversor.ui.UnidadeFragment;
import com.assissoft.canif.conversor.ui.UnidadeListFragment;
import com.assissoft.canif.simcalc.model.DefSimcalc;
import com.assissoft.canif.simcalc.ui.antecipacao.AntecipacaoFragment;
import com.assissoft.canif.simcalc.ui.aplicacao.AplicacaoFragment;
import com.assissoft.canif.simcalc.ui.aquisicao_de_veiculo.AquisicaoFragment;
import com.assissoft.canif.simcalc.ui.comprar_a_vista.CompraFragment;
import com.assissoft.canif.simcalc.ui.juntar_dinheiro.DinheiroFragment;
import com.assissoft.canif.simcalc.ui.emprestimo.EmprestimoFragment;
import com.assissoft.canif.simcalc.ui.aquisicao_de_imovel.FinanciamentoFragment;
import com.assissoft.canif.simcalc.ui.viver_de_renda.RendaFragment;

/**
 * Created by Marcos on 01/08/2016.
 *
 */
class OpenFragmentCanif {
    private Fragment fragment = null;
    private final boolean conversor;

    OpenFragmentCanif(int position){
        this.conversor = position == 1;
    }

    void openFragment(FragmentManager manager, String TAG, Bundle args) {

        //Classes do Conversor de Unidades de Medida

        if (DefConversor.MOEDA.equals(TAG)) {
            fragment = new MoedaFragment();
        }

        if (DefConversor.LISTA_MOEDA.equals(TAG)) {
            fragment = new MoedaListFragment();
        }

        if (DefConversor.ACELERACAO.equals(TAG) ||
                DefConversor.AREA.equals(TAG) ||
                DefConversor.COMPRIMENTO.equals(TAG) ||
                DefConversor.MASSA.equals(TAG) ||
                DefConversor.TAXAS.equals(TAG) ||
                DefConversor.TEMPERATURA.equals(TAG) ||
                DefConversor.VELOCIDADE.equals(TAG) ||
                DefConversor.VOLUME.equals(TAG) ||
                DefConversor.COMBUSTIVEL.equals(TAG)) {
            fragment = new UnidadeFragment();
        }

        if (DefConversor.TODAS_UNIDADES.equals(TAG)) {
            fragment = new ConverteTodasUnidades();
        }

        //Classes Gerais

        if (DefConversor.SOBRE.equals(TAG)) {
            fragment = new SobreFragmentCanif();
        }

        if (DefConversor.REPORTE.equals(TAG)) {
            fragment = new ReporteFragmentCanif();
        }

        if (DefConversor.DIALOG_CONFIGURACAO.equals(TAG)) {
            fragment = new ConfiguracaoFragmentCanif();
        }

        if (DefConversor.BUSCA.equals(TAG)) {
            fragment = new BuscaGeralCanif();
        }

        //Classes do Simcalc - Simulador Financeiro

        if (DefSimcalc.ANTECIPACAO.equals(TAG)){
            fragment = new AntecipacaoFragment();
        }

        if (DefSimcalc.EMPRESTIMO.equals(TAG)){
            fragment = new EmprestimoFragment();
        }

        if (DefSimcalc.FINANCIAMENTO.equals(TAG)){
            fragment = new FinanciamentoFragment();
        }

        if (DefSimcalc.AQUISICAO.equals(TAG)){
            fragment = new AquisicaoFragment();
        }

        if (DefSimcalc.APLICACAO.equals(TAG)){
            fragment = new AplicacaoFragment();
        }

        if (DefSimcalc.COMPRA.equals(TAG)){
            fragment = new CompraFragment();
        }

        if (DefSimcalc.RENDA.equals(TAG)){
            fragment = new RendaFragment();
        }

        if (DefSimcalc.DINHEIRO.equals(TAG)){
            fragment = new DinheiroFragment();
        }

        if (conversor){

            FragmentTransaction ft = manager.beginTransaction();
            fragment.setArguments(args);
            ft.replace(R.id.fragment_container,fragment, TAG);
            ft.addToBackStack(DefConversor.LISTA_CONVERSOR);
            ft.commit();

        } else {

            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.simcalc_container, fragment, TAG);
            ft.addToBackStack(DefSimcalc.LISTA);
            ft.commit();

        }

    }

    void openListFragment(FragmentManager manager, String TAG, Bundle args) {

        if (DefConversor.LISTA_ACELERACAO.equals(TAG) ||
                DefConversor.LISTA_AREA.equals(TAG) ||
                DefConversor.LISTA_COMPRIMENTO.equals(TAG) ||
                DefConversor.LISTA_MASSA.equals(TAG) ||
                DefConversor.LISTA_TAXAS.equals(TAG) ||
                DefConversor.LISTA_TEMPERATURA.equals(TAG) ||
                DefConversor.LISTA_VELOCIDADE.equals(TAG) ||
                DefConversor.LISTA_VOLUME.equals(TAG) ||
                DefConversor.LISTA_COMBUSTIVEL.equals(TAG)) {
            fragment = new UnidadeListFragment();
        }

        FragmentTransaction ft = manager.beginTransaction();
        fragment.setArguments(args);
        ft.replace(R.id.fragment_container,fragment,TAG);
        ft.addToBackStack(DefConversor.LISTA_CONVERSOR);
        ft.commit();

    }

}
