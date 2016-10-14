/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.dac.marketdataprovider.api.service;

import de.hf.dac.marketdataprovider.api.domain.Product;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author surak
 */
public interface ProductService {

    List<Product> listProducts(String env) throws SQLException;
    void saveProduct(Product product, String env) throws SQLException;
    List<Product> doSomeWork(String env) throws SQLException;
}
