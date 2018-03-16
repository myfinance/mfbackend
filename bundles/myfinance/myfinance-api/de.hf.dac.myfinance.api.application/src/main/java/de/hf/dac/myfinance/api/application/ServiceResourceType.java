/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ServiceResourceType.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 17.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.application;

import de.hf.dac.api.security.IdentifiableSecuredResource;

public interface ServiceResourceType extends IdentifiableSecuredResource<OpLevel> {
    ServiceResourceType getChildServiceContext(String id);
}
