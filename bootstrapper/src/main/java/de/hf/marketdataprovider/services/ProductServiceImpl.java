/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.services;

import de.hf.marketdataprovider.domain.Product;
import de.hf.marketdataprovider.repositories.ProductRepository;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author surak
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    
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
