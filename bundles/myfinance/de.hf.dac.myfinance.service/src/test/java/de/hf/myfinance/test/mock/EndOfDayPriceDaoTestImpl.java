package de.hf.myfinance.test.mock;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.hf.dac.myfinance.api.domain.EndOfDayPrice;
import de.hf.dac.myfinance.api.domain.Source;
import de.hf.dac.myfinance.api.persistence.dao.EndOfDayPriceDao;

public class EndOfDayPriceDaoTestImpl implements EndOfDayPriceDao {
    Map<Integer, Map<LocalDate,EndOfDayPrice>> pricemap = new HashMap<Integer, Map<LocalDate,EndOfDayPrice>>();

    @Override
    public Optional<EndOfDayPrice> getEndOfDayPrice(int instrumentid, LocalDate dayofprice) {
        return Optional.ofNullable(pricemap.get(instrumentid).get(dayofprice));
    }

    @Override
    public List<EndOfDayPrice> listEndOfDayPrices(int instrumentid) {
        return pricemap.get(instrumentid).values().stream().collect(Collectors.toList());
    }

    @Override
    public LocalDate getLastPricedDay(int instrumentid) {
        if(!pricemap.containsKey(instrumentid)) {
            return LocalDate.MIN;
        }
        return pricemap.get(instrumentid).keySet().stream().max( Comparator.comparing( LocalDate::toEpochDay)).get();
    }

    @Override
    public void saveEndOfDayPrice(EndOfDayPrice price) {
        Map<LocalDate,EndOfDayPrice> instrumentpricemap = new HashMap<LocalDate,EndOfDayPrice>();
        if(pricemap.containsKey(price.getSecurity().getInstrumentid())) {
            instrumentpricemap=pricemap.get(price.getSecurity().getInstrumentid());
        }
        instrumentpricemap.put(price.getDayofprice(), price);
        pricemap.put(price.getSecurity().getInstrumentid(), instrumentpricemap);
    }

    @Override
    public Optional<Source> getSource(int sourceId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Source> getActiveSources() {
        // TODO Auto-generated method stub
        return null;
    }
    
}