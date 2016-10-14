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

import javax.inject.Inject;
import javax.inject.Singleton;

import de.hf.dac.marketdataprovider.api.persistence.DaoBuilder;
import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.api.domain.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

/**
 *
 * @author hf
 */
@Slf4j
@OsgiServiceProvider(classes = {ProductService.class})
@Singleton
public class ProductServiceImpl implements ProductService {

    @OsgiService
    @Inject
    protected DaoBuilder daoBuilder;

    @Override
    public List<Product> listProducts(String env) throws SQLException {

        ProductDao productDao = daoBuilder.buildProductDao(env);
        List products = productDao.listProducts();

        return products;
    }

    @Override
    public void saveProduct(Product product, String env) throws SQLException {
        ProductDao productDao = daoBuilder.buildProductDao(env);
        productDao.saveProduct(product);
    }

    @Override
    public List<Product> doSomeWork(String env) throws SQLException {
        ProductDao productDao = daoBuilder.buildProductDao(env);
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
