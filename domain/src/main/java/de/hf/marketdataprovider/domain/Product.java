/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.marketdataprovider.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 *
 * @author surak
 */
@Entity
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
 
    @Version
    private Integer version;
 
    private String productId;
    private String description;
    private String imageUrl;
    private BigDecimal price;   
    //private BigDecimal details; 

    public Product() {
    }
    
    public Product(String description) {
        this.description=description;
    }
            
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description=description;
    }
    
    public Integer getVersion() {
        return version;
    }
 
    public void setVersion(Integer version) {
        this.version = version;
    }
 
    public Integer getId() {
        return id;
    }
 
    public void setId(Integer id) {
        this.id = id;
    }
 
    public String getProductId() {
        return productId;
    }
 
    public void setProductId(String productId) {
        this.productId = productId;
    }
 
    public String getImageUrl() {
        return imageUrl;
    }
 
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
 
    public BigDecimal getPrice() {
        return price;
    }
 
    public void setPrice(BigDecimal price) {
        this.price = price;
    }  
    
    /*public BigDecimal getDetails() {
        return details;
    }
 
    public void setDetails(BigDecimal details) {
        this.details = details;
    }      */
}
