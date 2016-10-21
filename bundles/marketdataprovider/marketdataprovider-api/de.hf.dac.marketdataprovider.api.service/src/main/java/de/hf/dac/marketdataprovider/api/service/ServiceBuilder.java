/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ServiceBuilder.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 21.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.service;

import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;

public interface ServiceBuilder {
    ProductService buildProductService(ProductDao productDao);
}
