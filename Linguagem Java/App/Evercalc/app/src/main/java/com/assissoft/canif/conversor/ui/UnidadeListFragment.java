package com.assissoft.canif.conversor.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import com.assissoft.canif.R;
import com.assissoft.canif.conversor.utils.TAGHelper;
import com.assissoft.canif.conversor.adapter.Conversor_RV_Adapter;
import com.assissoft.canif.conversor.model.Conversor;
import com.assissoft.canif.conversor.utils.ListasHelper;
import com.assissoft.canif.conversor.utils.TextoHelper;
import java.util.List;

/**
 * Created by Marcos on 21/07/2016.
 *
 */
public class UnidadeListFragment extends Fragment implements RecyclerViewOnClickListener{

    private String[] titulos;
    private final Bundle args = new Bundle();
    private String selecionarUnidade = "";
    private String siglaPriUnidade = "";
    private String siglaSegUnidade = "";
    private ConversorFragmentComunicator fc;
    private String montante = "";
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

        //Obtem a lista das Unidades
        List<Conversor> mList = listasHelper.getUnidadeList(getActivity());

        Conversor_RV_Adapter adapter = new Conversor_RV_Adapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListener(this);
        mRecyclerView.setAdapter(adapter);

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Inicializa a Interface de comunicação entre fragments
        fc = (ConversorFragmentComunicator) getActivity();

        //Atualiza título da Action Bar
        fc.atualizaTitulo(getString(R.string.msg_selecao_unidade));

        //Não exibe o botão flutante
        fc.exibeBotaoFlutuante(View.INVISIBLE);

        //Exibe o menu de conversão de todas as unidades
        fc.exibeOuNaoMenuConversaoTodas(false);

        TAGHelper tagHelper = new TAGHelper();

        //Carrega a lista de Unidade no array de string
        titulos = getActivity().getResources().getStringArray(getResources().getIdentifier(tagHelper.getNomeArray(getActivity()),"array",getActivity().getPackageName()));

        //Captura os parâmetros fornecidos pelo DefConversor de Unidade
        if (getArguments() != null)
            if (getArguments().getString("selecionarUnidade") != null) {
                selecionarUnidade = getArguments().getString("selecionarUnidade");
            }

        if (getArguments() != null)
            if (getArguments().getString("siglaPriUnidade") != null) {
                siglaPriUnidade = getArguments().getString("siglaPriUnidade");
            }

        if (getArguments() != null)
            if (getArguments().getString("siglaSegUnidade") != null) {
                siglaSegUnidade = getArguments().getString("siglaSegUnidade");
            }

        if (getArguments() != null)
            if (getArguments().getString("etMontante") != null) {
                montante = getArguments().getString("etMontante");
            }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_search).setVisible(false);
        if (menu.findItem(R.id.search_menu) != null && menu.findItem(R.id.search_menu).isVisible()) menu.removeItem(R.id.search_menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemClick(int position) {
        //Passa como parâmetro para o DefConversor de Unidade qual o campo de Unidade foi ativado
        args.putCharSequence("selecionarUnidade",selecionarUnidade);

        //Passa como parâmetro para o DefConversor de Unidade a sigla da primeira Unidade selecionada
        if (selecionarUnidade.equals("priUnidadeSelecionada")){
            args.putCharSequence("siglaPriUnidade", TextoHelper.extraiSigla(titulos[position]));
            args.putCharSequence("siglaSegUnidade",siglaSegUnidade);
        }

        //Passa como parâmetro para o DefConversor de Unidade a sigla da segunda Unidade selecionada
        if (selecionarUnidade.equals("segUnidadeSelecionada")){
            args.putCharSequence("siglaSegUnidade", TextoHelper.extraiSigla(titulos[position]));
            args.putCharSequence("siglaPriUnidade",siglaPriUnidade);
        }

        //Passa como parâmetro o montante
        args.putCharSequence("etMontante",montante);

        TAGHelper tagHelper = new TAGHelper();

        //Define qual TAG de DefConversor deverá ser acessada
        tagHelper.setTAG(getActivity(), tagHelper.getTAG(getActivity()));

        //Chama o fragment do conversor de Unidade
        fc.chamaConversorDeUnidade(args);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Inicializa a Interdace de comunição entre fragments
        fc = (ConversorFragmentComunicator) getActivity();

        //Atualiza título da Action Bar
        fc.atualizaTitulo(getString(R.string.msg_selecao_unidade));

        //Não exibe o botão flutante
        fc.exibeBotaoFlutuante(View.INVISIBLE);

        //Exibe o menu de conversão de todas as unidades
        fc.exibeOuNaoMenuConversaoTodas(false);

    }
}


