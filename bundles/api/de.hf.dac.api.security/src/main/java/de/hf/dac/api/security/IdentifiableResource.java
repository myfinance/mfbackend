/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : IdentifiableResource.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 30.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.security;

import java.util.List;

public interface IdentifiableResource<RESOURCE_LEVEL> {
    String getId();

    List<String> getParentIdTrail();

    RESOURCE_LEVEL getOpLevel();
}
