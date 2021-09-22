package com.assissoft.canif.simcalc.ui.viver_de_renda;

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

import static com.assissoft.canif.simcalc.utils.FuncoesHelper.calculaRenda_3;

/**
 * Created by Marcos on 11/10/2016.
 *
 */
public class Renda_3Fragment extends Fragment {

    private SimcalcFragmentComunicator fs;
    private View view;
    private EditText valor_acumulado;
    private EditText taxa_de_juros;
    private EditText periodo;
    private EditText retirada;
    private String[] colecao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.simcalc_renda, container, false);

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

        //Faz o link das views de texto com as variáveisa
        valor_acumulado = (EditText) view.findViewById(R.id.et_valor_acumulado);
        taxa_de_juros = (EditText) view.findViewById(R.id.et_taxa_de_juros);
        periodo = (EditText) view.findViewById(R.id.et_periodo);
        retirada = (EditText) view.findViewById(R.id.et_retirada);

        taxa_de_juros.addTextChangedListener(textWatcher);
        periodo.addTextChangedListener(textWatcher);
        retirada.addTextChangedListener(textWatcher);

        //Campos não editáveis
        valor_acumulado.setEnabled(false);

    }

    private final TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //after text changed

            String _retirada = "";
            String _juros = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                _retirada = retirada.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                _juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                _retirada = retirada.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                _juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            calculaRenda_3(_juros,periodo.getText().toString(),_retirada, valor_acumulado);
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