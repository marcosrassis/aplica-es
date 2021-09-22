package com.assissoft.canif.conversor.ui;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.assissoft.canif.R;
import com.assissoft.canif.conexaobanco.ComandosBD;
import com.assissoft.canif.conversor.model.DefConversor;
import com.assissoft.canif.conversor.utils.FuncoesHelper;
import com.assissoft.canif.conversor.utils.TextoHelper;
import com.assissoft.canif.conversor.model.ListaCotacoes;
import com.assissoft.canif.conversor.utils.FormatacaoHelper;
import com.assissoft.canif.simcalc.model.Preferencia;

import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 *
 */
public class MoedaFragment extends Fragment {

    private View view = null;
    private EditText etMontante;
    private TextView tvResultado;
    private String selecionarMoeda = "";
    private String siglaPriMoeda = "";
    private String siglaSegMoeda = "";
    private final Bundle args = new Bundle();
    private ConversorFragmentComunicator fc;
    private FuncoesHelper funcoesHelper;
    private ListaCotacoes lc = null;
    private String montante = "";
    private int casasdecimais = 2; //2-Default

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.conversor_fragment, container, false);

        // call the method setHasOptionsMenu, to have access to the menu from your fragment
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Inicializa a Interdace de comunição entre fragments
        fc = (ConversorFragmentComunicator) getActivity();

        //Atualiza título da Action Bar
        fc.atualizaTitulo(DefConversor.MOEDA);

        //Exibe o botão flutante
        fc.exibeBotaoFlutuante(View.VISIBLE);

        //Atribui o contexto da atividade do fragment
        funcoesHelper = new FuncoesHelper(getActivity());

        //Abre o bando de dados
        ComandosBD bd = new ComandosBD(getContext());

        //Obtem as preferências do usuário
        Preferencia preferencia = bd.buscarPreferencia();

        //Lê do banco o número de casas decimais
        casasdecimais = (preferencia.getId() > 0 ? preferencia.getCasasdecimais() : 2);

        //Carrega a lista de moedas no array de string
        String[] titulos = getActivity().getResources().getStringArray(R.array.moedas);

        //Faz o link das views de texto com as variáveis
        TextView tvDe = (TextView) view.findViewById(R.id.tvDe);
        TextView tvPara = (TextView) view.findViewById(R.id.tvPara);
        etMontante = (EditText) view.findViewById(R.id.etMontante);
        tvResultado = (TextView) view.findViewById(R.id.tvResultado);

        //Faz o link das imagens de seleção de moeda com as variáveis do tipo imagem
        ViewGroup moedaSel1 = (ViewGroup) view.findViewById(R.id.ll_De);
        ViewGroup moedaSel2 = (ViewGroup) view.findViewById(R.id.ll_Para);

        //Verifica os parâmetros fornecidos pelo fragment de seleção de moeda e carrega nas variáveis
        if (getArguments() != null)
            if ("priMoedaSelecionada".equals(getArguments().getString("selecionarMoeda"))) {
                siglaPriMoeda = getArguments().getString("siglaPriMoeda");
                siglaSegMoeda = getArguments().getString("siglaSegMoeda");
            }
        if (getArguments() != null)
            if ("segMoedaSelecionada".equals(getArguments().getString("selecionarMoeda"))) {
                siglaSegMoeda = getArguments().getString("siglaSegMoeda");
                siglaPriMoeda = getArguments().getString("siglaPriMoeda");
            }

        if (getArguments() != null) {
            montante = getArguments().getString("etMontante"); //Adicionado .toString() em 06/12/16 para tentar eliminar o error getSlotFromBufferLocked
            etMontante.setText(montante);
        }

        //Le o objeto da lista de cotações de moeda que foi serializado
        if (getArguments() != null) {
            lc = (ListaCotacoes) getArguments().getSerializable(ListaCotacoes.KEY);
        }

        //Carrega a lista de cotações em memória se já houve um dowload anterior
        if (lc != null && !(lc.cotacoes.isEmpty()))
            funcoesHelper.setCotacoes(lc.cotacoes);
        else
            Toast.makeText(getActivity(), getString(R.string.msg_conexao_servidor), Toast.LENGTH_LONG).show();

        //A primeira vez que entrar nessa tela pegará a primeira moeda da lista
        if (siglaPriMoeda.equals("") || siglaSegMoeda.equals("")){
            siglaPriMoeda = TextoHelper.extraiSiglaMoeda(titulos[0]);
            siglaSegMoeda = TextoHelper.extraiSiglaMoeda(titulos[0]);
        }

        //Obtem o nome completo da unidade com a sigla
        String nomePriMoeda = TextoHelper.extraiNome(getActivity(),siglaPriMoeda);
        String nomeSegMoeda = TextoHelper.extraiNome(getActivity(),siglaSegMoeda);

        //Atualiza os textos dos labels de moeda
        tvDe.setText(nomePriMoeda);
        tvPara.setText(nomeSegMoeda);

        String _montante = "";

        if (Locale.getDefault().getLanguage().equals("pt")){
            _montante = etMontante.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
        } else {
            _montante = etMontante.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
        }

        //Faz a conversão da moeda e mostra na tela
        if (!lc.cotacoes.isEmpty() && lc.cotacoes != null)
            tvResultado.setText(FormatacaoHelper.moeda(funcoesHelper.resultadoConversaoMoeda(siglaPriMoeda,siglaSegMoeda,_montante),casasdecimais));

        //Chama a conversão de moeda enquanto digita o montante a ser convertido
        etMontante.addTextChangedListener(textWatcher);

        //Handle para exibir a tela da seleção de moeda
        moedaSel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                selecionarMoeda = "priMoedaSelecionada";
                montante = etMontante.getText().toString();
                chamaListaDeMoeda();
            }
        });

        //Handle para exibir a tela da seleção de moeda
        moedaSel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                selecionarMoeda = "segMoedaSelecionada";
                montante = etMontante.getText().toString();
                chamaListaDeMoeda();
            }
        });

    }

    private final TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //after text changed

            String _montante = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                _montante = etMontante.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                _montante = etMontante.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            //Faz a conversão da moeda e mostra na tela
            if (!lc.cotacoes.isEmpty() && lc.cotacoes != null)
                tvResultado.setText(FormatacaoHelper.moeda(funcoesHelper.resultadoConversaoMoeda(siglaPriMoeda,siglaSegMoeda,_montante),casasdecimais));

            //Exibe o menu de conversão de todas as unidades
            fc.exibeOuNaoMenuConversaoTodas(true);

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
             if(etMontante.getText().toString().equals(""))
                 fc.exibeOuNaoMenuConversaoTodas(false);
             else
                 fc.exibeOuNaoMenuConversaoTodas(true);
        }
    };

    private void chamaListaDeMoeda(){
        //Passa os parâmetros para o fragment da seleção de moeda
        args.putCharSequence("selecionarMoeda",selecionarMoeda);
        args.putCharSequence("siglaPriMoeda",siglaPriMoeda);
        args.putCharSequence("siglaSegMoeda",siglaSegMoeda);
        args.putCharSequence("etMontante",montante);

        //Passa as cotações de moeda para o fragment de moeda
        args.putSerializable(ListaCotacoes.KEY, new ListaCotacoes(funcoesHelper.getCotacoes()));

        //Chama o fragment da lista de moedas
        fc.chamaListaDeMoeda(args);
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

        //Atualiza título da Action Bar
        fc.atualizaTitulo(DefConversor.MOEDA);

        //Exibe o botão flutante
        fc.exibeBotaoFlutuante(View.VISIBLE);
    }
}
