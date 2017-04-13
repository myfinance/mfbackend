/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : IdentifiableSecuredResource.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 13.04.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.security;

public interface IdentifiableSecuredResource<RESOURCE_LEVEL>  extends IdentifiableResource<RESOURCE_LEVEL>, Secured{
    IdentifiableSecuredResource<RESOURCE_LEVEL> getParent();
}
