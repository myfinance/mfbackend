package de.hf.dac.myfinance.api.restservice;

import java.util.Map;

import de.hf.dac.api.rest.model.data.MapModel;
import de.hf.dac.myfinance.api.domain.InstrumentDetails;
import io.swagger.annotations.ApiModelProperty;

public class InstrumentDetailModel extends MapModel<Integer, InstrumentDetails> {

    /**
     *
     */
    private static final long serialVersionUID = -7261744329095688352L;

    public InstrumentDetailModel(final Map<Integer, InstrumentDetails> map) {
        super(map);
    }

    @ApiModelProperty(required = true)
    @Override
    public Map<Integer, InstrumentDetails> getValues() {
        return values;
    }
}