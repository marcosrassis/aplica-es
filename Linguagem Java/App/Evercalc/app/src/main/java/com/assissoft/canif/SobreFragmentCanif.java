package com.assissoft.canif;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.assissoft.canif.conversor.ui.ConversorFragmentComunicator;

public class SobreFragmentCanif extends Fragment {

    private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.sobre_fragment, container, false);

        // call the method setHasOptionsMenu, to have access to the menu from your fragment
        setHasOptionsMenu(true);

        //Exibe o número da versão do Aplicativo
        TextView titleTextView = (TextView) view.findViewById(R.id.tv_numero_versao_app);

        titleTextView.setText(getVersionName());

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Inicializa a Interdace de comunição entre fragments
        final ConversorFragmentComunicator fc = (ConversorFragmentComunicator) getActivity();

        //Atualiza título da Action Bar
        fc.atualizaTitulo(getString(R.string.app_name_sobre));

        //Não exibe o botão flutante
        fc.exibeBotaoFlutuante(View.INVISIBLE);

        //Faz o link das views de texto com as variáveis
        ViewGroup ll_desenvolvedor_app = (ViewGroup) view.findViewById(R.id.ll_desenvolvedor_app);
        ViewGroup ll_descricao_app = (ViewGroup) view.findViewById(R.id.ll_descricao_app);
        ViewGroup ll_contato = (ViewGroup) view.findViewById(R.id.ll_contato);
        ViewGroup ll_observacao = (ViewGroup) view.findViewById(R.id.ll_observacao);

        //Abra a apresentação Evercalc no Youtube
        ll_descricao_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chama a página no Youtube
                Uri uri = Uri.parse(getString(R.string.yt_address));
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                myAppLinkToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myAppLinkToMarket);
            }
        });

        //Abra a página do Facebook
        ll_desenvolvedor_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chama a página no Facebook
                Uri uri = Uri.parse(getString(R.string.fb_address));
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                myAppLinkToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myAppLinkToMarket);
            }
        });

        //Abra o email
        ll_contato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: " + getString(R.string.destinatario_feedback)));
                emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                startActivity(Intent.createChooser(emailIntent, getString(R.string.action_contato)));
            }
        });

        //Avalie-nos
        ll_observacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse(getString(R.string.app_address));
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                myAppLinkToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    startActivity(myAppLinkToMarket);

                } catch (ActivityNotFoundException e) {

                    //the device hasn't installed Google Play
                    Toast.makeText(getActivity(), getString(R.string.msg_google_play), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_search).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Inicializa a Interdace de comunição entre fragments
        ConversorFragmentComunicator fc = (ConversorFragmentComunicator) getActivity();

        //Atualiza título da Action Bar
        fc.atualizaTitulo(getString(R.string.app_name_sobre));

        //Não exibe o botão flutante
        fc.exibeBotaoFlutuante(View.INVISIBLE);

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
