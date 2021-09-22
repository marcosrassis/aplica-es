package com.assissoft.canif;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assissoft.canif.simcalc.ui.SimcalcFragmentComunicator;

/**
 * Created by Marcos on 13/09/2016.
 *
 */
public class SimcalcFragmentCanif extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simcalc_root_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Inicializa a Interdace de comunição entre fragments
        SimcalcFragmentComunicator fs = (SimcalcFragmentComunicator) getActivity();

        //Chama o fragment do calculo selecionado
        fs.chamaSimcalcListFragment();

    }


}