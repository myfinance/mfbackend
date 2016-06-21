/** ----------------------------------------------------------------------------
 *
 * ---                                ---
 *              Copyright (c) 2015, ... All Rights Reserved
 *
 *
 *  Project     : finance-service
 *
 *  File        : FinanceService.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 09.03.2015
 *
 * ----------------------------------------------------------------------------
 */
package de.hf.finance.secureservice.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.hf.marketdataprovider.domain.Product;
import de.hf.marketdataprovider.service.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordnik.swagger.annotations.Api;



@Path("/service/finance")
@Api( value = "/service/finance", description = "A Test Service" )
//@Stateful
@Stateless
public class FinanceService  {
	
	private static final Logger LOG = LoggerFactory.getLogger(FinanceService.class);

	@Inject
	private ProductServiceImpl productService;


	/**
	 * The default constructor. 
	 */
	public FinanceService() {
		super();
	}

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/hello")
	public String hello() {
		return "hello";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get/products")
	public List<Product> getProducts() {
		return productService.listProducts();
	}
	

}



