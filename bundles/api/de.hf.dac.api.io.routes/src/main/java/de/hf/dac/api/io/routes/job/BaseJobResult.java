/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseJobResult.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes.job;

import java.util.Date;

public class BaseJobResult<T> extends BaseRoutable implements JobResult {

    private final T resultData;
    private Date start;
    private Date finish;
    private String message;

    public BaseJobResult(String uuid, String routingID, T resultData) {
        super(uuid, routingID);
        this.resultData = resultData;
        setMessage(this.resultData != null ? resultData.toString() : null);
    }

    private void setMessage(String s) {
        this.message = s;
    }

    public BaseJobResult(String uid, String routingID) {
        this(uid, routingID, null);
    }

    @Override
    public String toString() {
        return "BaseJobResult{" + "resultData=" + resultData + '}';
    }

    @Override
    public Date getStart() {
        return start;
    }

    @Override
    public Date getFinish() {
        return finish;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public T getResultData() {
        return resultData;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setFinish(Date finish) {
        this.finish = finish;
    }
}

