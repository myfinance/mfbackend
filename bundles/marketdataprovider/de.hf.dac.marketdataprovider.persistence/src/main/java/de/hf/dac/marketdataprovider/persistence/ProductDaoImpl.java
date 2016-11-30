/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ProductDaoImpl.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 11.10.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.persistence;

import de.hf.dac.marketdataprovider.api.domain.Product;
import de.hf.dac.marketdataprovider.api.persistence.dao.ProductDao;
import de.hf.dac.marketdataprovider.api.persistence.repositories.ProductRepository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl extends BaseDao implements ProductDao {

    private ProductRepository productRepository;

    @Inject
    public ProductDaoImpl(EntityManagerFactory marketDataEmf) {
        super(marketDataEmf);
        productRepository = repositoryService.buildRepository(ProductRepository.class, marketDataEm);
    }

    /**
     * show case: simple read with named Query
     * just a show case this shouldn't be used
     * Use repositories or for special optimized queries create the query here
     * @return
     */
    public List<Product> listProductsNamedQuery() {
        TypedQuery<Product> query = marketDataEm.createNamedQuery(Product.findAll, Product.class);
        return query.getResultList();
    }

    /**
     * show case: read with optimized Queries (show case for self written queries, the example query is simple)
     * @return
     */
    public String getProductDescriptionWithoutRepo(String productId) {
        String result="";
        Query query = marketDataEm.createQuery("select a.description FROM Product a WHERE product_Id = :product_Id");
        query.setParameter("product_Id", productId);
        List<Object> queryResult = (List<Object>) query.getResultList();
        if(queryResult.isEmpty()) return result;
        Object object = queryResult.get(0);
        /*Object[] pair = (Object[]) object;
        if (pair[0] != null) {
            result = (String) pair[0];
        }*/
        result = (String)object;
        return result;
    }

    /**
     * show case: read with optimized Queries (show case for self written queries, the example query is simple)
     * @return
     */
    public List<Product> getProductsWithoutRepo() {
        List<Product> result = new ArrayList<>();

        Query query = marketDataEm.createQuery("select a FROM Product a");
        List<Object> queryResult = (List<Object>) query.getResultList();
        for (Object object : queryResult) {
            result.add((Product)object);
        }
        return result;
    }

    /**
     * show case: simple read with repo
     * @return
     */
    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    /**
     * show case: simple read with repo
     * @return
     */
    public Product getProduct(String productId) {
        return productRepository.findByProductId(productId).orElse(null);
    }

    /**
     * Show case: simple save
     * @param product
     */
    public void saveProduct(Product product) {
        marketDataEm.getTransaction().begin();
        marketDataEm.persist(product);
        marketDataEm.getTransaction().commit();
    }

    /**
     * show case for a save of a List in an additional connection
     * @param products
     */
    public void saveProducts(List<Product> products) {
        EntityManager em = null;
        //try-catch-rollback is necessary for bigger transactions
        try {
            em = marketDataEmf.createEntityManager();
            ProductRepository productRepository = repositoryService.buildRepository(ProductRepository.class, em);
            em.getTransaction().begin();
            productRepository.save(products);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if(em!=null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    /**
     * show case for delete batch with Spring Data Repository
     * uses Try catch and rollback in case of errors. This is necessary for bigger transactions
     * uses an additional Connection
     */
    public void deleteAll() {
        EntityManager em = null;
        //try-catch-rollback is necessary for bigger transactions
        try {
            em = marketDataEmf.createEntityManager();
            ProductRepository productRepository = repositoryService.buildRepository(ProductRepository.class, em);
            em.getTransaction().begin();
            productRepository.deleteAllInBatch();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if(em!=null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    /**
     * show case for delete with Spring Data Repository
     * @param product
     */
    public void delete(Product product) {
        marketDataEm.getTransaction().begin();
        productRepository.delete(product);
        marketDataEm.getTransaction().commit();
    }

    /**
     * show case for manual fast deletes
     * @param productId
     */
    public void deleteFast(String productId) {
        marketDataEm.getTransaction().begin();
        Query query = marketDataEm.createQuery("DELETE FROM Product a WHERE product_Id = :product_Id");
        query.setParameter("product_Id", productId);
        query.executeUpdate();
        marketDataEm.getTransaction().commit();
    }
}
