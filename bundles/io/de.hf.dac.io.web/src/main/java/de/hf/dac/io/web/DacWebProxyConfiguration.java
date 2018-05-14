/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : DacWebProxyConfiguration.java
 * Author(s)   : hf
 * Created     : 11.05.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.web;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Dac Web Proxy Configuration")
public @interface DacWebProxyConfiguration {
    int proxy_timeout() default 30000;
    boolean proxy_on() default false;
    String proxy_url();
    int proxy_port();
    String proxy_user();
    String proxy_pw();
}
