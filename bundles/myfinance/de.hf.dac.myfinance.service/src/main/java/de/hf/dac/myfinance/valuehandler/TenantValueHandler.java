/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : TenantValueHandler.java
 * Author(s)   : xn01598
 * Created     : 21.03.2019
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.valuehandler;

import java.time.LocalDate;
import java.util.*;
import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.persistence.dao.InstrumentDao;
import de.hf.dac.myfinance.api.service.ValueCurveService;


public class TenantValueHandler  extends AbsValueHandler{
    private InstrumentDao instrumentDao;

    public TenantValueHandler(InstrumentDao instrumentDao, ValueCurveService valueCurveService){
        super(valueCurveService);
        this.instrumentDao = instrumentDao;
    }

    public TreeMap<LocalDate, Double> calcValueCurve(Instrument instrument) {
        TreeMap<LocalDate, Double> valueCurve = new TreeMap<>();
        List<Instrument>  childs = instrumentDao.getInstrumentChilds(instrument.getInstrumentid(), EdgeType.TENANTGRAPH, 1);
        if(childs==null || childs.isEmpty()) {
            return createZeroCurve(valueCurve);
        }
        Optional<Instrument> accPF = childs.stream().filter(i -> i.getInstrumentType().equals(InstrumentType.AccountPortfolio)).findFirst();
        if(!accPF.isPresent()) {
            return createZeroCurve(valueCurve);
        }
        List<Instrument>  accs = instrumentDao.getInstrumentChilds(accPF.get().getInstrumentid(), EdgeType.TENANTGRAPH, 1);
        if(accs==null || accs.isEmpty()) {
            return createZeroCurve(valueCurve);
        }
        LocalDate startDate = LocalDate.now();
        for (Instrument acc : accs) {
            TreeMap<LocalDate, Double> accValueCurve = this.valueCurveService.getValueCurve(acc.getInstrumentid());
            LocalDate minDate = accValueCurve.firstKey();
            if(minDate.isBefore(startDate)) {
                startDate = minDate;
            }
        }

        return getCombinedValueCurve(accs, startDate);
    }
}