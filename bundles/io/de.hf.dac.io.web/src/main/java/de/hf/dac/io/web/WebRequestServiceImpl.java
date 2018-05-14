/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : WebRequestServiceImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 26.01.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import javax.security.auth.login.AppConfigurationEntry;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import de.hf.dac.api.io.web.WebRequestService;

@Designate(ocd = DacWebProxyConfiguration.class)
@Component(immediate = true, name="DAC.WebRequestServiceImpl")
public class WebRequestServiceImpl implements WebRequestService {

    private DacWebProxyConfiguration dacWebProxyConfiguration;

    @Activate
    public void activate(BundleContext bc, DacWebProxyConfiguration dacWebProxyConfiguration)
    {
        this.dacWebProxyConfiguration=dacWebProxyConfiguration;
    }

    public WebRequestServiceImpl(){
    }

    public String getRequest(String url) throws IOException {

        URL request = new URL(url);
        URLConnection connection;
        InputStreamReader inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder responseBuilder;

        if(dacWebProxyConfiguration.proxy_on()) {

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(dacWebProxyConfiguration.proxy_url(), dacWebProxyConfiguration.proxy_port()));

            Authenticator authenticator = new Authenticator() {

                public PasswordAuthentication getPasswordAuthentication() {
                    return (new PasswordAuthentication(dacWebProxyConfiguration.proxy_user(), dacWebProxyConfiguration.proxy_pw().toCharArray()));
                }
            };
            Authenticator.setDefault(authenticator);

            connection = request.openConnection(proxy);

        } else {
            connection = request.openConnection();
        }

        connection.setConnectTimeout(dacWebProxyConfiguration.proxy_timeout());
        connection.setReadTimeout(dacWebProxyConfiguration.proxy_timeout());

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
