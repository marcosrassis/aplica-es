package com.assissoft.canif;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assissoft.canif.conversor.ui.ConversorFragmentComunicator;

/**
 * Created by Marcos on 13/09/2016.
 *
 */
public class ConversorFragmentCanif extends Fragment {

    //public String FRAGMENT_TAG = "ConversorFragmentCanif";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.conversor_root_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Inicializa a Interdace de comunição entre fragments
        ConversorFragmentComunicator fc = (ConversorFragmentComunicator) getActivity();

        //Chama o fragment da Lista de Conversores
        fc.chamaConversorListFragment();

        //Não exibe o botão flutante
        fc.exibeBotaoFlutuante(View.INVISIBLE);

    }
}