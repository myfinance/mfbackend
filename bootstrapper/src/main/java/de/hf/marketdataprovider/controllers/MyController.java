package de.hf.marketdataprovider.controllers;

import de.hf.marketdataprovider.domain.Product;
import de.hf.marketdataprovider.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

/**
 *
 * @author surak
 */
@Controller
public class MyController {
    private ProductService productService;
    
    @Autowired
    public void setProductService(ProductService productService) {
        this.productService =productService;
    }

    @PreAuthorize("hasRole('READ')")
    public List<Product> getProducts() {
        return productService.listProducts();
    }
}
