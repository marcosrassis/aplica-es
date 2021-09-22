package com.assissoft.canif.simcalc.utils;

import android.widget.EditText;
import android.widget.TextView;

import com.assissoft.canif.conversor.utils.ConverteUnidadeHelper;
import com.assissoft.canif.conversor.utils.FormatacaoHelper;

import java.util.Locale;

/**
 * Created by Marcos on 05/10/2016.
 *
 */
public class FuncoesHelper {
    private static double tx;
    private static double qp;
    private static double vp;
    private static double vf;
    private static double ve;
    private static double pv;
    private static double vdi;
    private static double vdm;
    private static double va;
    private static double rm;

    private static String trataNulo(String valor) {
        if (!valor.equals(".")) {
            if (valor.equals(""))
                return "0";
            else
                return valor;
        } else
            return "0.";

    }

    public static void calculaAntecipacao_1(String tx_, String qp_, String vn_, EditText va_, EditText vd_) {
        tx = Double.parseDouble(trataNulo(tx_)) / 100; // Taxa ao mês
        qp = Double.parseDouble(trataNulo(qp_));
        double vn = Double.parseDouble(trataNulo(vn_));

        boolean condicao = ((qp > 0) && (vn > 0) && (tx > 0));
        double resultado;

        if (condicao) {

            if (Locale.getDefault().getLanguage().equals("en"))
                resultado = ConverteUnidadeHelper.calculaEquivTaxas("month-day",tx_);
            else
                resultado = ConverteUnidadeHelper.calculaEquivTaxas("mês-dia",tx_);

            tx = resultado / 100; // Taxa ao dia

            double base = 1+tx;
            double v1 = Math.pow(base,qp);

            // Calcula o valor atual
            double va = vn / v1;

            // Calcula o valor do desconto
            double vd = vn - va;

            // Exibe o valor atual
            va_.setText(FormatacaoHelper.numero(va,2));

            // Exibe o valor do desconto
            vd_.setText(FormatacaoHelper.numero(vd,2));

        } else {

            // Exibe o valor atual
            va_.setText("");

            // Exibe o valor do desconto
            vd_.setText("");
        }
    }

    public static void calculaAntecipacao_2(String tx_, String qp_, String vp_, EditText va_, EditText vd_) {
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        qp = Double.parseDouble(trataNulo(qp_));
        vp = Double.parseDouble(trataNulo(vp_));

        double va;
        double va_acum = 0;
        double vd;
        double b;
        double r;

        boolean condicao = ((qp > 0) && (vp > 0) && (tx > 0));

        if (condicao) {
            for(int i=1; i < (qp+1); ++i){
                b = 1+tx;
                r = Math.pow(b,i);
                va = (vp / r);
                va_acum += va;
            }

            //Exibe o valor atual
            va_.setText(FormatacaoHelper.numero(va_acum,2));

            // Calcula o valor do desconto
            vd = (vp * qp) - va_acum;

            //Exibe o valor do desconto
            vd_.setText(FormatacaoHelper.numero(vd,2));

        } else {

            //Exibe o valor atual
            va_.setText("");

            //Exibe o valor do desconto
            vd_.setText("");

        }

    }

    public static void calculaEmprestimo_1(String tx_, String qp_, String m_, EditText c_) {
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        qp = Double.parseDouble(trataNulo(qp_));
        vf = Double.parseDouble(trataNulo(m_));

        boolean condicao = ((qp > 0) && (vf > 0) && (tx > 0));

        if (condicao) {

            //Exibe o valor do empréstimo ou do capital
            c_.setText(FormatacaoHelper.numero(Capital(tx,qp,vf),2));

        } else {

            //Exibe o valor do empréstimo ou do capital
            c_.setText("");

        }

    }

    private static double Capital(double tx, double qp, double vf){
        double fator = Math.pow((1+tx),qp);
        return vf / fator;
    }

    public static void calculaEmprestimo_2(String vf_, String qp_, String ve_, EditText tx_) {
        vf = Double.parseDouble(trataNulo(vf_));
        qp = Double.parseDouble(trataNulo(qp_));
        ve = Double.parseDouble(trataNulo(ve_));

        boolean condicao = ((qp > 0) && (ve > 0) && (vf > 0));

        if (condicao) {

            //Exibe a taxa de juros encontrada
            tx_.setText(FormatacaoHelper.numero(Taxa_de_Juros(vf,qp,ve),2));

        } else {

            //Exibe a taxa de juros encontrada
            tx_.setText("");

        }

    }

    private static double Taxa_de_Juros(double vf, double qp, double ve){
        double v1 = (vf / ve);
        double v2 = 1 / qp;
        double v3 = Math.pow(v1,v2);
        return (v3 - 1) * 100;
    }

    public static void calculaEmprestimo_3(String vf_, String tx_, String ve_, EditText qp_) {
        vf = Double.parseDouble(trataNulo(vf_));
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        ve = Double.parseDouble(trataNulo(ve_));

        boolean condicao = ((vf > 0) && (tx > 0) && (ve > 0));

        if (condicao) {
            double prazo = Math.log(vf / ve) / Math.log(1+tx);

            //Exibe o período ou número de meses
            qp_.setText(FormatacaoHelper.numero(prazo,0));

        } else {

            //Exibe o período ou número de meses
            qp_.setText("");

        }

    }

    public static void calculaEmprestimo_4(String qp_, String tx_, String ve_, EditText m_) {
        qp = Double.parseDouble(trataNulo(qp_));
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        ve = Double.parseDouble(trataNulo(ve_));

        boolean condicao = ((qp > 0) && (tx > 0) && (ve > 0));

        if (condicao) {

            //Exibe o montante ou valor futuro
            m_.setText(FormatacaoHelper.numero(Valor_Futuro(qp,tx,ve),2));

        } else {

            //Exibe o montante ou valor futuro
            m_.setText("");

        }

    }

    private static double Valor_Futuro(double qp, double tx, double ve) {
        double base = 1+tx;
        double fator = Math.pow(base,qp);
        return ve * fator;
    }

    public static void calculaFinanciamento_1(String vp_, String tx_, String qp_, EditText vf_, TextView vtp, TextView vgj) {
        qp = Double.parseDouble(trataNulo(qp_));
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        vp = Double.parseDouble(trataNulo(vp_));

        boolean condicao = ((qp > 0) && (tx > 0) && (vp > 0));

        if (condicao) {
            double base = 1+tx;

            double v1 = Math.pow(base,qp);
            double v2 = 1/v1;
            double v3 = 1-v2;
            double cf = tx / v3; 	  //coeficiente financeiro

            double vf = vp / cf;

            //Exibe o valor financiado
            vf_.setText(FormatacaoHelper.numero(vf,2));

            //Valor total a pagar
            double total_a_pagar = vp * qp;

            //Exibe o valor total a pagar
            vtp.setText(FormatacaoHelper.numero(total_a_pagar,2));

            //Valor gasto com juros
            double valorGastoComJuros = total_a_pagar - vf;

            //Exibe o valor gasto com juros
            vgj.setText(FormatacaoHelper.numero(valorGastoComJuros,2));

        } else {

            //Exibe o valor financiado
            vf_.setText("");

        }

    }

    public static void calculaFinanciamento_2(String vp_, String vf_, String qp_, EditText tx_, TextView vtp, TextView vgj, TextView taj) {
        qp = Double.parseDouble(trataNulo(qp_));
        vp = Double.parseDouble(trataNulo(vp_));
        vf = Double.parseDouble(trataNulo(vf_));

        tx = 0;
        long lvpc = 0;
        long lvp = 0;

        boolean condicao = (((qp > 0) && (vp > 0) && (vf > 0) && (vp < vf)));

        while (condicao && qp <= 360) {

            tx = tx + 0.001; //0.000001

            double base = 1+tx;

            double v1 = Math.pow(base,qp);
            double v2 = 1/v1;
            double v3 = 1-v2;

            double cf = tx / v3; 	  //coeficiente financeiro
            double vpc = vf * cf;

            lvpc = (long) vpc;
            lvp = (long) vp;

            if (lvpc >= lvp){
                condicao = false;
            }

        }

        if (lvpc >= lvp) {
            tx = tx * 100;

            //Exibe a taxa de juros encontrada
            tx_.setText(FormatacaoHelper.numero(tx,4));

            //Valor total a pagar
            double total_a_pagar = vp * qp;

            //Exibe o valor total a pagar
            vtp.setText(FormatacaoHelper.numero(total_a_pagar,2));

            //Valor gasto com juros
            double valorGastoComJuros = total_a_pagar - vf;

            //Exibe o valor gasto com juros
            vgj.setText(FormatacaoHelper.numero(valorGastoComJuros,2));

            double taxaAnual;

            if (Locale.getDefault().getLanguage().equals("en"))
                taxaAnual = ConverteUnidadeHelper.calculaEquivTaxas("month-year",String.valueOf(tx));
            else
                taxaAnual = ConverteUnidadeHelper.calculaEquivTaxas("mês-ano",String.valueOf(tx));

            //Exibe a taxa anual de juros encontrada
            taj.setText(FormatacaoHelper.numero(taxaAnual,4));

        } else {

            //Exibe a taxa de juros encontrada
            tx_.setText("");

        }

    }

    public static void calculaFinanciamento_3(String vp_, String vf_, String tx_, EditText qp_, TextView vtp, TextView vgj) {
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        vp = Double.parseDouble(trataNulo(vp_));
        vf = Double.parseDouble(trataNulo(vf_));

        boolean condicao = (((tx > 0) && (vp > 0) && (vf > 0) && (vp < vf)));

        qp = 0;
        long parcela_calculada = 0;
        long parcela_informada = 0;

        while (condicao && qp <= 360){

            qp = qp + 0.1;

            parcela_calculada = (long) Parcela(qp,vf,tx);
            parcela_informada = (long) vp;

            if (parcela_calculada <= parcela_informada){
                condicao = false;
            }
        }

        if (parcela_calculada <= parcela_informada) {
            //Exibe o período do funcionamento
            if (vp < vf) {
                if (parcela_calculada <= parcela_informada){
                    qp_.setText(FormatacaoHelper.numero(qp,0));

                    //Valor total a pagar
                    double total_a_pagar = vp * qp;

                    //Exibe o valor total a pagar
                    vtp.setText(FormatacaoHelper.numero(total_a_pagar,2));

                    //Valor gasto com juros
                    double valorGastoComJuros = total_a_pagar - vf;

                    //Exibe o valor gasto com juros
                    vgj.setText(FormatacaoHelper.numero(valorGastoComJuros,2));

                } else {
                    qp_.setText("?");
                }
            } else {
                qp_.setText("?");
            }

        } else {
            qp_.setText("");
        }

    }

    public static void calculaFinanciamento_4(String qp_, String vf_, String tx_, EditText vp_, TextView vtp, TextView vgj) {
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        qp = Double.parseDouble(trataNulo(qp_));
        vf = Double.parseDouble(trataNulo(vf_));

        boolean condicao = ((tx > 0) && (qp > 0) && (vf > 0));

        if (condicao) {
            double base = 1 + tx;

            double v1 = Math.pow(base,qp);
            double v2 = 1/v1;
            double v3 = 1-v2;
            double cf = tx / v3; 	  //coeficiente financeiro

            double vp = vf * cf;

            //Exibe o valor da parcela
            vp_.setText(FormatacaoHelper.numero(vp,2));

            //Valor total a pagar
            double total_a_pagar = vp * qp;

            //Exibe o valor total a pagar
            vtp.setText(FormatacaoHelper.numero(total_a_pagar,2));

            //Valor gasto com juros
            double valorGastoComJuros = total_a_pagar - vf;

            //Exibe o valor gasto com juros
            vgj.setText(FormatacaoHelper.numero(valorGastoComJuros,2));

        } else {

            //Exibe o valor da parcela
            vp_.setText("");

        }

    }

    public static void calculaFinanciamento_5(String qp_, String pv_, String ve_, String tx_, EditText vp_) {
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        qp = Double.parseDouble(trataNulo(qp_));
        pv = Double.parseDouble(trataNulo(pv_));
        ve = Double.parseDouble(trataNulo(ve_));

        boolean condicao = ((tx > 0) && (qp > 0) && (pv > 0) && (ve > 0));

        if (condicao) {

            double valor_financiado = pv - ve;
            double base = 1 + tx;

            double v1 = Math.pow(base,qp);
            double v2 = 1/v1;
            double v3 = 1-v2;
            double cf = tx / v3; 	  //coeficiente financeiro

            double vp = valor_financiado * cf;

            //Exibe o valor da parcela
            vp_.setText(FormatacaoHelper.numero(vp,2));

        } else {

            //Exibe o valor da parcela
            vp_.setText("");

        }

    }

    private static double Parcela(double qp, double vf, double tx) {
        double base = 1 + tx;

        double v1 = Math.pow(base,qp);
        double v2 = 1/v1;
        double v3 = 1-v2;
        double cf = tx / v3; 	  //coeficiente financeiro

        return vf * cf;
    }

    public static void calculaEntradaVeiculo(String qp_, String vp_, String preco_, String tx_, EditText vf_, EditText ve_) {
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        qp = Double.parseDouble(trataNulo(qp_));
        vp = Double.parseDouble(trataNulo(vp_));
        double preco_veiculo = Double.parseDouble(trataNulo(preco_));

        boolean condicao = ((tx > 0) && (qp > 0) && (vp > 0) && (preco_veiculo > vp));

        if (condicao) {
            double base = 1+tx;

            double v1 = Math.pow(base,qp);
            double v2 = 1/v1;
            double v3 = 1-v2;

            double cf = tx / v3; 	  //coeficiente financeiro

            double vf = vp / cf;
            double entrada = preco_veiculo - vf;

            //Exibe o valor financiado
            vf_.setText(FormatacaoHelper.numero(vf,2));

            //Exibe o valor da entrada
            ve_.setText(FormatacaoHelper.numero(entrada,2));

        } else {

            //Exibe o valor financiado
            vf_.setText("");

            //Exibe o valor da entrada
            ve_.setText("");

        }

    }

    public static void calculaAplicacao_1(String vp_, String tx_, String qp_, EditText vf_) {
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        qp = Double.parseDouble(trataNulo(qp_));
        vp = Double.parseDouble(trataNulo(vp_));

        boolean condicao = ((tx > 0) && (qp > 0) && (vp > 0));

        if (condicao) {

            //Exibe o valor futuro
            vf_.setText(FormatacaoHelper.numero(Valor_Futuro(qp,tx,vp),2));

        } else {

            //Exibe o valor futuro
            vf_.setText("");

        }

    }

    public static void calculaAplicacao_2(String vf_, String tx_, String qp_, EditText vp_) {
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        qp = Double.parseDouble(trataNulo(qp_));
        vf = Double.parseDouble(trataNulo(vf_));

        boolean condicao = ((tx > 0) && (qp > 0) && (vf > 0));

        if (condicao) {

            //Exibe o valor do capital
            vp_.setText(FormatacaoHelper.numero(Capital(tx,qp,vf),2));

        } else {

            //Exibe o valor do capital
            vp_.setText("");

        }
    }

    public static void calculaAplicacao_3(String vf_, String tx_, String vp_, EditText qp_) {
        vf = Double.parseDouble(trataNulo(vf_));
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        vp = Double.parseDouble(trataNulo(vp_));

        boolean condicao = ((vf > 0) && (tx > 0) && (vp > 0) && (vf > vp));

        if (condicao) {
            double prazo = Math.log(vf / vp) / Math.log(1+tx);

            //Exibe o período ou número de meses
            qp_.setText(FormatacaoHelper.numero(prazo,0));

        } else {

            //Exibe o período ou número de meses
            qp_.setText("");

        }

    }

    public static void calculaAplicacao_4(String vf_, String qp_, String vp_, EditText tx_) {
        vf = Double.parseDouble(trataNulo(vf_));
        qp = Double.parseDouble(trataNulo(qp_));
        vp = Double.parseDouble(trataNulo(vp_));

        boolean condicao = ((vf > 0) && (qp > 0) && (vp > 0) && (vf > vp));

        if (condicao) {

            //Exibe taxa de juros
            tx_.setText(FormatacaoHelper.numero(Taxa_de_Juros(vf,qp,vp),2));

        } else {

            //Exibe taxa de juros
            tx_.setText("");

        }

    }

    public static void calculaCompra_1(String di_, String dm_, String tx_, String qp_, EditText vb_) {
        vdi = Double.parseDouble(trataNulo(di_));
        vdm = Double.parseDouble(trataNulo(dm_));
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        qp = Double.parseDouble(trataNulo(qp_));

        boolean condicao = ((vdi >= 0) && (vdm > 0) && (tx > 0) && (qp > 0));

        if (condicao) {
            double base = 1 + tx;
            double fator = (Math.pow(base,qp) -1);		        //rendimento antecipado (^n) n=qp
            double v1 = fator / tx; 	   			            //fator de acumulação / taxa
            double va = (vdi * Math.pow(base,qp)) + (vdm * v1);	//valor do bem ou valor a atingir total, com o depósito inicial

            //Exibe o valor do bem
            vb_.setText(FormatacaoHelper.numero(va,2));

        } else {

            //Exibe o valor do bem
            vb_.setText("");

        }

    }

    public static void calculaCompra_2(String di_, String tx_, String qp_, String va_, EditText dm_) {
        vdi = Double.parseDouble(trataNulo(di_));       //valor do depósito inicial
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        qp = Double.parseDouble(trataNulo(qp_));
        va = Double.parseDouble(trataNulo(va_));        //valor do bem ou valor a atingir total, com o depósito inicial

        boolean condicao = ((vdi >= 0) && (tx > 0) && (qp > 0) && (va > vdi));

        if (condicao) {
            double base = 1 + tx;
            double fator = (Math.pow(base,qp) -1);		    //rendimento antecipado (^n) n=qp
            double v1 = fator / tx;      				    //fator de acumulação / taxa

            // Usa a fórmula do montante
            double vf_vdi = vdi * Math.pow(base,qp);		//valor futuro do depósito inicial
            double va_parcial = va - vf_vdi;
            double vdm = va_parcial / v1;       			//valor do depósito mensal

            //Exibe o valor do deposito mensal
            dm_.setText(FormatacaoHelper.numero(vdm,2));

        } else {

            //Exibe o valor do deposito mensal
            dm_.setText("");

        }
    }

    public static void calculaCompra_3(String di_, String dm_, String tx_, String va_, EditText qp_) {
        vdi = Double.parseDouble(trataNulo(di_));       //valor do depósito inicial
        vdm = Double.parseDouble(trataNulo(dm_));       //valor do depósito mensal
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        va = Double.parseDouble(trataNulo(va_));        //valor do bem ou valor a atingir total, com o depósito inicial

        double n = 0;
        double vac;
        long lvac = 0;
        long lva = 0;

        boolean condicao = ((vdi >= 0) && (vdm > 0) && (tx > 0) && (va > 0) && (va > (vdi + vdm)));

        while (condicao && n <= 360) {

            n = n + 1;
            double base = 1 + tx;
            double fator = (Math.pow(base,n) -1);			        //rendimento antecipado (^n) n=qp
            double v1 = fator / tx;     					        //fator de acumulação / taxa
            vac = (vdi * Math.pow(base,n)) + (vdm * v1);	    	//valor do bem ou valor a atingir total, com o depósito inicial

            lvac = (long) vac;
            lva = (long) va;

            if ((lvac == lva) || (lva < lvac)){
                condicao = false;
            }
        }

        if ((lvac == lva) || (lva < lvac)) {

            //Exibe o período
            qp_.setText(FormatacaoHelper.numero(n,0));

        } else {

            //Exibe o período
            qp_.setText("");

        }

    }

    public static void calculaCompra_4(String dm_, String tx_, String qp_, String va_, EditText di_) {
        vdm = Double.parseDouble(trataNulo(dm_));       //valor do depósito mensal
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        qp = Double.parseDouble(trataNulo(qp_));
        va = Double.parseDouble(trataNulo(va_));        //valor do bem ou valor a atingir total, com o depósito inicial

        boolean condicao = ((vdm > 0) && (tx > 0) && (qp > 0) && (va > vdm));

        if (condicao) {
            double base = 1 + tx;
            double fator = (Math.pow(base,qp) -1);		    //rendimento antecipado (^n) n=qp
            double v1 = fator / tx;	            			//fator de acumulação / taxa
            double vap = v1 * vdm; 				            //valor do bem ou valor a atingir parcial, sem o depósito inicial
            double vf_vdi = va - vap;				        //valor futuro do depósito inicial

            // Usa a fórumula do capital
            double fator_vdi = Math.pow(base,qp);
            double vdi = vf_vdi / fator_vdi;    			//valor do depósito inicial

            //Exibe o valor do depósito inicial
            di_.setText(FormatacaoHelper.numero(vdi,2));

        } else {

            //Exibe o valor do depósito inicial
            di_.setText("");

        }

    }

    public static void calculaRenda_1(String va_, String tx_, String qp_, EditText rm_) {
        va = Double.parseDouble(trataNulo(va_));
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        qp = Double.parseDouble(trataNulo(qp_));

        boolean condicao = ((va > 0) && (tx > 0) && (qp > 0));

        if (condicao) {
            double base = 1+tx;
            double fator = (Math.pow(base,qp) -1);
            double fator1 = (Math.pow(base,qp) * tx);
            double v1 = (fator / fator1);
            double rm = va / v1;

            //Exibe o valor da retirada mensal
            rm_.setText(FormatacaoHelper.numero(rm,2));

        } else {

            //Exibe o valor da retirada mensal
            rm_.setText("");

        }
    }

    public static void calculaRenda_2(String va_, String tx_, String rm_, EditText qp_) {
        va = Double.parseDouble(trataNulo(va_));
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        rm = Double.parseDouble(trataNulo(rm_));

        boolean condicao = ((va > 0) && (tx > 0) && (rm > 0) && (rm <= va));

        if (condicao) {
            double prazo = Math.log(rm / (rm - (va * tx))) / Math.log(1+tx);

            //Exibe o período ou prazo
            qp_.setText(FormatacaoHelper.numero(prazo,0));

        } else {

            //Exibe o período ou prazo
            qp_.setText("");

        }
    }

    public static void calculaRenda_3(String tx_, String qp_, String rm_, EditText va_) {
        tx = Double.parseDouble(trataNulo(tx_)) / 100;
        qp = Double.parseDouble(trataNulo(qp_));
        rm = Double.parseDouble(trataNulo(rm_));

        boolean condicao = ((tx > 0) && (qp > 0) && (rm > 0));

        if (condicao) {
            double base = 1+tx;
            double va = rm*(((Math.pow(base,qp))-1)/((Math.pow(base,qp))*tx));

            //Exibe o valor acumulado
            va_.setText(FormatacaoHelper.numero(va,2));

        } else {

            //Exibe o valor acumulado
            va_.setText("");

        }

    }

}
