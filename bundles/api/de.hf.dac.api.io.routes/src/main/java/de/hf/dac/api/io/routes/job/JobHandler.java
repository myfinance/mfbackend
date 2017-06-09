/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : JobHandler.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes.job;

/**
 * Job Handler need to implement this to process incoming jobs
 * @param <JOB_TYPE>
 */
public interface JobHandler<JOB_TYPE extends JobParameter> {

    public static String JOB_HANDLER_TARGET_PROPERTY = "job.handler.target=JOBS";

    /**
     * Return the String version of RoutingID
     * @return
     */
    String[] getRoutingIDs();

    /**
     * Return the name of the Camel Inbox for this RoutingID
     * @return name of inbox
     */
    String getInbox(String routingID);

    /** Notify once unbound from Service omponent */
    void postBind();

    /** Notify once unbound from Service omponent */
    void postUnBind();
}
