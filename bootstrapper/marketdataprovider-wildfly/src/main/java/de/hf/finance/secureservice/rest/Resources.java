/** ----------------------------------------------------------------------------
 *
 * ---                                ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : Resources.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 17.06.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.finance.secureservice.rest;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Resources {
    @Produces
    @Dependent
    @PersistenceContext(unitName = "primary")
    private EntityManager em;

}
