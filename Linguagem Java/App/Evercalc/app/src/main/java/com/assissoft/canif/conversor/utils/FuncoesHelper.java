package com.assissoft.canif.conversor.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.assissoft.canif.R;
import com.assissoft.canif.conversor.model.Unidade;
import com.assissoft.canif.conexaoweb.ConexaohttpClient;
import com.assissoft.canif.conversor.model.Cambio;
import com.assissoft.canif.conversor.model.DefConversor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by Marcos on 18/05/2016.
 *
 */
public class FuncoesHelper {

    //Lista para conter as cotações de câmbio
    private ArrayList<Cambio> cotacoes = new ArrayList<>();

    //Lista para conter as unidades de medida
    private final ArrayList<Unidade> unidades = new ArrayList<>();

    private final String TAG = "FuncoesHelper";

    private final Context contexto;

    public FuncoesHelper(Context contexto){
        this.contexto = contexto;
    }

    public ArrayList<Cambio> getCotacoes() {
        return this.cotacoes;
    }

    public ArrayList<Unidade> getUnidades() {
        return this.unidades;
    }

    public void setCotacoes(ArrayList<Cambio> cotacoes) {
        this.cotacoes = cotacoes;
    }

    private void atualizacaoTaxas(String nomeArqCotacao){ // Faz parte da Thread CargaCotacoesAsync
        int contador =0;
        String registro[] = new String [8];
        cotacoes.clear();

        LogHelper.d(TAG,"Iniciando leitura das taxas de câmbio e carregamento na memória...");

        try {

            File arquivoLido = contexto.getFileStreamPath(nomeArqCotacao + ".csv");
            if (arquivoLido.exists()){
                BufferedReader in = new BufferedReader(new FileReader(arquivoLido));
                String s;
                s = in.readLine();
                while (s != null){

                    StringTokenizer st = new StringTokenizer(s);
                    while (st.hasMoreElements()){
                        registro[contador] = st.nextToken(";");
                        contador++;
                    }

                    contador = 0;
                    Cambio c = new Cambio();
                    //c.setData(registro[0]);
                    //c.setCodigo(registro[1]);
                    //c.setTipo(registro[2]);
                    c.setSigla(registro[3]);
                    c.setTaxaCompra(Double.parseDouble(registro[4].replaceAll(",", ".")));
                    //c.setTaxaVenda(Double.parseDouble(registro[5].replaceAll(",", ".")));
                    //c.setParCompra(Double.parseDouble(registro[6].replaceAll(",", ".")));
                    //c.setParVenda(Double.parseDouble(registro[7].replaceAll(",", ".")));
                    cotacoes.add(c);
                    s = in.readLine();
                }
                in.close();
                LogHelper.d(TAG,"Leitura das taxas de câmbio e carregamento finalizado.");

                int q = cotacoes.size();
                LogHelper.d(TAG,"Qtd de moedas obtidas = " + String.valueOf(q));

            }

        }
        catch (Exception e) {
            LogHelper.e(TAG, e, "Falhou a atualização das taxas.");
        }

    }

    private String leCotacoes(String nomeArqCotacao){ // Faz parte da Thread CargaCotacoesAsync

        String respotaRetornada;
        String bcb = DefConversor.URL_SERVIDOR_COTACOES + nomeArqCotacao + ".csv";
        String pagina = "";

        LogHelper.d(TAG,"Lendo cotações do servidor " + "Url = " + bcb);
        LogHelper.d(TAG,"Lendo arquivo de cotação " + nomeArqCotacao);

        try {
            //Chamando a URL do BC pelo método GET
            respotaRetornada = ConexaohttpClient.executaHttpGet(bcb);
            //Le e armazena o conteúdo da página
            pagina = respotaRetornada;

        } catch (Exception e) {

            Log.i("Erro",e.toString());

            LogHelper.e(TAG, e, "Falhou a leitura do arquivo de cotações do servidor.");
            //Toast.makeText(contexto, R.string.msg_conexao_servidor, Toast.LENGTH_LONG).show();
            Toast.makeText(contexto, e.toString(), Toast.LENGTH_LONG).show();
        }

        return pagina;
    }

    public void iniciaCargaDadosDaWeb() { new CargaCotacoesAsync().execute(); }

    private void gravaCotacoes(String pagina, String nomeArqCotacao){ // Faz parte da Thread CargaCotacoesAsync
        try {

            LogHelper.d(TAG,"Iniciando download do arquivo de cotações de moedas...");

            //Download do arquivo de cotações de moedas
            FileOutputStream arquivoGravar = contexto.openFileOutput(nomeArqCotacao + ".csv", Context.MODE_PRIVATE); //este método cria o arquivo se ele não existir e substitui se ele existir
            arquivoGravar.write(pagina.getBytes()); // o método write espera receber um array, por isso preciso usar outro método como o getBytes que pega um array de caracteres
            arquivoGravar.close();

            LogHelper.d(TAG,"Download do arquivo de cotações de moedas realizado com sucesso.");

            //Atualiza e exibe a mensagem de taxas atualizadas com sucesso
            atualizacaoTaxas(nomeArqCotacao);

        } catch (Exception e) {
            LogHelper.e(TAG, e, "Falhou o download do arquivo de cotações de moedas.");

        }

    }

    private boolean paginaNaoEncontrada(String pagina){ // Faz parte da Thread CargaCotacoesAsync
        boolean status = false;

        int res1,res2,res3;
        res1 = pagina.indexOf("Error Page</title>");
        res2 = pagina.indexOf("erros/404b.htm");
        res3 = pagina.indexOf("404 - File or directory not found.");

        if (res1 != -1 || res2 != -1 || res3 != -1) status = true;

        LogHelper.d(TAG,"Página não encontrada " + "Status = " + String.valueOf(status));

        return status;

    }

    private class CargaCotacoesAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            String nomeArqCotacao;
            String pagina;
            boolean anterior = false;
            boolean continua = true;

            Calendar c = Calendar.getInstance();
            int dia = c.get(Calendar.DAY_OF_MONTH);
            int mes = c.get(Calendar.MONTH);
            int ano = c.get(Calendar.YEAR);

            int d = c.get(Calendar.DAY_OF_WEEK);

            if (d == 1 || d == 2 || d == 7)
               anterior = true;

            LogHelper.d(TAG, "Data de hoje = " + String.valueOf(ano) + String.valueOf(mes+1) + String.valueOf(dia));

            int contador =0;

            try {

                while ((continua) && (contador < 5)) {

                    DataFechamento dfv = new DataFechamento(anterior);
                    nomeArqCotacao = dfv.obtemData(ano,mes,dia);	//obtem uma data de fechamento de câmbio válida no formato YYYYMMDD

                    LogHelper.d(TAG, "CargaCotacoesAsync, " + "nomeArqCotacao antes da Url " + nomeArqCotacao);

                    //Le e armazena o conteúdo da página
                    pagina = leCotacoes(nomeArqCotacao);

                    if (!pagina.equals("")) {
                        if (paginaNaoEncontrada(pagina)){
                            anterior = true;
                            ano = Integer.parseInt(nomeArqCotacao.substring(0, 4));
                            mes = Integer.parseInt(nomeArqCotacao.substring(4, 6)) -1;
                            dia = Integer.parseInt(nomeArqCotacao.substring(6, 8));

                            LogHelper.d(TAG, "CargaCotacoesAsync, " + "Arquivo " + nomeArqCotacao + " não existe no BC");

                        } else {

                            LogHelper.d(TAG, "CargaCotacoesAsync, " + "Arquivo " + nomeArqCotacao + " encontrado na tentativa N. " + contador);

                            continua = false;
                            gravaCotacoes(pagina, nomeArqCotacao);
                        }
                    }

                    contador++;
                }

            } catch (Exception e) {
                LogHelper.e(TAG, e, "Falhou a carga assíncrona de cotações.");
                Log.i("Erro", e.toString());

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            //progressDialog.dismiss();

        }

    }

    private class DataFechamento { // Faz parte da Thread CargaCotacoesAsync

        private final boolean anterior;

        DataFechamento(boolean anterior){
            this.anterior = anterior;
        }

        private String obtemData(int ano, int mes, int dia){

            Calendar c = Calendar.getInstance();
            if (anterior)
                c.setTime(diaAnterior(ano, mes, dia));
            else
                c.setTime(diaHoje(ano, mes, dia));

            return pad(c.get(Calendar.YEAR)) + pad(c.get(Calendar.MONTH)+1) + pad(c.get(Calendar.DAY_OF_MONTH));

        }

        private Date diaAnterior(int ano, int mes, int dia) {
            GregorianCalendar cal = new GregorianCalendar(ano, mes, dia);
            cal.add(Calendar.DATE, retrocede(cal));
            return cal.getTime();
        }

        private Date diaHoje(int ano, int mes, int dia) {
            GregorianCalendar cal = new GregorianCalendar(ano, mes, dia);
            return cal.getTime();
        }

        private int retrocede(Calendar c){
            int d = c.get(Calendar.DAY_OF_WEEK);

            if (d == 2) //segunda
                return -3;
            else if (d == 1) //domingo
                return -2;
            else if (d == 7) //sábado
                return -1;
            else //qualquer outro dia que não seja sábado ou domingo
                return -1;
        }
    }

    private String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);

    }

    private Cambio pesqMoeda(String chave) { // Faz parte da Thread convertAllAsync
        int indice =0;
        Cambio c = new Cambio();
        try {
            while (indice < cotacoes.size()){
                c = cotacoes.get(indice);
                if(chave.equals(c.getSigla())){
                     return c;
                }
                indice++;
            }
        } catch (Exception e) {
            LogHelper.e(TAG, e, "Falhou a pesquisa da moeda.");
        }
        return c;
    }

    public double resultadoConversaoMoeda(String priMoeda, String segMoeda, String montante) { // Faz parte da Thread convertAllAsync
        double resultado = 0;
        double resultadoParcial = 0;
        double txPriMoeda;
        double txSegMoeda;

        if (!montante.equals(".")) {
            if (!cotacoes.isEmpty()) {
                try {
                    if (segMoeda.equals("BRL")) {

                        if (pesqMoeda(priMoeda) != null)
                            resultado = Double.parseDouble(montante) * pesqMoeda(priMoeda).getTaxaCompra();

                        return resultado;
                    } else if (priMoeda.equals("BRL")) {

                        if (pesqMoeda(segMoeda) != null)
                            resultado = Double.parseDouble(montante) / pesqMoeda(segMoeda).getTaxaCompra();

                        return resultado;
                    } else {

                        if (pesqMoeda(priMoeda) != null && pesqMoeda(segMoeda) != null) {
                            txPriMoeda = pesqMoeda(priMoeda).getTaxaCompra();
                            txSegMoeda = pesqMoeda(segMoeda).getTaxaCompra();

                            if (txPriMoeda != 0)
                               resultadoParcial = txSegMoeda / txPriMoeda;

                            if (montante.length() != 0 && resultadoParcial != 0) {
                                resultado = Double.parseDouble(montante) / resultadoParcial;
                            }
                        }

                        return resultado;
                    }

                } catch (Exception e) {
                    LogHelper.e(TAG, e, "Falhou a conversão de moeda.");
                    Toast.makeText(contexto, R.string.msg_calculo_moeda, Toast.LENGTH_LONG).show();
                }
            }else {
                LogHelper.d(TAG, "As cotações não foram carregadas.");
            }
        }

        return resultado;

    }

    public void convertAll(String siglaPriUnidade, double montante, int casasdecimais, boolean is_nc){
        double resultado_calculo;
        unidades.clear();

        TAGHelper tagHelper = new TAGHelper();

        //Carrega as unidades do conversor vigente ou em uso no momento
        String[] array_unidades = contexto.getResources().getStringArray(contexto.getResources().getIdentifier(tagHelper.getNomeArray(contexto),"array",contexto.getPackageName()));

        for (String texto : array_unidades) {

            String siglaSegUnidade = texto.substring(texto.indexOf("(") + 1, texto.indexOf(")"));
            String descricao = texto.substring(0, texto.indexOf("("));

            if (DefConversor.TAXAS.equals(tagHelper.getTAG(contexto)))
                resultado_calculo = ConverteUnidadeHelper.calculaEquivTaxas(siglaPriUnidade + "-" + siglaSegUnidade, String.valueOf(montante));
            else if (DefConversor.TEMPERATURA.equals(tagHelper.getTAG(contexto)))
                resultado_calculo = ConverteUnidadeHelper.calculaTemperatura(siglaPriUnidade + "-" + siglaSegUnidade, String.valueOf(montante));
            else if (DefConversor.MOEDA.equals(tagHelper.getTAG(contexto)))
                resultado_calculo = resultadoConversaoMoeda(siglaPriUnidade, siglaSegUnidade, String.valueOf(montante));
            else
                resultado_calculo = ConverteUnidadeHelper.calcula(tagHelper.getTAG(contexto), siglaPriUnidade + "-" + siglaSegUnidade, String.valueOf(montante));

            Unidade unidade = new Unidade();
            unidade.setSigla(siglaSegUnidade);
            unidade.setDescricao(descricao);

            if (is_nc)
                unidade.setValor(FormatacaoHelper.nc(resultado_calculo, casasdecimais));
            else
                unidade.setValor(FormatacaoHelper.numero(resultado_calculo, casasdecimais));

            unidades.add(unidade);
        }

    }

}
