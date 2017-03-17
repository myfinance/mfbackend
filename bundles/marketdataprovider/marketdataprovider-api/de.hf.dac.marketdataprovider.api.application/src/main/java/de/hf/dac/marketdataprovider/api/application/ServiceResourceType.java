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

package de.hf.dac.marketdataprovider.api.application;

import de.hf.dac.api.security.IdentifiableResource;
import de.hf.dac.api.security.Secured;

public interface ServiceResourceType extends IdentifiableResource<OpLevel>, Secured {
    ServiceResourceType getParent();
}
