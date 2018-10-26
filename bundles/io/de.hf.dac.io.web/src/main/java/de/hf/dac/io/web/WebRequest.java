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
    public String getRequest(String url, boolean useProxy, String proxyUrl, int proxyPort, String proxyUser, String proxyPw, int timeout) throws IOException {

        URL request = new URL(url);
        URLConnection connection;
        InputStreamReader inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder responseBuilder;

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
}
