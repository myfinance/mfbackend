/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ProductListCommand.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 28.09.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.service.commands;

import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.marketdataprovider.api.service.ProductService;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.service.command.CommandSession;

import javax.inject.Inject;
import java.util.List;

@Command(scope = "marketdata", name = "listproduct")
public class ProductListCommand extends EnvironmentBasedCommand<ProductService>{

    protected ProductService productService;
    @Inject
    public void setProductService(ProductService productService){
        this.productService=productService;
    }

    @Override
    public Object execute(CommandSession commandSession) throws Exception {
        List<Product> products = productService.listProducts(env);
        for (Product product : products) {
            System.out.println(product);
        }
        return "OK";
    }
}
