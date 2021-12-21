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

package de.hf.dac.myfinance.valuehandler;

import de.hf.dac.myfinance.api.domain.InstrumentType;
import de.hf.dac.myfinance.api.domain.InstrumentTypeGroup;
import de.hf.dac.myfinance.api.exceptions.MFException;
import de.hf.dac.myfinance.api.exceptions.MFMsgKey;
import de.hf.dac.myfinance.api.service.InstrumentService;
import de.hf.dac.myfinance.api.service.PriceService;
import de.hf.dac.myfinance.api.service.TransactionService;
import de.hf.dac.myfinance.api.service.ValueCurveCache;
import de.hf.dac.myfinance.api.service.ValueCurveHandler;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

/**
 * holds the valuecache instance and choose the right valueHandle to calculate the valuecurve
 */
public class ValueCurveHandlerImpl implements ValueCurveHandler {

    private final InstrumentService instrumentService;
    private final TransactionService transactionService;
    private final PriceService priceService;
    ValueCurveCache cache;

    @Inject
    public ValueCurveHandlerImpl(final InstrumentService instrumentService, final PriceService priceService,
            final ValueCurveCache cache, final TransactionService transactionService) {
        this.instrumentService = instrumentService;
        this.priceService = priceService;
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
    public Map<LocalDate, Double> getValueCurve(final int instrumentId, final LocalDate startDate,
            final LocalDate endDate) {
        final Map<LocalDate, Double> adjValueCurve = new TreeMap<>();
        if (startDate.isAfter(endDate) || startDate.getYear() < 1970)
            return adjValueCurve;
        final Map<LocalDate, Double> valueCurve = getValueCurve(instrumentId);
        var first = LocalDate.MIN;
        var last = LocalDate.MAX;
        for (final LocalDate date : valueCurve.keySet()) {
            if(first==LocalDate.MIN) {
                first = date;
            }
            if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                adjValueCurve.put(date, valueCurve.get(date));
            }
            last = date;
        }
        if(!valueCurve.isEmpty()) {
            if(first.isAfter(endDate)) {
                adjValueCurve.put(startDate, valueCurve.get(first));
                adjValueCurve.put(endDate, valueCurve.get(first));
            } else if(last.isBefore(startDate)) {
                adjValueCurve.put(startDate, valueCurve.get(last));
                adjValueCurve.put(endDate, valueCurve.get(last));
            } else if(first.isAfter(startDate)) {
                adjValueCurve.put(startDate, valueCurve.get(first));
            } else if(last.isBefore(endDate)) {
                adjValueCurve.put(endDate, valueCurve.get(last));
            }
        }
        return adjValueCurve;
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
                valueHandler = new SecurityValueHandler(this, priceService);
                break;
            case CASHACCOUNT:
                valueHandler = new CashAccValueHandler(transactionService, this);
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
        final TreeMap<LocalDate, Double> valueCurve = getValueCurve(instrumentId);
        return getValue(valueCurve, date);
    }

    @Override
    public double getValue(TreeMap<LocalDate, Double> valueCurve, LocalDate date) {
        double value = 0.0;
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
