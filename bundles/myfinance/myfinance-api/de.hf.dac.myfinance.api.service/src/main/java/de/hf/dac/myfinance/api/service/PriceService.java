package de.hf.dac.myfinance.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import de.hf.dac.myfinance.api.domain.EndOfDayPrice;
import de.hf.dac.myfinance.api.domain.Source;

public interface PriceService {
    Optional<EndOfDayPrice> getEndOfDayPrice(String isin, LocalDate date);
    List<EndOfDayPrice> listEodPrices(int instrumentId);
    Optional<EndOfDayPrice> getEndOfDayPrice(int instrumentId, LocalDate date);
    Optional<Source> getSource(int sourceId);
    void saveEndOfDayPrice(String currencyCode, String isin, LocalDate dayofprice, Double value, LocalDateTime lastchanged);
    void importPrices(LocalDateTime ts);
    void fillPriceHistory(int sourceId, String isin, LocalDateTime ts);
}