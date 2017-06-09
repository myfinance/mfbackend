/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseRoutable.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes.job;

public class BaseRoutable implements Routable {

    private String routingID;
    private String uid;

    public BaseRoutable() {
    }

    public BaseRoutable(String uuid, String routingID) {
        this.uid = uuid;
        this.routingID = routingID;
    }

    @Override
    public String getRoutingID() {
        return this.routingID;
    }

    public void setRoutingID(String routingID) {
        this.routingID = routingID;
    }

    @Override
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
