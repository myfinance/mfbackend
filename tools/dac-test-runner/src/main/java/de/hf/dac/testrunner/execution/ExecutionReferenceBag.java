/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : ExecutionReferenceBag.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 06.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.execution;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ExecutionReferenceBag implements ReferenceBag{
    private Map<String, Object> bag = new LinkedHashMap<>();

    public ExecutionReferenceBag() {
    }

    public ExecutionReferenceBag(Map<String, Object> bag) {
        this.bag.putAll(bag);
    }

    public ExecutionReferenceBag(ReferenceBag bag) {
        for (String s : bag.getKeys()) {
            this.bag.put(s, bag.get(s));
        }
    }



    public <T> T get(String key) {
        return (T) bag.get(key);
    }

    @Override
    public Set<String> getKeys() {
        return bag.keySet();
    }

    @Override
    public void set(String key, Object value) {
        this.bag.put(key, value);
    }

    public void reset() {
        this.bag.clear();
    }

    public Map<String, Object> getBag() {
        return bag;
    }

    public void setBag(Map<String, Object> bag) {
        this.bag = bag;
    }
}
