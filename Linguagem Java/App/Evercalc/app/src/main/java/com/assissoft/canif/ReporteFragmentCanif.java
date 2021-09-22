package com.assissoft.canif;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.assissoft.canif.conversor.ui.ConversorFragmentComunicator;

public class ReporteFragmentCanif extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.reporte_fragment, container, false);

        // call the method setHasOptionsMenu, to have access to the menu from your fragment
        setHasOptionsMenu(true);

        //Exibe o número da versão do Aplicativo
        TextView titleTextView = (TextView) view.findViewById(R.id.tv_title_report);

        String texto = getString(R.string.app_name) + " - " + "Build v" + getVersionName();
        titleTextView.setText(texto);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Inicializa a Interdace de comunição entre fragments
        ConversorFragmentComunicator fc = (ConversorFragmentComunicator) getActivity();

        //Atualiza título da Action Bar
        fc.atualizaTitulo(getString(R.string.app_name_reporte));

        //Exibe o botão flutante
        fc.exibeBotaoFlutuante(View.VISIBLE);

        //Muda o icone do botão flutuante quando estiver nesta tela
        fc.defineBotaoTelaReporte();

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
        ConversorFragmentComunicator fc = (ConversorFragmentComunicator) getActivity();

        //Atualiza título da Action Bar
        fc.atualizaTitulo(getString(R.string.app_name_reporte));

        //Exibe o botão flutante
        fc.exibeBotaoFlutuante(View.VISIBLE);

        //Muda o icone do botão flutuante quando estiver nesta tela
        fc.defineBotaoTelaReporte();

    }

    private String getVersionName(){

        String versionName_ = "";

        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            versionName_ = packageInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException ignored) {

        }
        return versionName_;

    }
}
