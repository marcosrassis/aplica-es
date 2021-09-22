package com.assissoft.canif;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.assissoft.canif.conversor.ui.ConversorFragmentComunicator;
import com.assissoft.canif.conexaobanco.ComandosBD;
import com.assissoft.canif.simcalc.model.Preferencia;

public class ConfiguracaoFragmentCanif extends Fragment {

    private Preferencia preferencia = new Preferencia();
    private View view = null;
    private ConversorFragmentComunicator fc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.configuracao_fragment, container, false);

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
        ComandosBD bd = new ComandosBD(getActivity());

        //Obtem as preferências do usuário
        preferencia = bd.buscarPreferencia();

        //Lê do banco a notação científica
        int notacao = (preferencia.getId() > 0 ? preferencia.getNotacao() : 0);

        //Atualiza título da Action Bar
        fc.atualizaTitulo(getString(R.string.app_name_configuracao));

        //Exibe o botão flutante
        fc.exibeBotaoFlutuante(View.INVISIBLE);

        //Faz o link das views de texto com as variáveis
        ViewGroup ll_numero_casas_decimais = (ViewGroup) view.findViewById(R.id.ll_numero_casas_decimais);
        ViewGroup ll_contato = (ViewGroup) view.findViewById(R.id.ll_contato);
        ViewGroup ll_sobre = (ViewGroup) view.findViewById(R.id.ll_sobre);

        Switch mySwitch = (Switch) view.findViewById(R.id.sw_status_notacao_cientifica);

        //Mostra na tela se a Notação está ativa ou não
        mySwitch.setChecked((notacao != 0));

        //Exibe o Diálogo de casas decimais
        ll_numero_casas_decimais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Exibe o Diálogo de casas decimais
                fc.exibeDialogoCasasDecimais();

            }
        });

        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Notação científica ativada, grava 1
                    if (preferencia.getId() > 0) {

                        preferencia.setNotacao(1);
                        preferencia.setRegistro("'configuracao'");

                        //Abre o bando de dados
                        ComandosBD bd = new ComandosBD(getActivity());
                        bd.atualizarPreferencia(preferencia);

                    } else {
                        preferencia.setNotacao(1);
                        preferencia.setRegistro("'configuracao'");

                        //Abre o bando de dados
                        ComandosBD bd = new ComandosBD(getActivity());
                        bd.inserirPreferencia(preferencia);
                    }
                }else{
                    //Notação científica desativada, grava 0
                    if (preferencia.getId() > 0) {

                        preferencia.setNotacao(0);
                        preferencia.setRegistro("'configuracao'");

                        //Abre o bando de dados
                        ComandosBD bd = new ComandosBD(getActivity());
                        bd.atualizarPreferencia(preferencia);

                    } else {
                        preferencia.setNotacao(0);
                        preferencia.setRegistro("'configuracao'");

                        //Abre o bando de dados
                        ComandosBD bd = new ComandosBD(getActivity());
                        bd.inserirPreferencia(preferencia);
                    }
                }
            }
        });

        //Exibe o Diálogo Sobre o aplicativo
        ll_sobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Exibe o Diálogo
                fc.chamaSobreConf();

            }
        });

        //Exibe o Diálogo de Reporte de problema
        ll_contato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Exibe o Diálogo
                fc.chamaReporteConf();

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.findItem(R.id.action_conf).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Inicializa a Interdace de comunição entre fragments
        fc = (ConversorFragmentComunicator) getActivity();

        //Atualiza título da Action Bar
        fc.atualizaTitulo(getString(R.string.app_name_configuracao));

        //Exibe o botão flutante
        fc.exibeBotaoFlutuante(View.INVISIBLE);

    }

}

