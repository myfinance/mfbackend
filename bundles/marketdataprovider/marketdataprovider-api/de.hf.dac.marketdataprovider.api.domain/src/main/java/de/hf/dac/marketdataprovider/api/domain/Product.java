/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hf.dac.marketdataprovider.api.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 *
 * @author surak
 */
@NamedQueries({
    @NamedQuery(name=Product.findAll,query="SELECT a FROM Product a")
})
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Product implements Serializable {
    public static final String findAll = "Product.findAll";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
 
    //@Version
    private Integer version;
    @NonNull
    @Column(name = "product_Id")
    private String productId;
    @NonNull
    private String description;
    private String image_url;
    private BigDecimal price;
}
