package com.assissoft.canif.conversor.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Marcos on 23/05/2016.
 *
 */
public class ListaCotacoes implements Serializable{

    //Lista para conter as cotações de câmbio
    public ArrayList<Cambio> cotacoes = new ArrayList<>();
    public static final String KEY = "cotacoes";

    public ListaCotacoes(ArrayList<Cambio> cotacoes){
        this.cotacoes = cotacoes;
    }
}
