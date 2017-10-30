/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : PostConstructTestee.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 26.07.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketDataProvider.paxexam.context;

import javax.annotation.PostConstruct;

public class PostConstructTestee {

    private String result;

    public PostConstructTestee() {

    }

    public String getResult() {
        return result;
    }

    @PostConstruct
    public void init() {
        this.result = "Called";
    }
}
