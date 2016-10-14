/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DaoBuilder.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 11.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.persistence;

import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;

import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;

public interface DaoBuilder {
    ProductDao buildProductDao(String env) throws SQLException;
}
