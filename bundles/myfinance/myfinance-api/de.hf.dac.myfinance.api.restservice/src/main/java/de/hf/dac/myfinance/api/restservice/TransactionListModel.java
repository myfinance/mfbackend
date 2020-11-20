/**
 * ----------------------------------------------------------------------------
 * ---          Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : TransactionListModel.java
 * Author(s)   : HF
 * Created     : 21.03.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.restservice;

import java.util.List;

import de.hf.dac.api.rest.model.data.ListModel;
import de.hf.dac.myfinance.api.domain.Transaction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class TransactionListModel extends ListModel<Transaction> {

    public TransactionListModel(List<Transaction> list){
        super(list);
    }

    @Override
    @ApiModelProperty(required = true)
    public List<Transaction> getValues() {
        return values;
    }
}
