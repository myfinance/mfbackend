/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : ProductServiceImpl.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 20.05.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.service;

import de.hf.marketdataprovider.domain.Product;
import de.hf.marketdataprovider.persistence.repositories.ProductRepository;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

/**
 *
 * @author surak
 */
@Slf4j
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Inject
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> listProducts() {
        Product p1 = new Product("1234", "Product 1 desc");
        Product p2 = new Product("1235", "Product 2 desc");
        ArrayList<Product> products = new ArrayList<>(2);
        products.add(p1);
        products.add(p2);

        productRepository.save(products);


        //fetch from DB
        //Product fetchedProduct = productRepository.findOne(p1.getId());
        //log.debug("test:"+fetchedProduct.getId());

        return products;
    }
}
