/**
 * ----------------------------------------------------------------------------
 * ---          HF - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : InstrumentGraph.java
 * Author(s)   : hf
 * Created     : 21.12.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.api.domain;

import java.util.List;

public class InstrumentGraph {
    private List<InstrumentGraph> childs;
    private EdgeType edgetype;
    private Instrument instrument;

    public InstrumentGraph(){ }

    public InstrumentGraph(Instrument instrument, List<InstrumentGraph> childs, EdgeType edgetype){
        this.instrument=instrument;
        this.edgetype=edgetype;
    }
}
