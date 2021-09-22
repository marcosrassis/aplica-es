package com.assissoft.canif;

import android.content.Context;
import android.content.res.TypedArray;
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
import com.assissoft.canif.conversor.adapter.Conversor_RV_Adapter;
import com.assissoft.canif.conversor.model.Conversor;
import com.assissoft.canif.conversor.ui.ConversorFragmentComunicator;
import com.assissoft.canif.conversor.ui.RecyclerViewOnClickListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcos on 23/02/2017.
 *
 */
public class BuscaGeralCanif  extends Fragment implements RecyclerViewOnClickListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private Conversor_RV_Adapter adapter;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<Conversor> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lista_para_busca_fragment, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Inicializa a Interface de comunicação entre fragments
        ConversorFragmentComunicator fc = (ConversorFragmentComunicator) mContext;

        //Atualiza título da Action Bar
        fc.atualizaTitulo(getString(R.string.action_search));

        //Não exibe o botão flutante
        fc.exibeBotaoFlutuante(View.INVISIBLE);

        //Carrega a lista completa
        resetSearch();

    }

    @Override
    public void onItemClick(int position) {
        //Inicializa a Interface de comunicação entre fragments
        GeralFragmentComunicator fg = (GeralFragmentComunicator) mContext;

        getFragmentManager().popBackStack();
        String tag = adapter.getList().get(position).getNomeApp();
        fg.chamaAppSelecionado(tag);

    }

    private ArrayList<Conversor> getSearchableList() {
        ArrayList<Conversor> listAux = new ArrayList<>();
        String[] titulos;
        String[] descricoes;
        String[] nomeApp;
        TypedArray imagens;

        titulos = this.getResources().getStringArray(R.array.titulo_busca_geral);
        descricoes = this.getResources().getStringArray(R.array.descricao_busca_geral);
        imagens = this.getResources().obtainTypedArray(R.array.icons_busca_geral);

        nomeApp = this.getResources().getStringArray(R.array.app_busca_geral);

        for(int i = 0; i < titulos.length; i++) {

            Conversor c = new Conversor();
            c.setTitulo(titulos[i]);
            c.setDescricao(descricoes[i]);
            c.setImagem(imagens.getResourceId(i,0));
            c.setNomeApp(nomeApp[i]);

            listAux.add(c);
        }

        imagens.recycle();
        return listAux;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_search).setVisible(false);
        if (menu.findItem(R.id.search_menu) != null && menu.findItem(R.id.search_menu).isVisible()) menu.removeItem(R.id.search_menu);

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.action_search));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
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
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return true;
    }

    private void resetSearch() {
        //Obtem a lista de todos as ferramentas
        mList = getSearchableList();
        adapter = new Conversor_RV_Adapter(mContext, mList);
        adapter.setRecyclerViewOnClickListener(this);
        mRecyclerView.setAdapter(adapter);
    }

}
