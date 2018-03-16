/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : SecurityEnvProviderServiceImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 19.04.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.application.securitycontext;

import de.hf.dac.myfinance.api.application.securitycontext.SecurityEnvProviderService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

@Designate(ocd = RootSecurityEnvironmentConfiguration.class)
@Component(service =  {SecurityEnvProviderService.class},immediate = true, name="DAC.SecurityEnvProviderService")
public class SecurityEnvProviderServiceImpl implements SecurityEnvProviderService{
    private String  securityEnv;

    @Activate
    private void activate(RootSecurityEnvironmentConfiguration securityEnv) {
        this.securityEnv = securityEnv.sourceEnvironmentForSecurityDB();
    }

    @Override
    public String getSecurityEnvironment() {
        return securityEnv;
    }
}
