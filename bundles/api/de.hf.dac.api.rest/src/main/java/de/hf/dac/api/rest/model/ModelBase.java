/** ----------------------------------------------------------------------------
 *
 * ---          HF- Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ModelBase.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 31.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.rest.model;

import de.hf.dac.api.base.identity.Storable;
import io.swagger.annotations.ApiModelProperty;

public abstract class ModelBase implements Storable {

    protected String url = "";
    protected String id;

    @ApiModelProperty(required = true)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ApiModelProperty(required = true)
    public String getId() {
        return id;
    }
}
