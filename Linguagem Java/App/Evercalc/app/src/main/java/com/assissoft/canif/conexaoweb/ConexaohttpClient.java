package com.assissoft.canif.conexaoweb;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Marcos on 18/05/2016.
 *
 */
public class ConexaohttpClient {

    private static HttpClient httpClient; // CRIA UMA INSTÂNCIA DA CLASSE HttpClient;

    private static HttpClient getHttpClient() {
        if(httpClient == null) {
            httpClient = new DefaultHttpClient();
        }
        return httpClient;
    }

    public static String executaHttpGet(String url) throws Exception{
        BufferedReader bufferedReader = null;
        try{
            HttpClient client = getHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = client.execute(httpGet);
            bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder("");
            String line;
            String LS = System.getProperty("line.separator");
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line).append(LS);
            }
            bufferedReader.close();
            return stringBuffer.toString();
        } finally {
            if (bufferedReader != null){
                try{
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    //Log.i("ConexaohttpClient", e.toString());
                }
            }
        }

    }


}
