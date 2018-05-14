/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : WebRequestService.java
 * Author(s)   : xn01598
 * Created     : 14.05.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.web;

import java.io.IOException;

public interface WebRequestService {
    /**
     * Get Data from an URL
     *
     * option -Djdk.http.auth.tunneling.disabledSchemes="" have to be set to empty at startup (e.G: in karaf_local.bat, karaf or in java command )
     * only http is working otherwise. https do not get the credentials
     * @param url
     * @return
     * @throws IOException
     */
    String getRequest(String url) throws IOException;
}
