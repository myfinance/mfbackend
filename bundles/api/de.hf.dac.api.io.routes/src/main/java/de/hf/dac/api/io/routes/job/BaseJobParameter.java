/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseJobParameter.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 28.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes.job;

import java.util.Date;

public abstract class BaseJobParameter implements JobParameter {

    protected String routingID;
    protected String resultRoutingID;
    protected String uid;
    protected final Date start;

    public BaseJobParameter(String uuid, String routingID, String resultRoutingID) {
        this.routingID = routingID;
        this.resultRoutingID = resultRoutingID;
        this.start = new Date();
        this.uid = uuid;
    }

    @Override
    public final String getRoutingID() {
        return routingID;
    }

    @Override
    public final String getResultRoutingID() {
        return resultRoutingID;
    }

    @Override
    public final Date getStart() {
        return start;
    }

    public final void setRoutingID(String routingID) {
        this.routingID = routingID;
    }

    public final void setResultRoutingID(String resultRoutingID) {
        this.resultRoutingID = resultRoutingID;
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }
}
