package com.assissoft.canif.conversor.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.assissoft.canif.R;
import com.assissoft.canif.conversor.adapter.Conversor_RV_Adapter;
import com.assissoft.canif.conversor.model.Conversor;
import com.assissoft.canif.conversor.utils.ListasHelper;
import com.assissoft.canif.conversor.model.ListaCotacoes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcos on 16/05/2016.
 *
 */
public class MoedaListFragment extends Fragment implements RecyclerViewOnClickListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{

    private final Bundle args = new Bundle();
    private String selecionarMoeda = "";
    private String siglaPriMoeda = "";
    private String siglaSegMoeda = "";
    private ConversorFragmentComunicator fc;
    private ListaCotacoes lc;
    private String montante = "";
    private final ListasHelper listasHelper = new ListasHelper();
    private List<Conversor> mList;
    private RecyclerView mRecyclerView;
    private Conversor_RV_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.conversor_list_fragment,container,false);

        // call the method setHasOptionsMenu, to have access to the menu from your fragment
        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Inicializa a Interface de comunicação entre fragments
        fc = (ConversorFragmentComunicator) getActivity();

        //Atualiza título da Action Bar
        fc.atualizaTitulo(getString(R.string.msg_selecao_moeda));

        //Não exibe o botão flutante
        fc.exibeBotaoFlutuante(View.INVISIBLE);

        //Exibe o menu de conversão de todas as unidades
        fc.exibeOuNaoMenuConversaoTodas(false);

        //Captura os parâmetros fornecidos pelo DefConversor de Moeda
        if (getArguments() != null)
            if (getArguments().getString("selecionarMoeda") != null) {
                selecionarMoeda = getArguments().getString("selecionarMoeda");
            }

        if (getArguments() != null)
            if (getArguments().getString("siglaPriMoeda") != null) {
                siglaPriMoeda = getArguments().getString("siglaPriMoeda");
            }

        if (getArguments() != null)
            if (getArguments().getString("siglaSegMoeda") != null) {
                siglaSegMoeda = getArguments().getString("siglaSegMoeda");
            }

        if (getArguments() != null)
            if (getArguments().getString("etMontante") != null) {
                montante = getArguments().getString("etMontante");
            }

        //Carrega a lista completa
        resetSearch();

        //Le o objeto da lista de cotações de moeda que foi serializado
        lc = (ListaCotacoes) getArguments().getSerializable(ListaCotacoes.KEY);

    }

    @Override
    public void onItemClick(int position) {
        //Passa como parâmetro para o DefConversor de Moeda qual o campo de moeda foi ativado
        args.putCharSequence("selecionarMoeda",selecionarMoeda);

        //Passa como parâmetro para o DefConversor de Moeda a sigla da primeira moeda selecionada
        if (selecionarMoeda.equals("priMoedaSelecionada")){
            args.putCharSequence("siglaPriMoeda", adapter.getList().get(position).getDescricao());
            args.putCharSequence("siglaSegMoeda",siglaSegMoeda);
        }

        //Passa como parâmetro para o DefConversor de Moeda a sigla da segunda moeda selecionada
        if (selecionarMoeda.equals("segMoedaSelecionada")){
            args.putCharSequence("siglaSegMoeda", adapter.getList().get(position).getDescricao());
            args.putCharSequence("siglaPriMoeda",siglaPriMoeda);
        }

        //Passa como parâmetro o montante
        args.putCharSequence("etMontante",montante);

        //Passa as cotações de moeda para o fragment de moeda
        args.putSerializable(ListaCotacoes.KEY, new ListaCotacoes(lc.cotacoes));

        //Chama o fragment do conversor de moedas
        fc.chamaConversorDeMoeda(args);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Inicializa a Interdace de comunição entre fragments
        fc = (ConversorFragmentComunicator) getActivity();

        //Atualiza título da Action Bar
        fc.atualizaTitulo(getString(R.string.msg_selecao_moeda));

        //Não exibe o botão flutante
        fc.exibeBotaoFlutuante(View.INVISIBLE);

        //Exibe o menu de conversão de todas as unidades
        fc.exibeOuNaoMenuConversaoTodas(false);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_search).setVisible(false);

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.action_search));

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }

        newText = newText.toLowerCase();
        List<Conversor> newList = new ArrayList<>();
        for(Conversor conversor : mList) {

            String titulo = conversor.getTitulo().toLowerCase();

            if (titulo.contains(newText))
                newList.add(conversor);

        }

        adapter.setFilter(newList);
        mRecyclerView.setAdapter(adapter);

        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return false;
    }

    private void resetSearch(){
        //Obtem a lista das Moedas
        mList = listasHelper.getMoedaList(getActivity());

        adapter = new Conversor_RV_Adapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListener(this);
        mRecyclerView.setAdapter(adapter);

    }
}

