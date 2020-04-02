/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RESTApplicationConfiguration.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.01.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.restservice;


import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "MyFinanceDataService Service Configuration")
public @interface RESTApplicationConfiguration {
    String serverAddress() default "/rest";

    String authRealm() default "karaf";

    String jaasContext() default "karaf";

    String swaggerBasePath() default "/dac/rest";

    String swaggerDescription() default "Dac Service REST API";

    String swaggerTitle() default "Dac Services";

    String swaggerContact() default "HF";

    String swaggerVersion() default "1.1";

    // If empty allow any header - if filled with comma separated origins allow only those
    String corsAllowedHeaders() default "";

    // If empty allow any origin - if filled with comma separated origins allow only those
    String corsAllowedOrigins() default "http://localhost:4300,http://localhost:4200,http://localhost:8080,http://myfinance.mylocaldomain/mff,https://babcom.myds.me,*";

    // If empty allow any methods - if filled with comma separated methods allow only those
    String corsAllowedMethods() default "GET,POST,DELETE,PUT,OPTIONS";
}
