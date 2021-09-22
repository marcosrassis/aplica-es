package com.assissoft.canif.simcalc.ui.comprar_a_vista;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import com.assissoft.canif.R;
import com.assissoft.canif.simcalc.adapter.Simcalc_RV_Adapter;
import com.assissoft.canif.simcalc.model.Calculo;
import com.assissoft.canif.simcalc.ui.RecyclerViewOnClickListener;
import com.assissoft.canif.simcalc.ui.SimcalcFragmentComunicator;
import com.assissoft.canif.simcalc.utils.ListasHelper;
import java.util.List;

/**
 * Created by Marcos on 03/10/2016.
 *
 */
public class CompraFragment extends Fragment implements RecyclerViewOnClickListener {

    private SimcalcFragmentComunicator fs;
    private final ListasHelper listasHelper = new ListasHelper();
    private String[] colecao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.simcalc_list_fragment,container,false);

        // call the method setHasOptionsMenu, to have access to the menu from your fragment
        setHasOptionsMenu(true);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        //Obtem a lista do menu Compra a Vista do Simcalc
        List<Calculo> mList = listasHelper.getCompraList(getActivity());

        Simcalc_RV_Adapter adapter = new Simcalc_RV_Adapter(getActivity(),mList);
        adapter.setRecyclerViewOnClickListener(this);
        mRecyclerView.setAdapter(adapter);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fs = (SimcalcFragmentComunicator) getActivity();

        colecao = getActivity().getResources().getStringArray(R.array.calculos_titulo);
        String titulo = colecao[fs.getSIMCALC_VIGENTE_ID()];

        //Atualiza título da Action Bar
        fs.atualizaTitulo(titulo);

        //Não Exibe o botão flutante
        fs.ocultaBotaoFlutuanteSimcalc();

    }

    @Override
    public void onItemClick(int position) {
        fs.chamaCalculoCompraSelecionado(position);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_search).setVisible(false);
        if (menu.findItem(R.id.search_menu) != null && menu.findItem(R.id.search_menu).isVisible()) menu.removeItem(R.id.search_menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        fs = (SimcalcFragmentComunicator) getActivity();

        colecao = getActivity().getResources().getStringArray(R.array.calculos_titulo);
        String titulo = colecao[fs.getSIMCALC_VIGENTE_ID()];

        //Atualiza título da Action Bar
        fs.atualizaTitulo(titulo);

        //Exibe o botão flutante
        fs.ocultaBotaoFlutuanteSimcalc();

    }
}
