package de.hf.dac.myfinance.api.restservice;

import java.io.Serializable;
import java.util.Map;

import de.hf.dac.api.rest.model.data.MapModel;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentValuesTuple;
import io.swagger.annotations.ApiModelProperty;

public class AccountValueTupleModel extends MapModel<Instrument, InstrumentValuesTuple> implements Serializable {

    private static final long serialVersionUID = -7261744329095688352L;

    public AccountValueTupleModel(final Map<Instrument, InstrumentValuesTuple> map) {
        super(map);
    }

    @ApiModelProperty(required = true)
    @Override
    public Map<Instrument, InstrumentValuesTuple> getValues() {
        return values;
    }
}