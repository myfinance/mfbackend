package de.hf.myfinance.test.mock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.EndOfDayPrice;
import de.hf.dac.myfinance.api.persistence.dao.EndOfDayPriceDao;

public class EndOfDayPriceDaoTestImpl implements EndOfDayPriceDao {

    @Override
    public Optional<EndOfDayPrice> getEndOfDayPrice(int instrumentid, LocalDate dayofprice) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<EndOfDayPrice> listEndOfDayPrices(int instrumentid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LocalDate getLastPricedDay(int instrumentid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveEndOfDayPrice(EndOfDayPrice price) {
        // TODO Auto-generated method stub

    }
    
}