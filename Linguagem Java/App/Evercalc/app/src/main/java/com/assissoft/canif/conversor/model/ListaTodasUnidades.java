package com.assissoft.canif.conversor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcos on 09/12/2016.
 *
 */
public class ListaTodasUnidades implements Serializable {

    //Lista para conter as cotações de câmbio
    public List<Conversor> todasUnidades = new ArrayList<>();
    public static final String KEY = "TodasUnidades";

    public ListaTodasUnidades(List<Conversor> todasUnidades){
        this.todasUnidades = todasUnidades;
    }
}
