package com.assissoft.canif.simcalc.ui.aquisicao_de_imovel;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.assissoft.canif.R;
import com.assissoft.canif.conversor.utils.ConverteUnidadeHelper;
import com.assissoft.canif.conversor.utils.FormatacaoHelper;
import com.assissoft.canif.simcalc.ui.SimcalcFragmentComunicator;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.assissoft.canif.simcalc.utils.FuncoesHelper.calculaFinanciamento_1;

/**
 * Created by Marcos on 10/10/2016.
 *
 */
public class Financiamento_1Fragment extends Fragment {

    private SimcalcFragmentComunicator fs;
    private View view;
    private EditText valor_financiado;
    private EditText taxa_de_juros;
    private EditText periodo;
    private EditText valor_da_parcela;
    private String[] colecao;
    private EditText primeiraParcemEm;
    private EditText valorTotalPagar;
    private EditText taxaAnualJuros;
    private EditText valorGastoComJuros;
    private EditText prazoPagto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.simcalc_financiamento, container, false);

        // call the method setHasOptionsMenu, to have access to the menu from your fragment
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fs = (SimcalcFragmentComunicator) getActivity();

        colecao = getActivity().getResources().getStringArray(R.array.calculos_titulo);
        String titulo = colecao[fs.getSIMCALC_VIGENTE_ID()];

        //Atualiza título da Action Bar
        fs.atualizaTitulo(titulo);

        //Não Exibe o botão flutante
        fs.ocultaBotaoFlutuanteSimcalc();

        //Faz o link das views de texto com as variáveis
        valor_financiado = (EditText) view.findViewById(R.id.et_valor_financiado);
        taxa_de_juros = (EditText) view.findViewById(R.id.et_taxa_de_juros);
        periodo = (EditText) view.findViewById(R.id.et_periodo);
        valor_da_parcela = (EditText) view.findViewById(R.id.et_valor_da_parcela);

        //Faz o link das views de texto com as variáveis complementares
        prazoPagto = (EditText) view.findViewById(R.id.tv_prazo_pgto_primeira_parcela);
        primeiraParcemEm = (EditText) view.findViewById(R.id.tv_data_primeira_parcela_em);
        valorTotalPagar = (EditText) view.findViewById(R.id.tv_valor_total_a_pagar_informado);
        taxaAnualJuros = (EditText) view.findViewById(R.id.tv_tx_anual_juros_informado);
        valorGastoComJuros = (EditText) view.findViewById(R.id.tv_valor_gasto_de_juros_informado);

        taxa_de_juros.addTextChangedListener(textWatcher);
        periodo.addTextChangedListener(textWatcher);
        valor_da_parcela.addTextChangedListener(textWatcher);

        //Campos não editáveis
        valor_financiado.setEnabled(false);

    }

    private final TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //after text changed

            String _valor_parcela = "";
            String _juros = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                _juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                _valor_parcela = valor_da_parcela.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                _juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                _valor_parcela = valor_da_parcela.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            calculaFinanciamento_1(_valor_parcela,_juros,periodo.getText().toString(), valor_financiado, valorTotalPagar, valorGastoComJuros);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            primeiraParcemEm.setText(getPrazoPrimeiraParcela());
            taxaAnualJuros.setText(getTaxaAnualJuros());
            prazoPagto.setText(getString(R.string.lbl_prazo_pgto));
        }
    };

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

    private String getPrazoPrimeiraParcela() {
        DecimalFormat formato;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, +30);

        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH);
        int ano = c.get(Calendar.YEAR);

        formato = new DecimalFormat("00");
        return formato.format(dia) + "/" + formato.format(mes+1) + "/" + String.valueOf(ano);
    }

    private String getTaxaAnualJuros(){
        double resultado = 0;

        String _juros = "";

        if (Locale.getDefault().getLanguage().equals("pt")){
            _juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
        } else {
            _juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
        }

        if (taxa_de_juros.getText().toString().length() !=0) {

            if (Locale.getDefault().getLanguage().equals("pt"))
                resultado = ConverteUnidadeHelper.calculaEquivTaxas("mês-ano",_juros);
            else
                resultado = ConverteUnidadeHelper.calculaEquivTaxas("month-year",_juros);

        }

        return FormatacaoHelper.numero(resultado,3) + "%";

    }

}