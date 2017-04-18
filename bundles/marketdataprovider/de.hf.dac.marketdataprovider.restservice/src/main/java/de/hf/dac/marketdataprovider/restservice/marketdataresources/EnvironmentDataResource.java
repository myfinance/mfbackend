/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : EnvironmentDataResource.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 25.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.restservice.marketdataresources;

import com.google.gson.Gson;
import de.hf.dac.api.security.SecuredResource;
import de.hf.dac.marketdataprovider.api.application.MarketDataEnvironment;
import de.hf.dac.marketdataprovider.api.application.OpLevel;
import de.hf.dac.marketdataprovider.api.application.OpType;
import de.hf.dac.marketdataprovider.api.application.servicecontext.MDEnvironmentContext;
import de.hf.dac.marketdataprovider.api.domain.Instrument;
import io.swagger.annotations.ApiOperation;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class EnvironmentDataResource extends SecuredResource<OpType,OpLevel> {

    MarketDataEnvironment marketDataEnvironment;
    final String OPERATIONID="environment";
    final protected static Gson gson = new Gson();

    public EnvironmentDataResource(MDEnvironmentContext envContext) {
        super(envContext);
        this.marketDataEnvironment = envContext.getMarketDataEnvironment();
    }

    @GET
    @Path("/instruments")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get Instruments",
        response = List.class)
    public List<Instrument> getInstruments() {
        checkOperationAllowed(OpType.READ, OPERATIONID);
        List<Instrument> returnvalue = marketDataEnvironment.getInstrumentService().listInstruments();
        return returnvalue;
    }
}
