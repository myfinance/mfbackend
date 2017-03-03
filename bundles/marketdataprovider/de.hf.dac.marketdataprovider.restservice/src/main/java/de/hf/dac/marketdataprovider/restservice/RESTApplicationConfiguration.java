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

package de.hf.dac.marketdataprovider.restservice;


import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "MDDataService Service Configuration")
public @interface RESTApplicationConfiguration {
    String serverAddress() default "/rest";

    String authRealm() default "karaf";

    String jaasContext() default "karaf";

    String swaggerBasePath() default "/dac/rest";

    String swaggerDescription() default "Dac Service REST API";

    String swaggerTitle() default "Dac Services";

    String swaggerContact() default "HF";

    String swaggerVersion() default "1.1";

    String corsAllowedHeaders() default "Origin,Accept,Authorization,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers";

    String corsAllowedOrigins() default "*";

    String corsAllowedMethods() default "GET,POST,DELETE,PUT,OPTIONS";
}
