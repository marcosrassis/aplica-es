package com.assissoft.canif;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.assissoft.canif.conversor.model.DefConversor;
import com.assissoft.canif.conversor.model.ListaCotacoes;
import com.assissoft.canif.conversor.ui.ConversorListFragment;
import com.assissoft.canif.conversor.ui.DialogoCasasDecimais;
import com.assissoft.canif.conversor.ui.ConversorFragmentComunicator;
import com.assissoft.canif.conversor.ui.MoedaFragment;
import com.assissoft.canif.conversor.ui.UnidadeFragment;
import com.assissoft.canif.conexaobanco.ComandosBD;
import com.assissoft.canif.conversor.utils.ConverteUnidadeHelper;
import com.assissoft.canif.conversor.utils.FormatacaoHelper;
import com.assissoft.canif.conversor.utils.FuncoesHelper;
import com.assissoft.canif.conversor.utils.TextoHelper;
import com.assissoft.canif.simcalc.model.Preferencia;
import com.assissoft.canif.simcalc.model.DefSimcalc;
import com.assissoft.canif.simcalc.ui.SimcalcFragmentComunicator;
import com.assissoft.canif.simcalc.ui.SimcalcListFragment;
import com.assissoft.canif.simcalc.utils.OpenFragmentCalculo;

import java.util.Locale;

import static com.assissoft.canif.R.id.viewPager;

public class ActivityCanif extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConversorFragmentComunicator, SimcalcFragmentComunicator, GeralFragmentComunicator {

    private Preferencia preferencia = new Preferencia();
    private FuncoesHelper funcoesHelper;
    private final Bundle args = new Bundle();

    private ComandosBD bd;

    private CustomViewPagerCanif mViewPager;
    private DrawerLayout drawer = null;
    private ActionBarDrawerToggle toggle;
    private FragmentManager manager = null;
    private FloatingActionButton fab;
    private ListaCotacoes lc;
    private int casasdecimais = 2; //2-Default
    private boolean isChecked = false;
    private boolean menuConversaoTodasUnidades = false;
    private Menu menu;
    private Bundle mSavedInstanceState;

    private final boolean op_Add = true;
    private final boolean op_Replace = false;
    private final String MENU_CONVERTE_TODAS_KEY = "MenuConverteTodasUnidades";
    private final String TELA_CALCULO_KEY = "Tela_de_calculo";
    private final String CONVERSOR_VIGENTE_ID_KEY = "Conversor_ID";
    private final String SIMCALC_VIGENTE_ID_KEY = "Simcalc_ID";
    private final String BOTAO_FLUTUANTE_KEY = "Botao_Flutuante";

    private int botaoFlutuante = View.INVISIBLE;
    private boolean telaCalculo = false;

    private TabLayout tabLayout;
    private String CONVERSOR_VIGENTE_TAG = "";
    private int CONVERSOR_VIGENTE_ID;
    private int SIMCALC_VIGENTE_ID;

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id " +
                    "'toolbar'");
        }

        toolbar.inflateMenu(R.menu.main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (drawer != null) {
            //Faz o link do navegation view com a variável
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setItemIconTintList(null);  //Permite usar icons coloridos no NavegationView - menu lateral esquerdo
            navigationView.setNavigationItemSelectedListener(this);

            toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(mDrawerListener);
            setSupportActionBar(toolbar);

        } else {
            setSupportActionBar(toolbar);
        }

    }

    private boolean isConversorFragmentAdded() {
        ConversorListFragment fc = (ConversorListFragment) manager.findFragmentByTag(DefConversor.LISTA_CONVERSOR);

        return fc != null;
    }

    private boolean isSimcalcFragmentAdded() {
        SimcalcListFragment fs = (SimcalcListFragment) manager.findFragmentByTag(DefSimcalc.LISTA);

        return fs != null;
    }

    private void showConvertersList(boolean operation) {
        ConversorListFragment fragment = new ConversorListFragment();
        FragmentTransaction ft = manager.beginTransaction();
        if ((operation) && !isConversorFragmentAdded())
            ft.add(R.id.fragment_container, fragment, DefConversor.LISTA_CONVERSOR);
        else
            ft.replace(R.id.fragment_container, fragment, DefConversor.LISTA_CONVERSOR);
        ft.addToBackStack(DefConversor.LISTA_CONVERSOR);
        ft.commit();

    }

    private void showSimcalcList(boolean operation) {
        SimcalcListFragment fragment = new SimcalcListFragment();
        FragmentTransaction ft = manager.beginTransaction();
        if ((operation) && !isSimcalcFragmentAdded())
            ft.add(R.id.simcalc_container, fragment, DefSimcalc.LISTA);
        else
            ft.replace(R.id.simcalc_container, fragment, DefSimcalc.LISTA);
        ft.addToBackStack(DefSimcalc.LISTA);
        ft.commit();

        //Esconde o botão flutuante
        fab.hide();

    }

    private void showSimcalcMenu() {

        if (telaCalculo) {
            //Chama o menu de cálculo anterior
            chamaCalculoSelecionado(SIMCALC_VIGENTE_ID);
        } else {
            //Atualiza o título da barra
            showButtonHome(getString(R.string.app_name));

            //Chama o menu principal do Simcalc
            showSimcalcList(op_Replace);
        }

    }

    private boolean isFirstScreen() {

        boolean retorno = false;

        if (tabLayout.getSelectedTabPosition() == 0) {
            SimcalcListFragment fs = (SimcalcListFragment) manager.findFragmentByTag(DefSimcalc.LISTA);

            retorno = fs != null && fs.isVisible();

        } else if (tabLayout.getSelectedTabPosition() == 1) {
            ConversorListFragment fc = (ConversorListFragment) manager.findFragmentByTag(DefConversor.LISTA_CONVERSOR);

            retorno = fc != null && fc.isVisible();
        }

        return retorno;

    }

    private final DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerClosed(View drawerView) {
            if (toggle != null) toggle.onDrawerClosed(drawerView);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            if (toggle != null) toggle.onDrawerStateChanged(newState);
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            if (toggle != null) toggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            if (toggle != null) toggle.onDrawerOpened(drawerView);
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.app_name);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Salva o estado da instância
        mSavedInstanceState = savedInstanceState;

        //Pega uma instância do gerente de fragments
        manager = getSupportFragmentManager();

        //Prepara o banco de dados
        bd = new ComandosBD(this);

        //Obtem as preferências do usuário
        preferencia = bd.buscarPreferencia();
        casasdecimais = (preferencia.getId() > 0 ? preferencia.getCasasdecimais() : 2);
        isChecked = (preferencia.getNotacao() != 0);

        //Inicializa a classe de Funções de Câmbio com o contexto da atividade principal
        funcoesHelper = new FuncoesHelper(ActivityCanif.this);

        //Inicializa a barra de ferramenta
        initializeToolbar();

        //Inicializa o View Pager Adapter
        InitializeViewPager();

        //Chama o fragment que irá exibir a lista de conversores
        if (savedInstanceState == null) {

            //Acessa o site do BC e carrega o arquivo de moedas
            funcoesHelper.iniciaCargaDadosDaWeb();
            lc = new ListaCotacoes(funcoesHelper.getCotacoes());

        } else {
            //Acessa as cotações de moedas que foram serializadas
            lc = (ListaCotacoes) savedInstanceState.getSerializable(ListaCotacoes.KEY);

            //Verifica qual era o valor do menu (true ou false)
            menuConversaoTodasUnidades = (boolean) savedInstanceState.getSerializable(MENU_CONVERTE_TODAS_KEY);

            telaCalculo = (boolean) savedInstanceState.getSerializable(TELA_CALCULO_KEY);

            CONVERSOR_VIGENTE_ID = (int) savedInstanceState.getSerializable(CONVERSOR_VIGENTE_ID_KEY);
            SIMCALC_VIGENTE_ID = (int) savedInstanceState.getSerializable(SIMCALC_VIGENTE_ID_KEY);
            botaoFlutuante = (int) savedInstanceState.getSerializable(BOTAO_FLUTUANTE_KEY);
        }

        //Inicializa as cotações de moeda se tiverem sido carregadas
        if (lc.cotacoes.size() > 0) funcoesHelper.setCotacoes(lc.cotacoes);

        //Esconde o botão flutuante
        fab = (FloatingActionButton) findViewById(R.id.fab);

        //Faz a troca de conversão de medidas caso o botão flutuante tenha sido acionado
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabAction();
            }
        });

    }

    private void InitializeViewPager() {
        SectionsPagerAdapterCanif mSectionsPagerAdapter = new SectionsPagerAdapterCanif(manager, this);
        mViewPager = (CustomViewPagerCanif) findViewById(viewPager);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
            tabLayout.setupWithViewPager(mViewPager);
        }
    }

    @Override
    public void onBackPressed() {
        //Define a imagem default do botão flutuante
        fab.setImageResource(R.mipmap.ic_rotate_3d);

        if (tabLayout.getSelectedTabPosition() == 0) {
            //Pesquisa a referência do fragment do Simcalc
            SimcalcListFragment fs = (SimcalcListFragment) manager.findFragmentByTag(DefSimcalc.LISTA);

            //Verifica se o fragment está ativo e visível na tela
            if (fs != null && fs.isVisible()) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            } else {
                if ((manager != null) && (drawer != null)) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {

                        //Atualiza o título da barra de acão
                        showButtonHome(getString(R.string.app_name));

                        //Desativa o botão flutuante
                        fab.hide();

                        //Atualiza o flag do botão flutuante
                        botaoFlutuante = View.INVISIBLE;

                        if (manager.getBackStackEntryCount() > 0) {
                            //Exibe a tela do Simcalc ou do menu de cálculo anterior
                            showSimcalcMenu();
                        } else {
                            super.onBackPressed();
                        }
                    }
                }
            }
        }

        if (tabLayout.getSelectedTabPosition() == 1) {
            //Pesquisa a referência do fragment da Lista de DefConversor
            ConversorListFragment fc = (ConversorListFragment) manager.findFragmentByTag(DefConversor.LISTA_CONVERSOR);

            //Verifica se o fragment está ativo e visível na tela
            if (fc != null && fc.isVisible()) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            } else {
                if ((manager != null) && (drawer != null)) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {

                        //Atualiza o título da barra de acão
                        showButtonHome(getString(R.string.app_name));

                        //Desativa o botão flutuante
                        fab.hide();

                        if (manager.getBackStackEntryCount() > 0) {
                            //Exibe a tela do conversor
                            showConvertersList(op_Replace);

                            //Esconde o Botão de Conversão de Todas as Unidades
                            menuConversaoTodasUnidades = false;
                            menu.findItem(R.id.action_convert_all).setVisible(false);

                        } else {
                            super.onBackPressed();
                        }
                    }
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_convert_all).setVisible(menuConversaoTodasUnidades);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Chama o fragment de busca
        if (id == R.id.action_search) {
            chamaBusca();
            return false;
        }

        //Chama a Conversão de Todas as Unidades
        if (id == R.id.action_convert_all) {
            chamaConversaoTodasUnidades();
            return false;
        }

        //Chama a tela de Configurações
        if (id == R.id.action_conf) {
            chamaConfiguracoes();
            return false;
        }

        if (id == R.id.action_rate) {
            //Nos avalie na Play Store
            Uri uri = Uri.parse(getString(R.string.app_address));
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            myAppLinkToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try {
                startActivity(myAppLinkToMarket);

            } catch (ActivityNotFoundException e) {

                //the device hasn't installed Google Play
                Toast.makeText(this, getString(R.string.msg_google_play), Toast.LENGTH_LONG).show();
            }

        }

        if (id == R.id.action_share) {
            //Menu main drawer - Compartilhe
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.titulo_assunto_email));
            sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.msg_compartilhe) + getString(R.string.app_address));
            sharingIntent.putExtra(Intent.EXTRA_EMAIL, "");
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.titulo_compartilhe)));
        }

        if ((id == android.R.id.home) && isFirstScreen())
            drawer.openDrawer(GravityCompat.START);
        else { //Botão Back da Toolbar

            //Exibe a tela do simcalc ou o menu de cálculo anterior
            if (tabLayout.getSelectedTabPosition() == 0)
                showSimcalcMenu();

            //Exibe a tela do conversor
            if (tabLayout.getSelectedTabPosition() == 1)
                showConvertersList(op_Replace);

            //Esconde o botão de Conversão de Todas as Unidades
            menuConversaoTodasUnidades = false;
            menu.findItem(R.id.action_convert_all).setVisible(false);

            //Desativa o botão flutuante
            fab.hide();

            //Define a imagem default do botão flutuante
            fab.setImageResource(R.mipmap.ic_rotate_3d);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void chamaConversorSelecionado(int conversor) {

        //Atualiza o ID do DefConversor Vigente ou selecionado
        CONVERSOR_VIGENTE_ID = conversor;

        String[] colecao = this.getResources().getStringArray(R.array.titulo_conversores);
        CONVERSOR_VIGENTE_TAG = colecao[conversor];

        if (DefConversor.MOEDA.equals(CONVERSOR_VIGENTE_TAG)) {
            //Passa as cotações de moeda para o fragment de moeda
            args.putSerializable(ListaCotacoes.KEY, new ListaCotacoes(lc.cotacoes));

            //Chama o conversor de Moeda
            OpenFragmentCanif open = new OpenFragmentCanif(1);
            open.openFragment(manager,CONVERSOR_VIGENTE_TAG,args);

        } else {

            //Chama o conversor de Unidade correspondente
            OpenFragmentCanif open = new OpenFragmentCanif(1);
            open.openFragment(manager,CONVERSOR_VIGENTE_TAG,null);

        }

        //Atualiza o flag do botão flutuante
        botaoFlutuante = View.VISIBLE;
    }

    private void fabAction() {
        UnidadeFragment fragUnidade = null;
        MoedaFragment fm = null;

        //Prepara o banco de dados
        bd = new ComandosBD(this);

        //Obtem as preferências do usuário
        preferencia = bd.buscarPreferencia();
        casasdecimais = (preferencia.getId() > 0 ? preferencia.getCasasdecimais() : 2);
        isChecked = (preferencia.getNotacao() != 0);

        String[] colecao = getResources().getStringArray(R.array.titulo_conversores);
        String TAG = colecao[CONVERSOR_VIGENTE_ID];

        //Pesquisa a referência do fragment de Moeda
        if (DefConversor.MOEDA.equals(TAG))
            fm = (MoedaFragment) manager.findFragmentByTag(DefConversor.MOEDA);

        //Pesquisa a referência do fragment selecionado
        if (!(DefConversor.MOEDA.equals(TAG))) {
            fragUnidade = (UnidadeFragment) manager.findFragmentByTag(TAG);
        }

        //Pesquisa a referência do fragment de Reporte de bug/problema
        ReporteFragmentCanif fr = (ReporteFragmentCanif) manager.findFragmentByTag(DefConversor.REPORTE);

        //Verifica se o fragment está ativo e visível na tela
        if (fm != null && fm.isVisible()) {

            String priUnidade, segUnidade;

            TextView tvDe = (TextView) findViewById(R.id.tvDe);
            TextView tvPara = (TextView) findViewById(R.id.tvPara);
            EditText etMontante = (EditText) findViewById(R.id.etMontante);
            TextView tvResultado = (TextView) findViewById(R.id.tvResultado);

            priUnidade = tvDe.getText().toString();
            segUnidade = tvPara.getText().toString();

            //Inverte o que foi selecionado
            tvDe.setText(segUnidade);
            tvPara.setText(priUnidade);

            String _montante = "";

            if (Locale.getDefault().getLanguage().equals("pt")){
                _montante = etMontante.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
            } else {
                _montante = etMontante.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
            }

            //Carrega a lista de cotações em memória se já houve um dowload anterior e faz a conversão de Moeda e mostra na tela
            if (lc.cotacoes.size() > 0) {
                double resultado;

                resultado = funcoesHelper.resultadoConversaoMoeda(TextoHelper.extraiSigla(tvDe.getText().toString()), TextoHelper.extraiSigla(tvPara.getText().toString()), _montante);
                tvResultado.setText(FormatacaoHelper.moeda(resultado, casasdecimais));
            }
        }

        //Verifica se o fragment está ativo e visível na tela
        if (fragUnidade != null && fragUnidade.isVisible()) inverteConvercao();

        //Verifica se o fragment está ativo e visível na tela
        if (fr != null && fr.isVisible()) {

            StringBuilder mensagem = new StringBuilder();

            //Captura o texto da ocorrência
            TextView etRelatorio = (TextView) findViewById(R.id.et_report);

            mensagem.append(getDeviceInfo()).append('\r').append('\n');
            mensagem.append("MENSAGEM:").append('\r').append('\n');
            mensagem.append(" - ").append(etRelatorio.getText().toString()).append('\r').append('\n');

            if (etRelatorio.getText().toString().length() > 0) {
                //Menu main drawer - Opine
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: " + getString(R.string.destinatario_feedback)));
                emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.putExtra(Intent.EXTRA_TEXT, mensagem.toString());
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.titulo_assunto_email_reporte));
                startActivity(Intent.createChooser(emailIntent, getString(R.string.app_name_reporte)));

            } else {

                //Nenhuma mensagem de report foi digitada
                Toast.makeText(this, getString(R.string.msg_tela_reporte), Toast.LENGTH_LONG).show();

            }
        }

    }

    private void inverteConvercao() {
        String priUnidade, segUnidade;

        TextView tvDe = (TextView) findViewById(R.id.tvDe);
        TextView tvPara = (TextView) findViewById(R.id.tvPara);
        EditText etMontante = (EditText) findViewById(R.id.etMontante);
        TextView tvResultado = (TextView) findViewById(R.id.tvResultado);

        priUnidade = tvDe.getText().toString();
        segUnidade = tvPara.getText().toString();

        //Inverte o que foi selecionado
        tvDe.setText(segUnidade);
        tvPara.setText(priUnidade);

        //Faz a conversão da Massa e mostra na tela
        double resultado;

        String _montante = "";

        if (Locale.getDefault().getLanguage().equals("pt")){
            _montante = etMontante.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
        } else {
            _montante = etMontante.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
        }

        String[] colecao = getResources().getStringArray(R.array.titulo_conversores);
        String TAG = colecao[CONVERSOR_VIGENTE_ID];

        if (DefConversor.TAXAS.equals(TAG))
            resultado = ConverteUnidadeHelper.calculaEquivTaxas(TextoHelper.extraiSigla(tvDe.getText().toString()) + "-" + TextoHelper.extraiSigla(tvPara.getText().toString()), _montante);
        else if (DefConversor.TEMPERATURA.equals(TAG))
            resultado = ConverteUnidadeHelper.calculaTemperatura(TextoHelper.extraiSigla(tvDe.getText().toString()) + "-" + TextoHelper.extraiSigla(tvPara.getText().toString()), _montante);
        else
            resultado = ConverteUnidadeHelper.calcula(TAG, TextoHelper.extraiSigla(tvDe.getText().toString()) + "-" + TextoHelper.extraiSigla(tvPara.getText().toString()), _montante);

        if (isChecked)
            tvResultado.setText(FormatacaoHelper.nc(resultado, casasdecimais));
        else {

            String texto = FormatacaoHelper.numero(resultado, casasdecimais) + (DefConversor.TAXAS.equals(TAG) ? " %" : "");
            tvResultado.setText(texto);
        }

    }

    //Pega as informações do dispositivo
    private String getDeviceInfo() {

        StringBuilder deviceInfo = new StringBuilder();

        int versao_sdk_ = Build.VERSION.SDK_INT;                // API Level
        String model_ = Build.MODEL;                            // Model of Device
        String display_ = Build.DISPLAY;                        // Version of Device
        String manufacturer_ = Build.MANUFACTURER;              // Manufacturer
        String android_version_ = Build.VERSION.RELEASE;        // Version of Android

        int width, height;

        if (Build.VERSION.SDK_INT >= 14) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            DisplayMetrics metrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        }

        deviceInfo.append("DEVICE INFO:").append('\r').append('\n');
        deviceInfo.append(" - Fabricante: ").append(manufacturer_).append('\r').append('\n');
        deviceInfo.append(" - Número do modelo: ").append(model_).append('\r').append('\n');
        deviceInfo.append(" - Versão do dispositivo: ").append(display_).append('\r').append('\n');
        deviceInfo.append(" - Versão do SDK: ").append(versao_sdk_).append('\r').append('\n');
        deviceInfo.append(" - Versão do Android: ").append(android_version_).append('\r').append('\n');
        deviceInfo.append(" - Resolução de Tela: ").append(height).append("x").append(width).append('\r').append('\n');

        return deviceInfo.toString();
    }

    private void chamaSobre() {
        botaoFlutuante = View.INVISIBLE;
        //Chama o fragment Sobre
        OpenFragmentCanif open = new OpenFragmentCanif(tabLayout.getSelectedTabPosition());
        open.openFragment(manager,DefConversor.SOBRE,null);
    }

    private void chamaBusca(){
        botaoFlutuante = View.INVISIBLE;

        //Habilita o swipe da página
        mViewPager.setPagingEnabled(true);

        //Chama a Busca geral
        OpenFragmentCanif open = new OpenFragmentCanif(tabLayout.getSelectedTabPosition());
        open.openFragment(manager,DefConversor.BUSCA,null);
    }

    private void chamaConversaoTodasUnidades() {
        botaoFlutuante = View.INVISIBLE;

        //Chama o fragment Converte Todas as Unidades
        CONVERSOR_VIGENTE_TAG = DefConversor.TODAS_UNIDADES;

        //Faz o link dos campos de tela com as variáveis
        TextView tvDe = (TextView) findViewById(R.id.tvDe);
        EditText etMontante = (EditText) findViewById(R.id.etMontante);

        String _montante = "";

        if (Locale.getDefault().getLanguage().equals("pt")){
            _montante = etMontante.getText().toString().replaceFirst("\\s", "").replace(".", "").replace(",", ".").trim();
        } else {
            _montante = etMontante.getText().toString().replaceFirst("\\s", "").replace(",", "").trim();
        }

        //Passa os parâmetros para o fragment da seleção de Unidade
        args.putCharSequence("siglaPriUnidade", tvDe.getText().toString());
        args.putDouble("Montante", Double.valueOf(_montante));

        //Passa as cotações de moeda para o fragment de moeda
        args.putSerializable(ListaCotacoes.KEY, new ListaCotacoes(lc.cotacoes));

        //Esconde o Botão de Conversão de Todas as Unidades
        menuConversaoTodasUnidades = false;
        menu.findItem(R.id.action_convert_all).setVisible(false);

        OpenFragmentCanif open = new OpenFragmentCanif(1);
        open.openFragment(manager, CONVERSOR_VIGENTE_TAG, args);
    }

    private void chamaReporte() {
        botaoFlutuante = View.VISIBLE;
        //Chama o fragment Reporte
        OpenFragmentCanif open = new OpenFragmentCanif(tabLayout.getSelectedTabPosition());
        open.openFragment(manager,DefConversor.REPORTE,null);
    }

    private void chamaConfiguracoes() {
        botaoFlutuante = View.INVISIBLE;
        //Chama o fragment de Configurações ou Preferences do Aplicativo
        OpenFragmentCanif open = new OpenFragmentCanif(tabLayout.getSelectedTabPosition());
        open.openFragment(manager, DefConversor.DIALOG_CONFIGURACAO, null);
    }

    private void showButtonHome(CharSequence titulo) {
        if (getSupportActionBar() != null) {
            toggle.setDrawerIndicatorEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(titulo);
            toggle.syncState();
        }

        //Exibe a barra de tabs
        tabLayout.setVisibility(View.VISIBLE);

        //Habilita o swipe da página
        mViewPager.setPagingEnabled(true);
    }

    @Override
    public void chamaConversorDeMoeda(Bundle args) {

        botaoFlutuante = View.VISIBLE;
        //Chama o conversor de Moeda
        OpenFragmentCanif open = new OpenFragmentCanif(1);
        open.openFragment(manager, DefConversor.MOEDA, args);
    }

    @Override
    public void chamaListaDeMoeda(Bundle args) {
        botaoFlutuante = View.INVISIBLE;
        //Chama o fragment da seleção de moeda
        OpenFragmentCanif open = new OpenFragmentCanif(1);
        open.openFragment(manager, DefConversor.LISTA_MOEDA, args);
    }

    @Override
    public void chamaConversorDeUnidade(Bundle args) {
        botaoFlutuante = View.VISIBLE;
        //Chama o fragment da seleção de unidade
        OpenFragmentCanif open = new OpenFragmentCanif(1);
        open.openFragment(manager, CONVERSOR_VIGENTE_TAG, args);
    }

    @Override
    public void chamaListaDeUnidade(Bundle args) {

        botaoFlutuante = View.INVISIBLE;

        String[] colecao = getResources().getStringArray(R.array.titulo_conversores);
        String TAG = colecao[CONVERSOR_VIGENTE_ID];

        //Chama o fragment da seleção de unidade
        OpenFragmentCanif open = new OpenFragmentCanif(1);
        open.openListFragment(manager, DefConversor.PREFIXO_LISTA + TAG, args);
    }

    @Override
    public void chamaConversorListFragment() {
        botaoFlutuante = View.INVISIBLE;
        if (mSavedInstanceState == null) showConvertersList(op_Add);
    }

    @Override
    public void atualizaTitulo(String titulo) {
        if (getSupportActionBar() != null) {
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(titulo);
            toggle.syncState();
        }

        //Esconde a barra de tabs
        tabLayout.setVisibility(View.GONE);

        //Desabilita o swipe da página
        mViewPager.setPagingEnabled(false);
    }

    @Override
    public void exibeBotaoFlutuante(int visibilidade) {
        botaoFlutuante = visibilidade;

        if (visibilidade == View.VISIBLE)
           fab.show();
        else
           fab.hide();
    }

    @Override
    public void ocultaBotaoFlutuanteSimcalc() {
        botaoFlutuante = View.INVISIBLE;
        fab.hide();
    }

    @Override
    public void defineBotaoTelaReporte() {
        fab.setImageResource(R.mipmap.ic_send);
    }

    @Override
    public void exibeDialogoCasasDecimais() {
        botaoFlutuante = View.INVISIBLE;
        FragmentTransaction ft = manager.beginTransaction();
        DialogoCasasDecimais dialogoCasasDecimais = new DialogoCasasDecimais();
        dialogoCasasDecimais.show(ft, DefConversor.DIALOG_CASAS_DECIMAIS);
    }

    @Override
    public void exibeOuNaoMenuConversaoTodas(boolean valor) {
        if (menu != null) {
            menuConversaoTodasUnidades = valor;
            menu.findItem(R.id.action_convert_all).setVisible(valor);
        }
    }

    @Override
    public void exibeBotaoHome(String titulo) {
        showButtonHome(titulo);
    }

    @Override
    public void chamaSobreConf() {
        chamaSobre();
    }

    @Override
    public void chamaReporteConf() {
        chamaReporte();
    }

    @Override
    public String getCONVERSOR_VIGENTE_TAG() {
        return CONVERSOR_VIGENTE_TAG;
    }

    @Override
    public void setCONVERSOR_VIGENTE_TAG(String tag) {
        CONVERSOR_VIGENTE_TAG = tag;
    }

    @Override
    public int getCONVERSOR_VIGENTE_ID() {
        return CONVERSOR_VIGENTE_ID;
    }

    @Override
    public int getSelectedTabPosition() { return tabLayout.getSelectedTabPosition(); }

    @Override
    public void chamaCalculoAntecipadoSelecionado(int posicao) {
        //Informa que está sendo chamado uma tela de cálculo
        telaCalculo = true;
        OpenFragmentCalculo open = new OpenFragmentCalculo();

        if (posicao == 0)  //Antecipação de dívida de valor único
            open.openFragment(manager, DefSimcalc.ANTECIPACAO_1);

        if (posicao == 1)   //Antecipação de dívida financiada
            open.openFragment(manager, DefSimcalc.ANTECIPACAO_2);
    }

    @Override
    public void chamaCalculoEmprestimoSelecionado(int posicao) {
        //Informa que está sendo chamado uma tela de cálculo
        telaCalculo = true;
        OpenFragmentCalculo open = new OpenFragmentCalculo();

        if (posicao == 0)  //Empréstimo - Cálculo do capital
            open.openFragment(manager, DefSimcalc.EMPRESTIMO_1);

        if (posicao == 1)  //Empréstimo - Cálculo da taxa de juros
            open.openFragment(manager, DefSimcalc.EMPRESTIMO_2);

        if (posicao == 2)  //Empréstimo - Cálculo do período
            open.openFragment(manager, DefSimcalc.EMPRESTIMO_3);

        if (posicao == 3)  //Empréstimo - Cálculo do montante
            open.openFragment(manager, DefSimcalc.EMPRESTIMO_4);

    }

    @Override
    public void chamaCalculoFinanciamentoSelecionado(int posicao) {
        //Informa que está sendo chamado uma tela de cálculo
        telaCalculo = true;
        OpenFragmentCalculo open = new OpenFragmentCalculo();

        if (posicao == 0)  //Financiamento - Cálculo do valor financiado
            open.openFragment(manager, DefSimcalc.FINANCIAMENTO_1);

        if (posicao == 1)  //Financiamento - Cálculo da taxa de juros
            open.openFragment(manager, DefSimcalc.FINANCIAMENTO_2);

        if (posicao == 2)  //Financiamento - Cálculo do período
            open.openFragment(manager, DefSimcalc.FINANCIAMENTO_3);

        if (posicao == 3)  //Financiamento - Cálculo da parcela
            open.openFragment(manager, DefSimcalc.FINANCIAMENTO_4);

    }

    @Override
    public void chamaCalculoEntradaVeiculoSelecionado(int posicao) {
        //Informa que está sendo chamado uma tela de cálculo
        telaCalculo = true;
        OpenFragmentCalculo open = new OpenFragmentCalculo();

        if (posicao == 0) //Financiamento - Cálculo da entrada de compra de veiculo
           open.openFragment(manager, DefSimcalc.FINANCIAMENTO_VEICULO);

        if (posicao == 1)  //Financiamento - Cálculo da parcela do financiamento do veiculo
            open.openFragment(manager, DefSimcalc.FINANCIAMENTO_VEICULO_P);
    }

    @Override
    public void chamaCalculoAplicacaoSelecionado(int posicao) {
        //Informa que está sendo chamado uma tela de cálculo
        telaCalculo = true;
        OpenFragmentCalculo open = new OpenFragmentCalculo();

        if (posicao == 0)  //Aplicação - Cálculo do valor futuro
            open.openFragment(manager, DefSimcalc.APLICACAO_1);

        if (posicao == 1)  //Aplicação - Cálculo do capital
            open.openFragment(manager, DefSimcalc.APLICACAO_2);

        if (posicao == 2)  //Aplicação - Cálculo do periodo
            open.openFragment(manager, DefSimcalc.APLICACAO_3);

        if (posicao == 3)  //Aplicação - Cálculo da taxa de juros
            open.openFragment(manager, DefSimcalc.APLICACAO_4);

    }

    @Override
    public void chamaCalculoCompraSelecionado(int posicao) {
        //Informa que está sendo chamado uma tela de cálculo
        telaCalculo = true;
        OpenFragmentCalculo open = new OpenFragmentCalculo();

        if (posicao == 0)  //Compra - Cálculo do valor do bem
            open.openFragment(manager, DefSimcalc.COMPRA_1);

        if (posicao == 1)  //Compra - Cálculo do valor dos depósitos mensais
            open.openFragment(manager, DefSimcalc.COMPRA_2);

        if (posicao == 2)  //Compra - Cálculo do período
            open.openFragment(manager, DefSimcalc.COMPRA_3);

        if (posicao == 3)  //Compra - Cálculo do valor inicial
            open.openFragment(manager, DefSimcalc.COMPRA_4);

    }

    @Override
    public void chamaCalculoRendaSelecionado(int posicao) {
        //Informa que está sendo chamado uma tela de cálculo
        telaCalculo = true;
        OpenFragmentCalculo open = new OpenFragmentCalculo();

        if (posicao == 0)  //Renda - Cálculo do retirada mensal
            open.openFragment(manager, DefSimcalc.RENDA_1);

        if (posicao == 1)  //Renda - Cálculo do período
            open.openFragment(manager, DefSimcalc.RENDA_2);

        if (posicao == 2)  //Renda - Cálculo do valor acumulado
            open.openFragment(manager, DefSimcalc.RENDA_3);

    }

    @Override
    public void chamaCalculoDinheiroSelecionado(int posicao) {
        //Informa que está sendo chamado uma tela de cálculo
        telaCalculo = true;
        OpenFragmentCalculo open = new OpenFragmentCalculo();

        if (posicao == 0)  //Juntar Dinheiro - Cálculo do valor acumulado
            open.openFragment(manager, DefSimcalc.DINHEIRO_1);

        if (posicao == 1)  //Juntar Dinheiro - Cálculo do depósito mensal
            open.openFragment(manager, DefSimcalc.DINHEIRO_2);

        if (posicao == 2)  //Juntar Dinheiro - Cálculo do período
            open.openFragment(manager, DefSimcalc.DINHEIRO_3);

        if (posicao == 3)  //Juntar Dinheiro - Cálculo do depósito inicial
            open.openFragment(manager, DefSimcalc.DINHEIRO_4);

    }

    @Override
    public int getSIMCALC_VIGENTE_ID() {
        return SIMCALC_VIGENTE_ID;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            //Menu main drawer - Configurações
            chamaConfiguracoes();

        } else if (id == R.id.nav_share) {

            //Menu main drawer - Compartilhe
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.titulo_assunto_email));
            sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.msg_compartilhe) + getString(R.string.app_address));
            sharingIntent.putExtra(Intent.EXTRA_EMAIL, "");
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.titulo_compartilhe)));

        } else if (id == R.id.nav_send) {

            //Menu main drawer - Opine
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto: " + getString(R.string.destinatario_feedback)));
            emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.titulo_assunto_email));
            startActivity(Intent.createChooser(emailIntent, getString(R.string.titulo_opine)));

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        //Passa as cotações de moeda para o fragment de moeda
        savedInstanceState.putSerializable(ListaCotacoes.KEY, new ListaCotacoes(funcoesHelper.getCotacoes()));
        savedInstanceState.putSerializable(MENU_CONVERTE_TODAS_KEY, menuConversaoTodasUnidades);
        savedInstanceState.putSerializable(TELA_CALCULO_KEY, telaCalculo);
        savedInstanceState.putSerializable(CONVERSOR_VIGENTE_ID_KEY, CONVERSOR_VIGENTE_ID);
        savedInstanceState.putSerializable(SIMCALC_VIGENTE_ID_KEY, SIMCALC_VIGENTE_ID);
        savedInstanceState.putSerializable(BOTAO_FLUTUANTE_KEY, botaoFlutuante);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void chamaSimcalcListFragment() {
        if (mSavedInstanceState == null) showSimcalcList(op_Add);
    }

    @Override
    public void chamaCalculoSelecionado(int posicao) {
        //Informa que está sendo chamado uma lista de cálculo
        telaCalculo = false;

        //Atualiza o ID do Simcalc vigente ou selecionado
        SIMCALC_VIGENTE_ID = posicao;

        //Chama o conversor de Unidade correspondente
        String[] colecao = this.getResources().getStringArray(R.array.calculos_titulo);
        String SIMCALC_VIGENTE_TAG = colecao[posicao];
        OpenFragmentCanif open = new OpenFragmentCanif(0);
        open.openFragment(manager, SIMCALC_VIGENTE_TAG,null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (toggle != null) {
            toggle.onConfigurationChanged(newConfig);
        }

        if (tabLayout.getSelectedTabPosition() == 0)
            if (menu != null) menu.findItem(R.id.action_convert_all).setVisible(false);

        if (tabLayout.getSelectedTabPosition() == 1) {
            if (menu != null) menu.findItem(R.id.action_convert_all).setVisible(menuConversaoTodasUnidades);
            //if (botaoFlutuante == View.VISIBLE) fab.setVisibility(botaoFlutuante);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (toggle != null) {
            toggle.syncState();
        }
    }

    @Override
    public void chamaAppSelecionado(String tag) {
        OpenFragmentCalculo open = new OpenFragmentCalculo();
        OpenFragmentCanif open_ = new OpenFragmentCanif(1);

        menu.removeItem(R.menu.search_menu);

        switch (tag.trim()) {
            case "DefConversor.ACELERACAO":
                mViewPager.setCurrentItem(1);
                tabLayout.setupWithViewPager(mViewPager);
                CONVERSOR_VIGENTE_ID = 0;
                open_.openFragment(manager,DefConversor.ACELERACAO,null);
                break;

            case "DefConversor.AREA":
                mViewPager.setCurrentItem(1);
                tabLayout.setupWithViewPager(mViewPager);
                CONVERSOR_VIGENTE_ID = 1;
                open_.openFragment(manager,DefConversor.AREA,null);
                break;

            case "DefConversor.COMBUSTIVEL":
                mViewPager.setCurrentItem(1);
                tabLayout.setupWithViewPager(mViewPager);
                CONVERSOR_VIGENTE_ID = 2;
                open_.openFragment(manager,DefConversor.COMBUSTIVEL,null);
                break;

            case "DefConversor.COMPRIMENTO":
                mViewPager.setCurrentItem(1);
                tabLayout.setupWithViewPager(mViewPager);
                CONVERSOR_VIGENTE_ID = 3;
                open_.openFragment(manager,DefConversor.COMPRIMENTO,null);
                break;

            case "DefConversor.MASSA":
                mViewPager.setCurrentItem(1);
                tabLayout.setupWithViewPager(mViewPager);
                CONVERSOR_VIGENTE_ID = 4;
                open_.openFragment(manager,DefConversor.MASSA,null);
                break;

            case "DefConversor.MOEDA":
                mViewPager.setCurrentItem(1);
                tabLayout.setupWithViewPager(mViewPager);
                CONVERSOR_VIGENTE_ID = 5;
                args.putSerializable(ListaCotacoes.KEY, new ListaCotacoes(lc.cotacoes));
                open_.openFragment(manager,DefConversor.MOEDA,args);
                break;

            case "DefConversor.TAXAS":
                mViewPager.setCurrentItem(1);
                tabLayout.setupWithViewPager(mViewPager);
                CONVERSOR_VIGENTE_ID = 6;
                open_.openFragment(manager,DefConversor.TAXAS,null);
                break;

            case "DefConversor.TEMPERATURA":
                mViewPager.setCurrentItem(1);
                tabLayout.setupWithViewPager(mViewPager);
                CONVERSOR_VIGENTE_ID = 7;
                open_.openFragment(manager,DefConversor.TEMPERATURA,null);
                break;

            case "DefConversor.VELOCIDADE":
                mViewPager.setCurrentItem(1);
                tabLayout.setupWithViewPager(mViewPager);
                CONVERSOR_VIGENTE_ID = 8;
                open_.openFragment(manager,DefConversor.VELOCIDADE,null);
                break;

            case "DefConversor.VOLUME":
                mViewPager.setCurrentItem(1);
                tabLayout.setupWithViewPager(mViewPager);
                CONVERSOR_VIGENTE_ID = 9;
                open_.openFragment(manager,DefConversor.VOLUME,null);
                break;

            case "DefSimcalc.ANTECIPACAO_1":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 0;
                open.openFragment(manager, DefSimcalc.ANTECIPACAO_1);
                break;

            case "DefSimcalc.ANTECIPACAO_2":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 0;
                open.openFragment(manager, DefSimcalc.ANTECIPACAO_2);
                break;

            case "DefSimcalc.EMPRESTIMO_1":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 1;
                open.openFragment(manager, DefSimcalc.EMPRESTIMO_1);
                break;

            case "DefSimcalc.EMPRESTIMO_2":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 1;
                open.openFragment(manager, DefSimcalc.EMPRESTIMO_2);
                break;

            case "DefSimcalc.EMPRESTIMO_3":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 1;
                open.openFragment(manager, DefSimcalc.EMPRESTIMO_3);
                break;

            case "DefSimcalc.EMPRESTIMO_4":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 1;
                open.openFragment(manager, DefSimcalc.EMPRESTIMO_4);
                break;

            case "DefSimcalc.FINANCIAMENTO_1":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 2;
                open.openFragment(manager, DefSimcalc.FINANCIAMENTO_1);
                break;

            case "DefSimcalc.FINANCIAMENTO_2":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 2;
                open.openFragment(manager, DefSimcalc.FINANCIAMENTO_2);
                break;

            case "DefSimcalc.FINANCIAMENTO_3":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 2;
                open.openFragment(manager, DefSimcalc.FINANCIAMENTO_3);
                break;

            case "DefSimcalc.FINANCIAMENTO_4":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 2;
                open.openFragment(manager, DefSimcalc.FINANCIAMENTO_4);
                break;

            case "DefSimcalc.FINANCIAMENTO_VEICULO":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 3;
                open.openFragment(manager, DefSimcalc.FINANCIAMENTO_VEICULO);
                break;

            case "DefSimcalc.APLICACAO_1":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 4;
                open.openFragment(manager, DefSimcalc.APLICACAO_1);
                break;

            case "DefSimcalc.APLICACAO_2":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 4;
                open.openFragment(manager, DefSimcalc.APLICACAO_2);
                break;

            case "DefSimcalc.APLICACAO_3":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 4;
                open.openFragment(manager, DefSimcalc.APLICACAO_3);
                break;

            case "DefSimcalc.APLICACAO_4":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 4;
                open.openFragment(manager, DefSimcalc.APLICACAO_4);
                break;

            case "DefSimcalc.COMPRA_1":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                open.openFragment(manager, DefSimcalc.COMPRA_1);
                break;

            case "DefSimcalc.COMPRA_2":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 5;
                open.openFragment(manager, DefSimcalc.COMPRA_2);
                break;

            case "DefSimcalc.COMPRA_3":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 5;
                open.openFragment(manager, DefSimcalc.COMPRA_3);
                break;

            case "DefSimcalc.COMPRA_4":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 5;
                open.openFragment(manager, DefSimcalc.COMPRA_4);
                break;

            case "DefSimcalc.RENDA_1":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 6;
                open.openFragment(manager, DefSimcalc.RENDA_1);
                break;

            case "DefSimcalc.RENDA_2":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 6;
                open.openFragment(manager, DefSimcalc.RENDA_2);
                break;

            case "DefSimcalc.RENDA_3":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 6;
                open.openFragment(manager, DefSimcalc.RENDA_3);
                break;

            case "DefSimcalc.DINHEIRO_1":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 7;
                open.openFragment(manager, DefSimcalc.DINHEIRO_1);
                break;

            case "DefSimcalc.DINHEIRO_2":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 7;
                open.openFragment(manager, DefSimcalc.DINHEIRO_2);
                break;

            case "DefSimcalc.DINHEIRO_3":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 7;
                open.openFragment(manager, DefSimcalc.DINHEIRO_3);
                break;

            case "DefSimcalc.DINHEIRO_4":
                mViewPager.setCurrentItem(0);
                tabLayout.setupWithViewPager(mViewPager);
                SIMCALC_VIGENTE_ID = 7;
                open.openFragment(manager, DefSimcalc.DINHEIRO_4);
                break;
        }
    }

}
