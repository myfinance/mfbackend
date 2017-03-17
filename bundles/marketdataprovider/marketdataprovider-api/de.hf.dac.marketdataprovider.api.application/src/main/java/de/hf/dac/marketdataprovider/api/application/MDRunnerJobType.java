/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MDRunnerJobType.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.application;

import de.hf.dac.api.security.Secured;

public interface MDRunnerJobType extends ServiceResourceType, Secured {
    String getBeanClass();
}
