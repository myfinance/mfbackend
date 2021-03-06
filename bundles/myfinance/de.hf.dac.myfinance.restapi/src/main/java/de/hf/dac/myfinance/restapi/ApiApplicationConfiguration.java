/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ApiApplicationConfiguration.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.01.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.restapi;


import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "MyFinanceDataService Service Configuration")
public @interface ApiApplicationConfiguration {
    String serverAddress() default "/api";

    String authRealm() default "karaf";

    String jaasContext() default "karaf";

    // If empty allow any header - if filled with comma separated origins allow only those
    String corsAllowedHeaders() default "";

    // If empty allow any origin - if filled with comma separated origins allow only those
    String corsAllowedOrigins() default "http://localhost:4300,http://localhost:4200,http://localhost:8080,http://prod.mffe.mylocaldomain,http://dev.mffe.mylocaldomain,https://babcom.myds.me,*";

    // If empty allow any methods - if filled with comma separated methods allow only those
    String corsAllowedMethods() default "GET,POST,DELETE,PUT,OPTIONS";
}
