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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author surak
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> listProducts() {
        Product p1 = new Product("Product 1 desc");
        p1.setProductId("1234");
        Product p2 = new Product("Product 2 desc");
        p1.setProductId("1235");
        ArrayList<Product> products = new ArrayList<>(2);
        products.add(p1);
        products.add(p2);

        productRepository.save(products);


        //fetch from DB
        Product fetchedProduct = productRepository.findOne(p1.getId());
        log.debug("test:"+fetchedProduct.getId());

        return products;
    }
}
