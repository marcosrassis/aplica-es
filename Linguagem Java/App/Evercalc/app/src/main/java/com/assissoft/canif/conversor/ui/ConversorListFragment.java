package com.assissoft.canif.conversor.ui;

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
import com.assissoft.canif.conversor.adapter.Conversor_RV_Adapter;
import com.assissoft.canif.conversor.model.Conversor;
import com.assissoft.canif.R;
import com.assissoft.canif.conversor.utils.ListasHelper;
import java.util.List;

/**
 * Created by Marcos on 03/05/2016.
 *
 */
public class ConversorListFragment extends Fragment implements RecyclerViewOnClickListener {

    private ConversorFragmentComunicator fc;
    private final ListasHelper listasHelper = new ListasHelper();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.conversor_list_fragment,container,false);

        // call the method setHasOptionsMenu, to have access to the menu from your fragment
        setHasOptionsMenu(true);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        //Obtem a lista dos Conversores
        List<Conversor> mList = listasHelper.getConversorList(getActivity());

        Conversor_RV_Adapter adapter = new Conversor_RV_Adapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListener(this);
        mRecyclerView.setAdapter(adapter);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fc = (ConversorFragmentComunicator) getActivity();

        //Atualiza título da Action Bar
        fc.exibeBotaoHome(getString(R.string.app_name));

        //Exibe o botão flutante
        fc.exibeBotaoFlutuante(View.INVISIBLE);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_search).setVisible(true);
        if (menu.findItem(R.id.search_menu) != null && menu.findItem(R.id.search_menu).isVisible()) menu.removeItem(R.id.search_menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemClick(int position) {
        fc.chamaConversorSelecionado(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Inicializa a Interdace de comunição entre fragments
        fc = (ConversorFragmentComunicator) getActivity();

        //Atualiza título da Action Bar
        fc.exibeBotaoHome(getString(R.string.app_name));

        //Exibe o botão flutante
        fc.exibeBotaoFlutuante(View.INVISIBLE);

    }

}
