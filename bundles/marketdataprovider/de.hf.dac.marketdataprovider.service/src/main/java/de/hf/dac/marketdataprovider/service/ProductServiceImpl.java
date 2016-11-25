/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : ProductServiceImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 20.05.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.service;


import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.api.domain.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

/**
 *
 * @author hf
 */
@Slf4j
public class ProductServiceImpl implements ProductService {


    private ProductDao productDao;

    @Inject
    public ProductServiceImpl(ProductDao productDao){
        this.productDao = productDao;
    }

    @Override
    public List<Product> listProducts() throws SQLException {

        List products = productDao.listProducts();

        return products;
    }

    @Override
    public void saveProduct(Product product) throws SQLException {
        productDao.saveProduct(product);
    }

    @Override
    public List<Product> doSomeWork() throws SQLException {
        productDao.deleteAll();
        Product p = new Product("isin1", "singleProductAdded");
        productDao.saveProduct(p);
        List<Product> products = new ArrayList<>();
        products.add(new Product("isin2", "singleProductAdded1"));
        products.add(new Product("isin3", "singleProductAdded2"));
        products.add(new Product("isin4", "singleProductAdded3"));
        products.add(new Product("isin5", "singleProductAdded4"));
        products.add(new Product("isin6", "singleProductAdded5"));
        productDao.saveProducts(products);
        productDao.deleteFast("isin3");
        p = productDao.getProduct("isin4");
        if(p!=null) {
            productDao.delete(p);
        }
        String desc = productDao.getProductDescriptionWithoutRepo("isin6");
        List<Product> productResult1 = productDao.getProductsWithoutRepo();
        List<Product> productResult2 = productDao.listProducts();
        List<Product> productResult3 = productDao.listProductsNamedQuery();
        return productResult2;
    }
}
