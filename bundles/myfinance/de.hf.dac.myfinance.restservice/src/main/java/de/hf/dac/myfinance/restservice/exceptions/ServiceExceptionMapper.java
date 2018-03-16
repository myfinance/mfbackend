/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ServiceExceptionMapper.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.01.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.restservice.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ServiceExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable ex) {
        StringWriter errorStackTrace = new StringWriter();
        ex.printStackTrace(new PrintWriter(errorStackTrace));
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorStackTrace.toString())
            .type(MediaType.TEXT_PLAIN).build();
    }
}
