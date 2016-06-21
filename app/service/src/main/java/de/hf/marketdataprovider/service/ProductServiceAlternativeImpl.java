/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.service;

import de.hf.marketdataprovider.domain.Product;
import java.util.ArrayList;
import java.util.List;
//import org.springframework.stereotype.Service;

/**
 *
 * @author surak
 */
//@Service
public class ProductServiceAlternativeImpl implements ProductService {
    @Override
    public List<Product> listProducts() {
        ArrayList<Product> products = new ArrayList<>(2);
        products.add(new Product("1", "Product alternative  1 desc"));
        products.add(new Product("2", "Product alternative 2 desc"));
        return products;
    }
}