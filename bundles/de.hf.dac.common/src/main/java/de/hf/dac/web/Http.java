/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : Http.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 26.01.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.Properties;

public class Http {

    private final int timeOut;

    public Http(int timeOut){
        this.timeOut=timeOut;
    }

    public String getRequest(String url) throws IOException {
        return getRequest( url, false, null, 0, null, null);
    }

    /**
     * Get Data from an URL
     *
     * option -Djdk.http.auth.tunneling.disabledSchemes="" have to be set to empty at startup (e.G: in karaf_local.bat, karaf or in java command )
     * only http is working otherwise. https do not get the credentials
     * @param url
     * @param useProxy
     * @param proxyUrl
     * @param proxyPort
     * @param proxyUser
     * @param proxyPw
     * @return
     * @throws IOException
     */
    public String getRequest(String url, Boolean useProxy, String proxyUrl, int proxyPort, String proxyUser, String proxyPw) throws IOException {
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

        connection.setConnectTimeout(timeOut);
        connection.setReadTimeout(timeOut);

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
