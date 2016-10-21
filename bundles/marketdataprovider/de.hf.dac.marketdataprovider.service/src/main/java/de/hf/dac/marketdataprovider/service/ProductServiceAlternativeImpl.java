/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.dac.marketdataprovider.service;

import de.hf.dac.marketdataprovider.api.service.ProductService;
import de.hf.dac.marketdataprovider.api.domain.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author surak
 */
public class ProductServiceAlternativeImpl implements ProductService {
    @Override
    public List<Product> listProducts() throws SQLException {
        ArrayList<Product> products = new ArrayList<>(2);
        products.add(new Product("1", "Product alternative  1 desc"));
        products.add(new Product("2", "Product alternative 2 desc"));
        return products;
    }

    @Override
    public void saveProduct(Product product) throws SQLException {

    }

    @Override
    public List<Product> doSomeWork() throws SQLException {
        return null;
    }
}