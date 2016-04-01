/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.services;

import de.hf.marketdataprovider.domain.Product;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 *
 * @author surak
 */
public interface ProductService {
    @PreAuthorize("hasRole('READ')")
    List<Product> listProducts();
    
}
