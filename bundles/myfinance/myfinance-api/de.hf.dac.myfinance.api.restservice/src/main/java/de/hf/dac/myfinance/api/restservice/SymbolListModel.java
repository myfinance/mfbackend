package de.hf.dac.myfinance.api.restservice;

import java.util.List;

import de.hf.dac.api.rest.model.data.ListModel;
import de.hf.dac.myfinance.api.domain.SecuritySymbols;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SymbolListModel extends ListModel<SecuritySymbols>{

    /**
     *
     */
    private static final long serialVersionUID  = 1L;

    public SymbolListModel(List<SecuritySymbols> list) {
        super(list);
    }

    @Override
    @ApiModelProperty(required = true)
    public List<SecuritySymbols> getValues() {
        return values;
    }
}