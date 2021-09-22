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
import com.assissoft.canif.R;
import com.assissoft.canif.conversor.utils.TAGHelper;
import com.assissoft.canif.conversor.model.DefConversor;
import com.assissoft.canif.conexaobanco.ComandosBD;
import com.assissoft.canif.conversor.utils.ConverteUnidadeHelper;
import com.assissoft.canif.conversor.utils.FormatacaoHelper;
import com.assissoft.canif.conversor.utils.TextoHelper;
import com.assissoft.canif.simcalc.model.Preferencia;

import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 * Created by Marcos on 21/07/2016.
 */
public class UnidadeFragment extends Fragment {

    private View view = null;
    private EditText etMontante;
    private TextView tvResultado;
    private String selecionarUnidade = "";
    private String siglaPriUnidade = "";
    private String siglaSegUnidade = "";
    private final Bundle args = new Bundle();
    private ConversorFragmentComunicator fc;
    private String montante = "";
    private int casasdecimais = 2; //2-Default
    private boolean isChecked = false;
    private String[] colecao;

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

        //Abre o bando de dados
        ComandosBD bd = new ComandosBD(getContext());

        //Obtem as preferências do usuário
        Preferencia preferencia = bd.buscarPreferencia();

        //Lê do banco o número de casas decimais
        casasdecimais = (preferencia.getId() > 0 ? preferencia.getCasasdecimais() : 2);

        //Lê do banco a notação científica
        isChecked = (preferencia.getNotacao() != 0);

        TAGHelper tagHelper = new TAGHelper();

        colecao = getActivity().getResources().getStringArray(R.array.titulo_conversores);
        String titulo = colecao[fc.getCONVERSOR_VIGENTE_ID()];

        //Atualiza título da Action Bar
        fc.atualizaTitulo(titulo);

        //Exibe o botão flutante
        fc.exibeBotaoFlutuante(View.VISIBLE);

        //Carrega a lista de Unidade no array de string
        String[] titulos = getActivity().getResources().getStringArray(getResources().getIdentifier(tagHelper.getNomeArray(getActivity()), "array", getActivity().getPackageName()));

        //Faz o link das views de texto com as variáveis
        TextView tvDe = (TextView) view.findViewById(R.id.tvDe);
        TextView tvPara = (TextView) view.findViewById(R.id.tvPara);
        etMontante = (EditText) view.findViewById(R.id.etMontante);
        tvResultado = (TextView) view.findViewById(R.id.tvResultado);

        //Faz o link das imagens de seleção de Unidade com as variáveis do tipo imagem
        ViewGroup UnidadeSel1 = (ViewGroup) view.findViewById(R.id.ll_De);
        ViewGroup UnidadeSel2 = (ViewGroup) view.findViewById(R.id.ll_Para);

        //Verifica os parâmetros fornecidos pelo fragment de seleção de Unidade e carrega nas variáveis
        if (getArguments() != null)
            if ("priUnidadeSelecionada".equals(getArguments().getString("selecionarUnidade"))) {
                siglaPriUnidade = getArguments().getString("siglaPriUnidade");
                siglaSegUnidade = getArguments().getString("siglaSegUnidade");
            }
        if (getArguments() != null)
            if ("segUnidadeSelecionada".equals(getArguments().getString("selecionarUnidade"))) {
                siglaSegUnidade = getArguments().getString("siglaSegUnidade");
                siglaPriUnidade = getArguments().getString("siglaPriUnidade");
            }

        if (getArguments() != null) {
            montante = getArguments().getString("etMontante"); //Adicionado .toString() em 06/12/16 para tentar eliminar o error getSlotFromBufferLocked
            etMontante.setText(montante);
        }

        //A primeira vez que entrar nessa tela pegará a primeira Unidade da lista
        if ("".equals(siglaPriUnidade) || "".equals(siglaSegUnidade)){
            siglaPriUnidade = TextoHelper.extraiSigla(titulos[0]);
            siglaSegUnidade = TextoHelper.extraiSigla(titulos[0]);
        }

        //Obtem o nome completo da unidade com a sigla
        String nomePriUnidade = TextoHelper.extraiNome(getActivity(),siglaPriUnidade);
        String nomeSegUnidade = TextoHelper.extraiNome(getActivity(),siglaSegUnidade);

        //Atualiza os textos dos labels de Unidade
        tvDe.setText(nomePriUnidade);
        tvPara.setText(nomeSegUnidade);

        String _montante = "";

        if (Locale.getDefault().getLanguage().equals("pt")){
            _montante = etMontante.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
        } else {
            _montante = etMontante.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
        }

        //Variável para o cálculo da conversão
        double resultado;

        if (DefConversor.TAXAS.equals(tagHelper.getTAG(getActivity())))
            resultado = ConverteUnidadeHelper.calculaEquivTaxas(siglaPriUnidade +"-"+ siglaSegUnidade,_montante);
        else
            if (DefConversor.TEMPERATURA.equals(tagHelper.getTAG(getActivity())))
                resultado = ConverteUnidadeHelper.calculaTemperatura(siglaPriUnidade +"-"+ siglaSegUnidade,_montante);
            else
                resultado = ConverteUnidadeHelper.calcula(tagHelper.getTAG(getActivity()),siglaPriUnidade +"-"+ siglaSegUnidade,_montante);

        if (isChecked)
            tvResultado.setText(FormatacaoHelper.nc(resultado,casasdecimais));
        else {
            String texto = FormatacaoHelper.numero(resultado, casasdecimais) + (DefConversor.TAXAS.equals(tagHelper.getTAG(getActivity())) ? " %" : "");
            tvResultado.setText(texto);
        }

        //Chama a conversão de Unidade enquanto digita o montante a ser convertido
        etMontante.addTextChangedListener(textWatcher);

        //Handle para exibir a tela da seleção de Unidade
        UnidadeSel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                selecionarUnidade = "priUnidadeSelecionada";
                montante = etMontante.getText().toString();
                chamaListaDeUnidade();
            }
        });

        //Handle para exibir a tela da seleção de Unidade
        UnidadeSel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                selecionarUnidade = "segUnidadeSelecionada";
                montante = etMontante.getText().toString();
                chamaListaDeUnidade();
            }
        });

    }

    private final TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Faz a conversão da Unidade e mostra na tela
            double resultado;

            TAGHelper tagHelper = new TAGHelper();

            String _montante = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                _montante = etMontante.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                _montante = etMontante.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            if (DefConversor.TAXAS.equals(tagHelper.getTAG(getActivity())))
                resultado = ConverteUnidadeHelper.calculaEquivTaxas(siglaPriUnidade +"-"+ siglaSegUnidade,_montante);
            else
                if (DefConversor.TEMPERATURA.equals(tagHelper.getTAG(getActivity())))
                    resultado = ConverteUnidadeHelper.calculaTemperatura(siglaPriUnidade +"-"+ siglaSegUnidade,_montante);
                else
                    resultado = ConverteUnidadeHelper.calcula(tagHelper.getTAG(getActivity()),siglaPriUnidade +"-"+ siglaSegUnidade,_montante);

            if (isChecked)
                tvResultado.setText(FormatacaoHelper.nc(resultado,casasdecimais));
            else {
                String texto = FormatacaoHelper.numero(resultado, casasdecimais) + (DefConversor.TAXAS.equals(tagHelper.getTAG(getActivity())) ? " %" : "");
                tvResultado.setText(texto);
            }

            //Exibe o menu de conversão de todas as unidades
            fc.exibeOuNaoMenuConversaoTodas(true);

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(etMontante.getText().toString().equals("") || etMontante.getText().toString().equals("."))
                fc.exibeOuNaoMenuConversaoTodas(false);
            else
                fc.exibeOuNaoMenuConversaoTodas(true);
        }
    };

    private void chamaListaDeUnidade(){
        //Passa os parâmetros para o fragment da seleção de Unidade
        args.putCharSequence("selecionarUnidade",selecionarUnidade);
        args.putCharSequence("siglaPriUnidade",siglaPriUnidade);
        args.putCharSequence("siglaSegUnidade",siglaSegUnidade);
        args.putCharSequence("etMontante",montante);

        TAGHelper tagHelper = new TAGHelper();

        //Define uma TAG específica para acessar uma lista de unidades de medida
        tagHelper.setTAGLista(getActivity());

        //Chama o fragment da lista de unidade
        fc.chamaListaDeUnidade(args);
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

        colecao = getActivity().getResources().getStringArray(R.array.titulo_conversores);
        String titulo = colecao[fc.getCONVERSOR_VIGENTE_ID()];

        //Atualiza título da Action Bar
        fc.atualizaTitulo(titulo);

        //Exibe o botão flutante
        fc.exibeBotaoFlutuante(View.VISIBLE);
    }

}

