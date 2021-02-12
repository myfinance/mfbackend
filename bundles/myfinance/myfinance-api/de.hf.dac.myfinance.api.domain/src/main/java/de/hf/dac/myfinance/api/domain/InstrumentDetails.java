package de.hf.dac.myfinance.api.domain;

import java.util.HashMap;
import java.util.Map;

public class InstrumentDetails {

    Map<String, String> valuemap = new HashMap<>();

    public Map<String, String> getValuemap() {
        return valuemap;
    }

    public String getValue(String propertyName) {
        return valuemap.get(propertyName);
    }

    public String putValue(String propertyName, String value) {
        return valuemap.put(propertyName, value);
    }
}