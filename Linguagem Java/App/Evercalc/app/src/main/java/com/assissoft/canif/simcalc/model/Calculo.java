package com.assissoft.canif.simcalc.model;

/**
 * Created by Marcos on 01/12/2016.
 *
 */
public class Calculo {

    private String titulo;
    private String descricao;
    private int imagem;

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public String getTitulo() {
        return this.titulo;
    }
    public String getDescricao() {
        return this.descricao;
    }
    public int getImagem() { return this.imagem; }

}
