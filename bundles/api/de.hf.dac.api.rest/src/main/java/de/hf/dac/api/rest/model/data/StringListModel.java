/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : StringListModel.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 31.03.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.api.rest.model.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel
public class StringListModel extends ListModel<String> implements Serializable {

    public StringListModel(List<String> list) {
        super(list);
    }

    @ApiModelProperty(required = true)
    @Override
    public List<String> getValues() {
        return values;
    }

}
