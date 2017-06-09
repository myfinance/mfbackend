/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : JobResult.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.io.routes.job;

import java.util.Date;

public interface JobResult extends Routable {
    Date getStart();

    Date getFinish();

    String getMessage();
}
