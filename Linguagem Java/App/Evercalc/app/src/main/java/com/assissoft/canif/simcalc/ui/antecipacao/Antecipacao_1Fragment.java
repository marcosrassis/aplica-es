package com.assissoft.canif.simcalc.ui.antecipacao;

import android.content.DialogInterface;
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
import com.assissoft.canif.simcalc.utils.FuncoesHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Marcos on 05/10/2016.
 *
 */
public class Antecipacao_1Fragment extends Fragment implements DatePickerDialog.OnDateSetListener, DialogInterface.OnCancelListener {

    private View view;
    private EditText valor_a_receber;
    private EditText taxa_de_juros;
    private EditText valor_atual;
    private EditText valor_do_desconto;
    private EditText tvVencimento;
    private EditText tvDiaDoPagamento;
    private int ano_v, mes_v, dia_v;
    private int ano_p, mes_p, dia_p;
    private Calendar calendarVencimento;
    private Calendar calendarPagamento;
    private SimcalcFragmentComunicator fs;
    private String[] colecao;
    private boolean btVencimentoPressed = false;
    private boolean btPagamentoPressed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.simcalc_antecipacao_1, container, false);

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
        valor_a_receber = (EditText) view.findViewById(R.id.et_valor_a_receber);
        taxa_de_juros = (EditText) view.findViewById(R.id.et_taxa_de_juros);
        valor_atual = (EditText) view.findViewById(R.id.et_valor_atual);
        valor_do_desconto = (EditText) view.findViewById(R.id.et_valor_do_desconto);
        tvVencimento = (EditText) view.findViewById(R.id.et_vencimento_parcela);
        tvDiaDoPagamento = (EditText) view.findViewById(R.id.et_dia_pagamento);

        valor_a_receber.addTextChangedListener(textWatcherVR);
        taxa_de_juros.addTextChangedListener(textWatcherJuros);

        tvVencimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtemVencimento();
            }
        });

        tvDiaDoPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtemDiaPagamento();
            }
        });

    }

    private final TextWatcher textWatcherVR = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //Calcula o valor atual

            String current = "";
            String juros = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                current = s.toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                current = s.toString().replaceFirst("\\s", "").replace(",", "").trim();
                juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            if (obtemNumeroMeses() >= 0)
               FuncoesHelper.calculaAntecipacao_1(juros, String.valueOf(obtemNumeroMeses()), current, valor_atual, valor_do_desconto);
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

            //Calcula o valor atual

            String current = "";
            String juros = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                current = valor_a_receber.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                juros = s.toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                current = valor_a_receber.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                juros = s.toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            if (obtemNumeroMeses() >= 0)
                FuncoesHelper.calculaAntecipacao_1(juros, String.valueOf(obtemNumeroMeses()), current, valor_atual, valor_do_desconto);
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

    private void obtemVencimento(){
        initCalendarDataVenc();
        Calendar calendar = Calendar.getInstance();
        calendar.set(ano_v, mes_v, dia_v);

        btVencimentoPressed = true; // A data do vencimento foi acionada

        DatePickerDialog datePickerDialogVencimento = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        Calendar dataAtual = Calendar.getInstance();
        dataAtual.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);

        datePickerDialogVencimento.setMinDate(dataAtual);
        datePickerDialogVencimento.setOnCancelListener(this);
        datePickerDialogVencimento.show(getActivity().getFragmentManager(),"datePickerDialogVencimento");
    }

    private void obtemDiaPagamento(){
        initCalendarDataPgto();
        Calendar calendar = Calendar.getInstance();
        calendar.set(ano_p, mes_p, dia_p);

        btPagamentoPressed = true; // A data do pagamento foi acionada

        DatePickerDialog datePickerDialogPagamento = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialogPagamento.setMinDate(calendar);
        datePickerDialogPagamento.setOnCancelListener(this);
        datePickerDialogPagamento.show(getActivity().getFragmentManager(),"datePickerDialogPagamento");
    }

    private void initCalendarDataVenc(){
        if (ano_v == 0) {
            Calendar c = Calendar.getInstance();
            ano_v = c.get(Calendar.YEAR);
            mes_v = c.get(Calendar.MONTH);
            dia_v = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    private void initCalendarDataPgto(){
        Calendar c = Calendar.getInstance();
        ano_p = c.get(Calendar.YEAR);
        mes_p = c.get(Calendar.MONTH);
        dia_p = c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (btVencimentoPressed) {
            calendarVencimento = Calendar.getInstance();
            calendarVencimento.set(year, monthOfYear, dayOfMonth);

            tvVencimento.setText( (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/" + (monthOfYear + 1 < 10 ? "0" + (monthOfYear +1) : monthOfYear + 1) + "/" + year);

            String current = "";
            String juros = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                current = valor_a_receber.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                current = valor_a_receber.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            if (obtemNumeroMeses() >= 0)
                FuncoesHelper.calculaAntecipacao_1(juros, String.valueOf(obtemNumeroMeses()), current, valor_atual, valor_do_desconto);

            btVencimentoPressed = false;

            //Armazena sempre a última data selecionada
            ano_v = year;
            mes_v = monthOfYear;
            dia_v = dayOfMonth;
        }

        if (btPagamentoPressed) {
            calendarPagamento = Calendar.getInstance();
            calendarPagamento.set(year, monthOfYear, dayOfMonth);

            tvDiaDoPagamento.setText( (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/" + (monthOfYear + 1 < 10 ? "0" + (monthOfYear +1) : monthOfYear + 1) + "/" + year);

            String current = "";
            String juros = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                current = valor_a_receber.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
                juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                current = valor_a_receber.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
                juros = taxa_de_juros.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            if (obtemNumeroMeses() >= 0)
                FuncoesHelper.calculaAntecipacao_1(juros, String.valueOf(obtemNumeroMeses()), current, valor_atual, valor_do_desconto);

            btPagamentoPressed = false;

            //Armazena sempre a última data selecionada
            ano_p = year;
            mes_p = monthOfYear;
            dia_p = dayOfMonth;
        }
    }

    private int obtemNumeroMeses(){
        float dayCount =0;

        if (tvVencimento.getText().toString().length() != 0 && tvDiaDoPagamento.getText().toString().length() != 0) {
            long diff = calendarVencimento.getTimeInMillis() - calendarPagamento.getTimeInMillis();
            dayCount = (float) diff / (24 * 60 * 60 * 1000);
        }

        return (int) dayCount;
    }
}