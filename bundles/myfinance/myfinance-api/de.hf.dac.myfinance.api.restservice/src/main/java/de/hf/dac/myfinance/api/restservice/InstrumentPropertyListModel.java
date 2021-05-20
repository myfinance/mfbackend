package de.hf.dac.myfinance.api.restservice;

import java.util.List;

import de.hf.dac.api.rest.model.data.ListModel;
import de.hf.dac.myfinance.api.domain.InstrumentProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public  class InstrumentPropertyListModel extends ListModel<InstrumentProperties> {
    public InstrumentPropertyListModel(List<InstrumentProperties> list) {
        super(list);
    }

    @Override
    @ApiModelProperty(required = true)
    public List<InstrumentProperties> getValues() {
        return values;
    }
}