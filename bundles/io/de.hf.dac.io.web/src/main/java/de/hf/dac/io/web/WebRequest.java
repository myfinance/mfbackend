/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : WebRequest.java
 * Author(s)   : xn01598
 * Created     : 26.10.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class WebRequest {

    boolean useProxy;
    String proxyUrl;
    int proxyPort;
    String proxyUser;
    String proxyPw;
    int timeout;

    public WebRequest(){
        this.useProxy=false;
        this.timeout=10000;
    }

    public WebRequest(boolean useProxy, String proxyUrl, int proxyPort, String proxyUser, String proxyPw, int timeout){
        this.useProxy=useProxy;
        this.proxyUrl=proxyUrl;
        this.proxyPort=proxyPort;
        this.proxyUser=proxyUser;
        this.proxyPw=proxyPw;
        this.timeout=timeout;
    }

    public String getRequest(String url) throws IOException {

        InputStreamReader inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder responseBuilder;

        URLConnection connection = getUrlConnection(url);

        try {
            inputStream = new InputStreamReader(connection.getInputStream(), "UTF-8");
            bufferedReader = new BufferedReader(inputStream);
            responseBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseBuilder.append(line);
            }

        } finally {
            if(inputStream!=null) inputStream.close();
            if(bufferedReader!=null) bufferedReader.close();
        }
        return responseBuilder.toString();
    }

    public String deleteRequest(String url) throws IOException {

        InputStreamReader inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder responseBuilder;

        HttpURLConnection connection = (HttpURLConnection)getUrlConnection(url);

        connection.setRequestMethod( "DELETE" );
        connection.setRequestProperty( "Content-Type", "application/json");
        try {
            inputStream = new InputStreamReader(connection.getInputStream(), "UTF-8");
            bufferedReader = new BufferedReader(inputStream);
            responseBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseBuilder.append(line);
            }

        } finally {
            if(inputStream!=null) inputStream.close();
            if(bufferedReader!=null) bufferedReader.close();
        }
 
        /*conn.setDoOutput( true );
        conn.setInstanceFollowRedirects( false );
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        conn.setUseCaches( false );*/
        return responseBuilder.toString();
    }

    private URLConnection getUrlConnection(String url) throws IOException {
        URL request = new URL(url);
        URLConnection connection;

        if(useProxy) {

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl, proxyPort));

            Authenticator authenticator = new Authenticator() {

                public PasswordAuthentication getPasswordAuthentication() {
                    return (new PasswordAuthentication(proxyUser, proxyPw.toCharArray()));
                }
            };
            Authenticator.setDefault(authenticator);

            connection = request.openConnection(proxy);

        } else {
            connection = request.openConnection();
        }

        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        return connection;
    }
}
