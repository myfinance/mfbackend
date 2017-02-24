/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : Routable.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes.job;

import java.io.Serializable;

public interface Routable extends Serializable {
    /** Defines the Routing ID for the new job */
    String getRoutingID();

    String getUid();
    void setUid(String uid);

}
