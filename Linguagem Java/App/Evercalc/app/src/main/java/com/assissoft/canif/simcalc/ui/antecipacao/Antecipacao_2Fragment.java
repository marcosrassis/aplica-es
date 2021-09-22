package com.assissoft.canif.simcalc.ui.antecipacao;

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
import com.assissoft.canif.simcalc.ui.SimcalcFragmentComunicator;

import java.util.Locale;

import static com.assissoft.canif.simcalc.utils.FuncoesHelper.calculaAntecipacao_2;

/**
 * Created by Marcos on 05/10/2016.
 *
 */
public class Antecipacao_2Fragment extends Fragment {

    private SimcalcFragmentComunicator fs;
    private View view;
    private EditText valor_da_parcela;
    private EditText taxa_de_juros;
    private EditText parcelas_a_vencer;
    private EditText valor_atual;
    private EditText valor_do_desconto;
    private String[] colecao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.simcalc_antecipacao_2, container, false);

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
        valor_da_parcela = (EditText) view.findViewById(R.id.et_valor_da_parcela);
        taxa_de_juros = (EditText) view.findViewById(R.id.et_taxa_de_juros);
        parcelas_a_vencer = (EditText) view.findViewById(R.id.et_parcelas_a_vencer);
        valor_atual = (EditText) view.findViewById(R.id.et_valor_atual);
        valor_do_desconto = (EditText) view.findViewById(R.id.et_valor_do_desconto);

        valor_da_parcela.addTextChangedListener(textWatcherVP);
        taxa_de_juros.addTextChangedListener(textWatcherJuros);
        parcelas_a_vencer.addTextChangedListener(textWatcherPV);

    }

    private final TextWatcher textWatcherVP = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String _parcelas_a_vencer = "";
            String _valor_da_parcela = "";
            String _juros = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                _valor_da_parcela = s.toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                _juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                _parcelas_a_vencer = parcelas_a_vencer.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                _valor_da_parcela = s.toString().replaceFirst("\\s", "").replace(",", "").trim();
                _juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                _parcelas_a_vencer = parcelas_a_vencer.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            //after text changed
            calculaAntecipacao_2(_juros,_parcelas_a_vencer,_valor_da_parcela, valor_atual, valor_do_desconto);

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private final TextWatcher textWatcherJuros = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String _parcelas_a_vencer = "";
            String _valor_da_parcela = "";
            String _juros = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                _valor_da_parcela = valor_da_parcela.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                _juros = s.toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                _parcelas_a_vencer = parcelas_a_vencer.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                _valor_da_parcela = valor_da_parcela.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                _juros = s.toString().replaceFirst("\\s", "").replace(",", "").trim();
                _parcelas_a_vencer = parcelas_a_vencer.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            calculaAntecipacao_2(_juros,_parcelas_a_vencer,_valor_da_parcela, valor_atual, valor_do_desconto);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private final TextWatcher textWatcherPV = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String _parcelas_a_vencer = "";
            String _valor_da_parcela = "";
            String _juros = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                _valor_da_parcela = valor_da_parcela.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                _juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                _parcelas_a_vencer = s.toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                _valor_da_parcela = valor_da_parcela.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                _juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                _parcelas_a_vencer = s.toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            calculaAntecipacao_2(_juros,_parcelas_a_vencer,_valor_da_parcela, valor_atual, valor_do_desconto);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

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

}