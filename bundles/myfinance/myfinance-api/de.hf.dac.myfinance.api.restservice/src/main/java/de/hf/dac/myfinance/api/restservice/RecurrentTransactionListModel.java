package de.hf.dac.myfinance.api.restservice;

import de.hf.dac.api.rest.model.data.ListModel;
import de.hf.dac.myfinance.api.domain.RecurrentTransaction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class RecurrentTransactionListModel extends ListModel<RecurrentTransaction> {

    public RecurrentTransactionListModel(List<RecurrentTransaction> list){
        super(list);
    }

    @Override
    @ApiModelProperty(required = true)
    public List<RecurrentTransaction> getValues() {
        return values;
    }
}
