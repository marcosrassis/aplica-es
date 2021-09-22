package com.assissoft.canif.simcalc.utils;

import android.content.Context;
import android.content.res.TypedArray;
import com.assissoft.canif.R;
import com.assissoft.canif.simcalc.model.Calculo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcos on 01/12/2016.
 *
 */
public class ListasHelper {
    private final List<Calculo> listAux = new ArrayList<>();
    private String[] titulos;
    private String[] descricoes;
    private TypedArray imagens;

    public List<Calculo> getFinancasList(Context contexto) {
        titulos = contexto.getResources().getStringArray(R.array.calculos_titulo);
        descricoes = contexto.getResources().getStringArray(R.array.calculos_descricao);
        imagens = contexto.getResources().obtainTypedArray(R.array.icons_calculos);

        for(int i = 0; i < titulos.length; i++) {

            Calculo c = new Calculo();
            c.setTitulo(titulos[i]);
            c.setDescricao(descricoes[i]);
            c.setImagem(imagens.getResourceId(i,0));

            listAux.add(c);
        }

        return listAux;
    }

    public List<Calculo> getAntecipacaoList(Context contexto) {
        titulos = contexto.getResources().getStringArray(R.array.antecipacao_titulo);
        descricoes = contexto.getResources().getStringArray(R.array.antecipacao_descricao);
        imagens = contexto.getResources().obtainTypedArray(R.array.antecipacao_icons);

        for(int i = 0; i < titulos.length; i++) {

            Calculo c = new Calculo();
            c.setTitulo(titulos[i]);
            c.setDescricao(descricoes[i]);
            c.setImagem(imagens.getResourceId(i,0));

            listAux.add(c);
        }

        return listAux;
    }

    public List<Calculo> getEmprestimoList(Context contexto) {
        titulos = contexto.getResources().getStringArray(R.array.emprestimo_titulo);
        descricoes = contexto.getResources().getStringArray(R.array.emprestimo_descricao);
        imagens = contexto.getResources().obtainTypedArray(R.array.emprestimo_icons);

        for(int i = 0; i < titulos.length; i++) {

            Calculo c = new Calculo();
            c.setTitulo(titulos[i]);
            c.setDescricao(descricoes[i]);
            c.setImagem(imagens.getResourceId(i,0));

            listAux.add(c);
        }

        return listAux;
    }

    public List<Calculo> getFinanciamentoList(Context contexto) {
        titulos = contexto.getResources().getStringArray(R.array.financiamento_titulo);
        descricoes = contexto.getResources().getStringArray(R.array.financiamento_descricao);
        imagens = contexto.getResources().obtainTypedArray(R.array.financiamento_icons);

        for(int i = 0; i < titulos.length; i++) {

            Calculo c = new Calculo();
            c.setTitulo(titulos[i]);
            c.setDescricao(descricoes[i]);
            c.setImagem(imagens.getResourceId(i,0));

            listAux.add(c);
        }

        return listAux;
    }

    public List<Calculo> getAplicacaoList(Context contexto) {
        titulos = contexto.getResources().getStringArray(R.array.aplicacao_titulo);
        descricoes = contexto.getResources().getStringArray(R.array.aplicacao_descricao);
        imagens = contexto.getResources().obtainTypedArray(R.array.aplicacao_icons);

        for(int i = 0; i < titulos.length; i++) {

            Calculo c = new Calculo();
            c.setTitulo(titulos[i]);
            c.setDescricao(descricoes[i]);
            c.setImagem(imagens.getResourceId(i,0));

            listAux.add(c);
        }

        return listAux;
    }

    public List<Calculo> getAquisicaoList(Context contexto) {
        titulos = contexto.getResources().getStringArray(R.array.aquisicao_veiculo_titulo);
        descricoes = contexto.getResources().getStringArray(R.array.aquisicao_veiculo_descricao);
        imagens = contexto.getResources().obtainTypedArray(R.array.aquisicao_veiculo_icons);

        for(int i = 0; i < titulos.length; i++) {

            Calculo c = new Calculo();
            c.setTitulo(titulos[i]);
            c.setDescricao(descricoes[i]);
            c.setImagem(imagens.getResourceId(i,0));

            listAux.add(c);
        }

        return listAux;
    }

    public List<Calculo> getCompraList(Context contexto) {
        titulos = contexto.getResources().getStringArray(R.array.comprar_a_vista_titulo);
        descricoes = contexto.getResources().getStringArray(R.array.comprar_a_vista_descricao);
        imagens = contexto.getResources().obtainTypedArray(R.array.comprar_a_vista_icons);

        for(int i = 0; i < titulos.length; i++) {

            Calculo c = new Calculo();
            c.setTitulo(titulos[i]);
            c.setDescricao(descricoes[i]);
            c.setImagem(imagens.getResourceId(i,0));

            listAux.add(c);
        }

        return listAux;
    }

    public List<Calculo> getDinheiroList(Context contexto) {
        titulos = contexto.getResources().getStringArray(R.array.juntar_dinheiro_titulo);
        descricoes = contexto.getResources().getStringArray(R.array.juntar_dinheiro_descricao);
        imagens = contexto.getResources().obtainTypedArray(R.array.juntar_dinheiro_icons);

        for(int i = 0; i < titulos.length; i++) {

            Calculo c = new Calculo();
            c.setTitulo(titulos[i]);
            c.setDescricao(descricoes[i]);
            c.setImagem(imagens.getResourceId(i,0));

            listAux.add(c);
        }

        return listAux;
    }

    public List<Calculo> getRendaList(Context contexto) {
        titulos = contexto.getResources().getStringArray(R.array.viver_de_renda_titulo);
        descricoes = contexto.getResources().getStringArray(R.array.viver_de_renda_descricao);
        imagens = contexto.getResources().obtainTypedArray(R.array.viver_de_renda_icons);

        for(int i = 0; i < titulos.length; i++) {

            Calculo c = new Calculo();
            c.setTitulo(titulos[i]);
            c.setDescricao(descricoes[i]);
            c.setImagem(imagens.getResourceId(i,0));

            listAux.add(c);
        }

        return listAux;
    }

}
