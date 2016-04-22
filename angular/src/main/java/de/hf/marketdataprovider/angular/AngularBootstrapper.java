/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : AngularBootstrapper.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 22.04.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.angular;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AngularBootstrapper {
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(AngularBootstrapper.class, args);
    }
}
