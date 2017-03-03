/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseWrapper.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 28.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes.job;

import java.io.Serializable;

public class BaseWrapper extends BaseJobParameter implements Serializable {

    private final Serializable wrappedSerializable;

    public BaseWrapper(Serializable serializable, String uuid, String routingID, String resultRoutingId) {
        super(uuid, routingID, resultRoutingId);
        this.wrappedSerializable = serializable;
    }

    public Serializable getWrappedSerializable() {
        return wrappedSerializable;
    }


}
