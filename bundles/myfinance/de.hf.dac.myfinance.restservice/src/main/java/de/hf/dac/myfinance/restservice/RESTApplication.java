/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : RESTApplication.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.01.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.restservice;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import de.hf.dac.myfinance.restservice.exceptions.ServiceExceptionMapper;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.FastInfosetFeature;
import org.apache.cxf.interceptor.security.JAASAuthenticationFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.impl.WebApplicationExceptionMapper;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.provider.MultipartProvider;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharingFilter;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.apache.cxf.transport.http.HttpAuthenticationFaultHandler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import java.util.Arrays;

@Designate(ocd = RESTApplicationConfiguration.class)
@Component(
    service = RESTApplication.class,
    name = "MD.RestAplicationService",
    immediate = true
)
public class RESTApplication {

    private Server server = null;

    @Activate
    protected void activate(RESTApplicationConfiguration RESTApplicationConfiguration) {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(
            MyHello.class,
            MyFinanceDataService.class,
            MyFinanceRunnerService.class);
        sf.setResourceProvider(this.getClass(), new SingletonResourceProvider(this));
        sf.setAddress(RESTApplicationConfiguration.serverAddress());
        sf.setProvider(new JacksonJsonProvider());
        sf.setProvider(new MultipartProvider());

        {
            final HttpAuthenticationFaultHandler httpAuthenticationFaultHandler = new HttpAuthenticationFaultHandler();
            httpAuthenticationFaultHandler.setRealm(RESTApplicationConfiguration.authRealm());
            sf.getInInterceptors().add(httpAuthenticationFaultHandler);
        }
        {
            final JAASAuthenticationFeature jaasAuthenticationFeature = new JAASAuthenticationFeature();
            jaasAuthenticationFeature.setContextName(RESTApplicationConfiguration.jaasContext());
            jaasAuthenticationFeature.setReportFault(true);
            sf.getFeatures().add(jaasAuthenticationFeature);
        }
        {
            final Swagger2Feature swagger2Feature = new Swagger2Feature();
            swagger2Feature.setPrettyPrint(true);
            swagger2Feature.setBasePath(RESTApplicationConfiguration.swaggerBasePath());
            swagger2Feature.setDescription(RESTApplicationConfiguration.swaggerDescription());
            swagger2Feature.setTitle(RESTApplicationConfiguration.swaggerTitle());
            swagger2Feature.setContact(RESTApplicationConfiguration.swaggerContact());
            swagger2Feature.setVersion(RESTApplicationConfiguration.swaggerVersion());
            swagger2Feature.setScan(false);
            sf.getFeatures().add(swagger2Feature);
        }
        {
            sf.getFeatures().add(new FastInfosetFeature());
            sf.getFeatures().add(new GZIPFeature());
        }
        {
            WebApplicationExceptionMapper x =  new WebApplicationExceptionMapper();
            x.setAddMessageToResponse(true);
            x.setPrintStackTrace(true);
            sf.setProvider(new ServiceExceptionMapper());
            sf.setProvider(x);
        }
        {
            final CrossOriginResourceSharingFilter crossOriginResourceSharingFilter = new CrossOriginResourceSharingFilter();
            crossOriginResourceSharingFilter.setAllowCredentials(true);
            if(!RESTApplicationConfiguration.corsAllowedHeaders().isEmpty()) {
                crossOriginResourceSharingFilter.setAllowHeaders(Arrays.asList(RESTApplicationConfiguration.corsAllowedHeaders().split(",")));
            }

            /*if(!RESTApplicationConfiguration.corsAllowedOrigins().isEmpty()) {
                crossOriginResourceSharingFilter.setAllowOrigins(Arrays.asList(RESTApplicationConfiguration.corsAllowedOrigins().split(",")));
            }*/

            sf.setProvider(crossOriginResourceSharingFilter);
        }
        server = sf.create();
    }

    @Deactivate
    protected void deactivate() {
        if (server != null) {
            server.stop();
            server.destroy();
        }
    }

}
