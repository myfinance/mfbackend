package de.hf.marketdataprovider.controllers;

import de.hf.marketdataprovider.domain.Product;
import de.hf.marketdataprovider.services.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    public List<Product> getProducts() {
        return productService.listProducts();
    }
}
