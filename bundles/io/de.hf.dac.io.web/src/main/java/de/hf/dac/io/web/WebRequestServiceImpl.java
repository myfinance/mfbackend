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
        WebRequest request = new WebRequest(dacWebProxyConfiguration.proxy_on(),
            dacWebProxyConfiguration.proxy_url(),
            dacWebProxyConfiguration.proxy_port(),
            dacWebProxyConfiguration.proxy_user(),
            dacWebProxyConfiguration.proxy_pw(),
            dacWebProxyConfiguration.proxy_timeout());

        return request.getRequest(url);
    }
}
