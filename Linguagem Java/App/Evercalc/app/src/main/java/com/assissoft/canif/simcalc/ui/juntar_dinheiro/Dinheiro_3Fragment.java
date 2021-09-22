package com.assissoft.canif.simcalc.ui.juntar_dinheiro;

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

import static com.assissoft.canif.simcalc.utils.FuncoesHelper.calculaCompra_3;

/**
 * Created by Marcos on 11/10/2016.
 *
 */
public class Dinheiro_3Fragment extends Fragment {

    private SimcalcFragmentComunicator fs;
    private View view;
    private EditText deposito_inicial;
    private EditText deposito_mensal;
    private EditText taxa_de_juros;
    private EditText periodo;
    private EditText valor_acumulado;
    private String[] colecao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.simcalc_dinheiro, container, false);

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
        deposito_inicial = (EditText) view.findViewById(R.id.et_deposito_inicial);
        deposito_mensal = (EditText) view.findViewById(R.id.et_deposito_mensal);
        taxa_de_juros = (EditText) view.findViewById(R.id.et_taxa_de_juros);
        periodo = (EditText) view.findViewById(R.id.et_periodo);
        valor_acumulado = (EditText) view.findViewById(R.id.et_valor_acumulado);

        deposito_inicial.addTextChangedListener(textWatcher);
        deposito_mensal.addTextChangedListener(textWatcher);
        taxa_de_juros.addTextChangedListener(textWatcher);
        valor_acumulado.addTextChangedListener(textWatcher);

        //Campos não editáveis
        periodo.setEnabled(false);

    }

    private final TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Comprar à Vista ou Juntar Dinheiro

            String _deposito_inicial = "";
            String _deposito_mensal = "";
            String _valor_acumulado = "";
            String _juros = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                _deposito_inicial = deposito_inicial.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                _deposito_mensal = deposito_mensal.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                _valor_acumulado = valor_acumulado.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                _juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                _deposito_inicial = deposito_inicial.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                _deposito_mensal = deposito_mensal.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                _valor_acumulado = valor_acumulado.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                _juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            calculaCompra_3(_deposito_inicial,_deposito_mensal,_juros,_valor_acumulado, periodo);
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