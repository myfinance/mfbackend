/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentResourceImpl.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 25.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.restservice.marketdataresources;

import de.hf.dac.api.security.AuthorizationSubject;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.OpType;
import de.hf.dac.marketdataprovider.api.domain.Instrument;
import de.hf.dac.marketdataprovider.api.restservice.EnvironmentResource;
import de.hf.dac.security.restauthorization.SecuredResource;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class EnvironmentResourceImpl extends SecuredResource<OpType,OpLevel> implements EnvironmentResource {

    MarketDataEnvironment marketDataEnvironment;

    public EnvironmentResourceImpl(AuthorizationSubject authorization, MarketDataEnvironment marketDataEnvironment) {
        super(authorization, marketDataEnvironment);
        this.marketDataEnvironment = marketDataEnvironment;
    }

    @GET
    @Path("/instruments")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Instruments",
        response = List.class)
    public List<Instrument> getInstruments() {
        checkOperationAllowed(OpType.READ, marketDataEnvironment.getId());
        checkPassthroughAllowed();
        List<Instrument> returnvalue = marketDataEnvironment.getInstrumentService().listInstruments();
        return returnvalue;
    }
}
