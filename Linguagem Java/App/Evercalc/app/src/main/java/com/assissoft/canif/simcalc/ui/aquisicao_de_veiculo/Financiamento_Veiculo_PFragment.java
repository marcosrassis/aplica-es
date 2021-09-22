package com.assissoft.canif.simcalc.ui.aquisicao_de_veiculo;

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

import static com.assissoft.canif.simcalc.utils.FuncoesHelper.calculaFinanciamento_5;

/**
 * Created by Marcos on 27/04/2017.
 *
 */
public class Financiamento_Veiculo_PFragment extends Fragment {

    private SimcalcFragmentComunicator fs;
    private View view;
    private EditText taxa_de_juros;
    private EditText periodo;
    private EditText valor_da_parcela;
    private EditText valor_da_entrada;
    private EditText preco_do_veiculo;
    private String[] colecao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.simcalc_fin_veiculo_p, container, false);

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
        preco_do_veiculo = (EditText) view.findViewById(R.id.et_preco_veiculo);
        taxa_de_juros = (EditText) view.findViewById(R.id.et_taxa_de_juros);
        periodo = (EditText) view.findViewById(R.id.et_periodo);
        valor_da_parcela = (EditText) view.findViewById(R.id.et_valor_da_parcela);
        valor_da_entrada = (EditText) view.findViewById(R.id.et_valor_da_entrada);

        preco_do_veiculo.addTextChangedListener(textWatcher);
        valor_da_entrada.addTextChangedListener(textWatcher);
        taxa_de_juros.addTextChangedListener(textWatcher);
        periodo.addTextChangedListener(textWatcher);

    }

    private final TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //after text changed

            String _preco_do_veiculo = "";
            String _valor_da_entrada = "";
            String _juros = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                _preco_do_veiculo = preco_do_veiculo.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                _valor_da_entrada = valor_da_entrada.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                _juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                _preco_do_veiculo = preco_do_veiculo.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                _valor_da_entrada = valor_da_entrada.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                _juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            calculaFinanciamento_5(periodo.getText().toString(),_preco_do_veiculo,_valor_da_entrada, _juros, valor_da_parcela);
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
