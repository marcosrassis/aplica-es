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
import android.widget.TextView;

import com.assissoft.canif.conversor.utils.FormatacaoHelper;
import com.assissoft.canif.conversor.utils.TAGHelper;
import com.assissoft.canif.conversor.adapter.Conversor_RV_Adapter;
import com.assissoft.canif.conversor.model.DefConversor;
import com.assissoft.canif.conversor.model.ListaTodasUnidades;
import com.assissoft.canif.conexaobanco.ComandosBD;
import com.assissoft.canif.conversor.utils.FuncoesHelper;
import com.assissoft.canif.conversor.model.ListaCotacoes;
import com.assissoft.canif.R;
import com.assissoft.canif.conversor.utils.ListasHelper;
import com.assissoft.canif.conversor.utils.LogHelper;
import com.assissoft.canif.conversor.utils.TextoHelper;
import com.assissoft.canif.simcalc.model.Preferencia;

/**
 * Created by Marcos on 21/07/2016.
 *
 */
public class ConverteTodasUnidades extends Fragment implements RecyclerViewOnClickListener {

    private View view = null;
    private final Bundle args = new Bundle();
    private String siglaPriUnidade = "";
    private String[] titulos;
    private double montante = 0;
    private ConversorFragmentComunicator fc;
    private ListaCotacoes lc = null;
    private FuncoesHelper funcoesHelper;
    private RecyclerView mRecyclerView;
    private ListaTodasUnidades mList = null;
    private final ListasHelper listasHelper = new ListasHelper();
    private String[] colecao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.unidades_list_fragment,container,false);

        // call the method setHasOptionsMenu, to have access to the menu from your fragment
        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        String FRAGMENT_TAG = "ConverteTodasUnidades";
        try {
            savedInstanceState.putSerializable(ListaTodasUnidades.KEY, new ListaTodasUnidades(mList.todasUnidades));

        } catch (Exception e) {
            LogHelper.e(FRAGMENT_TAG, e, "Falhou o onSaveInstanceState para funcoesHelper.getUnidades()");
        }

        try {
            savedInstanceState.putSerializable(ListaCotacoes.KEY, new ListaCotacoes(lc.cotacoes));

        } catch (Exception e) {
            LogHelper.e(FRAGMENT_TAG, e, "Falhou o onSaveInstanceState para funcoesHelper.getCotacoes()");
        }

        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Inicializa a Interface de comunicação entre fragments
        fc = (ConversorFragmentComunicator) getActivity();

        colecao = getContext().getResources().getStringArray(R.array.titulo_conversores);
        String titulo = colecao[fc.getCONVERSOR_VIGENTE_ID()];

        //Atualiza título da Action Bar
        fc.atualizaTitulo(titulo);

        //Não exibe o botão flutante
        fc.exibeBotaoFlutuante(View.INVISIBLE);

        //Carrega a lista de Unidade no array de string
        TAGHelper tagHelper = new TAGHelper();
        titulos = getContext().getResources().getStringArray(getResources().getIdentifier(tagHelper.getNomeArray(getContext()),"array",getContext().getPackageName()));

        //Faz o link dos campos de tela com as variáveis
        TextView tv_Montante = (TextView) view.findViewById(R.id.tvMontante);
        TextView tv_Unidade = (TextView) view.findViewById(R.id.tvUnidade);

        //Prepara o banco de dados
        ComandosBD bd = new ComandosBD(getContext());

        //Obtem as preferências do usuário
        Preferencia preferencia = bd.buscarPreferencia();
        int casasdecimais = (preferencia.getId() > 0 ? preferencia.getCasasdecimais() : 2);
        boolean isChecked = (preferencia.getNotacao() != 0);

        //Captura os parâmetros fornecidos pelo DefConversor de Unidade
        if (getArguments() != null)
            if (getArguments().getString("siglaPriUnidade") != null) {
                siglaPriUnidade = getArguments().getString("siglaPriUnidade");
            }

        if (getArguments() != null)
            if (getArguments().getDouble("Montante") != 0) {
                montante = getArguments().getDouble("Montante");
            }

        //Exibe a unidade
        tv_Unidade.setText(TextoHelper.extraiSigla(siglaPriUnidade));

        //Exibe o montante formatado
        String mascara = FormatacaoHelper.numero(montante, casasdecimais);
        tv_Montante.setText(mascara);

        //Inicializa a classe de Funções de Câmbio com o contexto da atividade principal
        funcoesHelper = new FuncoesHelper(getContext());

        //Le o objeto da lista de cotações de moeda que foi serializado
        if (DefConversor.MOEDA.equals(tagHelper.getTAG(getContext()))) {
            if (getArguments() != null ) {
                lc = (ListaCotacoes) getArguments().getSerializable(ListaCotacoes.KEY);
                funcoesHelper.setCotacoes(lc != null ? lc.cotacoes : null);
            }
        }

        //Converte todas as unidades
        funcoesHelper.convertAll(TextoHelper.extraiSigla(siglaPriUnidade),montante, casasdecimais, isChecked);

        if (savedInstanceState == null ) {
            //Obtem a lista de todas as unidades do Conversor
            mList = listasHelper.getUnidadesTodas(getContext(),funcoesHelper.getUnidades());
        } else {
            mList = (ListaTodasUnidades) savedInstanceState.getSerializable(ListaTodasUnidades.KEY);

            //Obtem a lista de todas as unidades do Conversor
            lc = (ListaCotacoes) savedInstanceState.getSerializable(ListaCotacoes.KEY);
            funcoesHelper.setCotacoes(lc != null ? lc.cotacoes : null);
        }

        Conversor_RV_Adapter adapter = new Conversor_RV_Adapter(getContext(),mList.todasUnidades);
        adapter.setRecyclerViewOnClickListener(this);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(int position) {

        TAGHelper tagHelper = new TAGHelper();

        //Define qual TAG de DefConversor deverá ser acessada
        tagHelper.setTAG(getContext(), tagHelper.getTAG(getContext()));

        if (DefConversor.MOEDA.equals(tagHelper.getTAG(getContext()))) {
            //Passa os parâmetros da unidades escolhida para o DefConversor respectivo
            args.putCharSequence("selecionarMoeda","segMoedaSelecionada");
            args.putCharSequence("siglaPriMoeda",TextoHelper.extraiSigla(siglaPriUnidade));
            args.putCharSequence("siglaSegMoeda", TextoHelper.extraiSigla(titulos[position]));
            args.putCharSequence("etMontante",String.valueOf(montante));

            //Passa as cotações de moeda para o fragment de moeda
            args.putSerializable(ListaCotacoes.KEY, new ListaCotacoes(funcoesHelper.getCotacoes()));

            //Chama o fragment do conversor de moedas
            fc.chamaConversorDeMoeda(args);
        } else {
            //Passa os parâmetros da unidades escolhida para o DefConversor respectivo
            args.putCharSequence("selecionarUnidade","segUnidadeSelecionada");
            args.putCharSequence("siglaPriUnidade",TextoHelper.extraiSigla(siglaPriUnidade));
            args.putCharSequence("siglaSegUnidade", TextoHelper.extraiSigla(titulos[position]));
            args.putCharSequence("etMontante",String.valueOf(montante));

            //Chama o fragment do conversor de Unidade
            fc.chamaConversorDeUnidade(args);
        }

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

        //Inicializa a Interdace de comunição entre fragments
        fc = (ConversorFragmentComunicator) getActivity();

        colecao = getContext().getResources().getStringArray(R.array.titulo_conversores);
        String titulo = colecao[fc.getCONVERSOR_VIGENTE_ID()];

        //Atualiza título da Action Bar
        fc.atualizaTitulo(titulo);

        //Não exibe o botão flutante
        fc.exibeBotaoFlutuante(View.INVISIBLE);

    }
}



