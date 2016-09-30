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
import de.hf.dac.marketdataprovider.api.service.ProductServiceBuilder;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.service.command.CommandSession;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

@Command(scope = "marketdata", name = "listproduct")
public class ProductListCommand extends EnvironmentBasedCommand<ProductService>{

    @Inject
    protected ProductServiceBuilder productServiceBuilder;

    public ProductServiceBuilder getProductServiceBuilder() {
        return productServiceBuilder;
    }
    public void setProductServiceBuilder(ProductServiceBuilder productServiceBuilder) {
        this.productServiceBuilder = productServiceBuilder;
    }

    @Override
    public Object execute(CommandSession commandSession) throws Exception {
        ProductService build = build();
        List<Product> products = build.listProducts();
        for (Product product : products) {
            System.out.println(product);
        }
        return "OK";
    }

    @Override
    protected ProductService build() throws SQLException {
        return productServiceBuilder.build(env);
    }


}
