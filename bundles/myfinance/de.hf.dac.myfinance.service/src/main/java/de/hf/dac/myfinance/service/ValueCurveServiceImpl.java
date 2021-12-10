/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ValueCurveService.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 16.02.2018
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.service;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.InstrumentGraphEntry;
import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.InstrumentTypeGroup;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.persistence.dao.EndOfDayPriceDao;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.TransactionService;
import de.hf.dac.myfinance.api.service.ValueCurveCache;
import de.hf.dac.myfinance.api.service.ValueCurveService;
import de.hf.dac.myfinance.valuehandler.CashAccValueHandler;
import de.hf.dac.myfinance.valuehandler.DepotValueHandler;
import de.hf.dac.myfinance.valuehandler.PortfolioValueHandler;
import de.hf.dac.myfinance.valuehandler.RealEstateValueHandler;
import de.hf.dac.myfinance.valuehandler.SecurityValueHandler;
import de.hf.dac.myfinance.valuehandler.TenantValueHandler;
import de.hf.dac.myfinance.valuehandler.ValueHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

public class ValueCurveServiceImpl implements ValueCurveService {

    private final InstrumentService instrumentService;
    private final TransactionService transactionService;
    private final EndOfDayPriceDao endOfDayPriceDao;
    ValueCurveCache cache;

    @Inject
    public ValueCurveServiceImpl(final InstrumentService instrumentService, final EndOfDayPriceDao endOfDayPriceDao,
            final ValueCurveCache cache, final TransactionService transactionService) {
        this.instrumentService = instrumentService;
        this.endOfDayPriceDao = endOfDayPriceDao;
        this.transactionService = transactionService;
        this.cache = cache;
    }

    public TreeMap<LocalDate, Double> getValueCurve(final int instrumentId) {
        TreeMap<LocalDate, Double> valueCurve = cache.getValueCurve(instrumentId);
        if (valueCurve == null) {
            final var instrument = instrumentService.getInstrument(instrumentId);
            valueCurve = getValueHandler(instrument.getInstrumentType()).calcValueCurve(instrument);
            cache.addValueCurve(instrumentId, valueCurve);
        }
        return valueCurve;
    }

    @Override
    public Map<Integer, TreeMap<LocalDate, Double>> getPositionCurve(int depotId) {
        Map<Integer, TreeMap<LocalDate, Double>> valueCurve = cache.getPositionCurve(depotId);
        if (valueCurve == null) {
            final var instrument = instrumentService.getInstrument(depotId);
            if(!instrument.getInstrumentType().getTypeGroup().equals(InstrumentTypeGroup.DEPOT)){
                throw new MFException(MFMsgKey.WRONG_INSTRUMENTTYPE_EXCEPTION, " wrong instrumenttype to calculate positions:" + instrument.getInstrumentType());
            }
            valueCurve = new DepotValueHandler(transactionService, this).calcPositionsCurve(depotId);
            cache.addPositionCurve(depotId, valueCurve);
        }
        return valueCurve;
    }

    @Override
    public Map<Integer, TreeMap<LocalDate, Double>> getPositionValueCurve(int depotId) {
        Map<Integer, TreeMap<LocalDate, Double>> valueCurve = cache.getPositionValueCurve(depotId);
        if (valueCurve == null) {
            valueCurve = new DepotValueHandler(transactionService, this).calcPositionsValueCurve(getPositionCurve(depotId));
            cache.addPositionValueCurve(depotId, valueCurve);
        }
        return valueCurve;
    }

    private ValueHandler getValueHandler(final InstrumentType instrumentType) {
        ValueHandler valueHandler;
        switch (instrumentType.getTypeGroup()) {
            case SECURITY:
                valueHandler = new SecurityValueHandler(this, endOfDayPriceDao);
                break;
            case CASHACCOUNT:
                valueHandler = new CashAccValueHandler(transactionService);
                break;
            case TENANT:
                valueHandler = new TenantValueHandler(instrumentService, this);
                break;
            case PORTFOLIO:
                valueHandler = new PortfolioValueHandler(instrumentService, this);
                break;
            case REALESTATE:
                valueHandler = new RealEstateValueHandler(instrumentService, this);
                break;
            case DEPOT:
                valueHandler = new DepotValueHandler(transactionService, this);
                break;
            case DEPRECATIONOBJECT:
            case LIVEINSURANCE:
            case LOAN:
            case UNKNOWN:
            default:
                throw new MFException(MFMsgKey.UNKNOWN_INSTRUMENTTYPE_EXCEPTION, "Type:" + instrumentType);
        }
        return valueHandler;
    }

    @Override
    public double getValue(int instrumentId, LocalDate date) {
        double value = 0.0;
        final TreeMap<LocalDate, Double> valueCurve = getValueCurve(instrumentId);
        if (valueCurve.containsKey(date)) {
            value = valueCurve.get(date);
        } else if (valueCurve.firstKey().isAfter(date)) {
            value = valueCurve.get(valueCurve.firstKey());
        } else if (valueCurve.lastKey().isBefore(date)) {
            value = valueCurve.get(valueCurve.lastKey());
        }
        return value;
    }

    public void invalidateCache(final int instrumentId) {
        final var parentIds = instrumentService.getParentIds(instrumentId);
        for (final Integer id : parentIds) {
            cache.removeCurve(id);
        }
    }
}
