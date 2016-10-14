/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ProductDao.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 11.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.persistence.dao;

import de.hf.dac.marketdataprovider.api.domain.Product;

import java.util.List;

public interface ProductDao {
    List<Product> listProductsNamedQuery();
    String getProductDescriptionWithoutRepo(String productId);
    List<Product> getProductsWithoutRepo();
    List<Product> listProducts();
    void saveProduct(Product product);
    void saveProducts(List<Product> products);
    void deleteAll();
    void delete(Product product);
    void deleteFast(String productId);
    Product getProduct(String productId);
}
