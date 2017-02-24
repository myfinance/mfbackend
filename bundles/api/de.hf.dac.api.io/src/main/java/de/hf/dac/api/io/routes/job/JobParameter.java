/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : JobParameter.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes.job;

import java.util.Date;

/**
 * Base interface for all jobs transmitted using the camel routing infrastructure
 * in order to trigger custom Job Handler
 */
public interface JobParameter extends Routable {
    /** Routing ID for the Result Instance */
    String getResultRoutingID();

    Date getStart();
}
