package com.assissoft.canif.simcalc.ui.emprestimo;

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

import static com.assissoft.canif.simcalc.utils.FuncoesHelper.calculaEmprestimo_2;

/**
 * Created by Marcos on 05/10/2016.
 *
 */
public class Emprestimo_2Fragment extends Fragment {

    private SimcalcFragmentComunicator fs;
    private View view;
    private EditText valor_do_emprestimo;
    private EditText taxa_de_juros;
    private EditText numero_de_meses;
    private EditText montante;
    private String[] colecao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.simcalc_emprestimo, container, false);

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
        valor_do_emprestimo = (EditText) view.findViewById(R.id.et_valor_do_emprestimo);
        taxa_de_juros = (EditText) view.findViewById(R.id.et_taxa_de_juros);
        numero_de_meses = (EditText) view.findViewById(R.id.et_numero_de_meses);
        montante = (EditText) view.findViewById(R.id.et_montante);

        montante.addTextChangedListener(textWatcher);
        valor_do_emprestimo.addTextChangedListener(textWatcher);
        numero_de_meses.addTextChangedListener(textWatcher);

        //Campos não editáveis
        taxa_de_juros.setEnabled(false);

    }

    private final TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //after text changed

            String _valor_emprestimo = "";
            String _montante = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                _valor_emprestimo = valor_do_emprestimo.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                _montante = montante.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                _valor_emprestimo = valor_do_emprestimo.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                _montante = montante.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            calculaEmprestimo_2(_montante,numero_de_meses.getText().toString(),_valor_emprestimo, taxa_de_juros);

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