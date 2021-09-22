package com.assissoft.canif.conversor.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.assissoft.canif.conexaobanco.ComandosBD;
import com.assissoft.canif.R;
import com.assissoft.canif.simcalc.model.Preferencia;

/**
 * Created by Marcos on 10/08/2016.
 *
 */
public class DialogoCasasDecimais extends DialogFragment {

    private Preferencia preferencia = new Preferencia();
    private TextView tv_valor_max;
    private View view;
    private int casasdecimais = 2; // 2-Default
    private ComandosBD bd;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final ViewGroup nullParent = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.casas_decimais,nullParent);

        builder.setView(view)
                .setTitle(getString(R.string.titulo_formatacao))
                .setMessage(getString(R.string.titulo_numero_casas_decimais))
                .setPositiveButton(getString(R.string.texto_bt_aceitar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Grava a formatação de casas decimais
                        if (preferencia.getId() > 0) {
                            preferencia.setCasasdecimais(casasdecimais);
                            preferencia.setRegistro("'configuracao'");

                            //Abre o bando de dados
                            bd = new ComandosBD(getContext());

                            bd.atualizarPreferencia(preferencia);
                        } else {
                            preferencia.setCasasdecimais(casasdecimais);
                            preferencia.setRegistro("'configuracao'");

                            //Abre o bando de dados
                            bd = new ComandosBD(getContext());

                            bd.inserirPreferencia(preferencia);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.texto_bt_cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.show();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Abre o bando de dados
        bd = new ComandosBD(getContext());

        //Obtem as preferências do usuário
        preferencia = bd.buscarPreferencia();

        //Lê do banco o número de casas decimais
        casasdecimais = (preferencia.getId() > 0 ? preferencia.getCasasdecimais() : 2);

        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        tv_valor_max = (TextView) view.findViewById(R.id.tv_valor_max);

        //Atualiza o valor do progress bar
        seekBar.setProgress(casasdecimais);
        tv_valor_max.setText(String.valueOf(casasdecimais));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_valor_max.setText(String.valueOf(progress));
                casasdecimais = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}


