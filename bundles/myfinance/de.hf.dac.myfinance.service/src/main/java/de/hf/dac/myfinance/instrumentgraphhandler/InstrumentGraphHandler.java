package de.hf.dac.myfinance.instrumentgraphhandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import de.hf.dac.myfinance.api.domain.EdgeType;
import de.hf.dac.myfinance.api.domain.Instrument;
import de.hf.dac.myfinance.api.domain.InstrumentType;

public interface InstrumentGraphHandler {
    /**
     * add an instrument to the graph
     * @param instrumentId the instrumentId of the instrument
     * @param ancestorId the id of the parent of the instrument
     * @param edgeType the edgetype that describes the relation between parent and child e.G. TenantGraph
     */
    void addInstrumentToGraph(int instrumentId, int ancestorId, EdgeType edgeType);
    /** calls addInstrumentToGraph(int instrumentId, int ancestorId, EdgeType edgeType) with EdgeType =  TenantGraph */
    void addInstrumentToGraph(int instrumentId, int ancestorId);

    /**
     * get the id of the rootinstrument in the graph 
     * @param instrumentId the id of the child instrument where there the root is requested for
     * @param edgeType the edgetype that describes the relation between parent and child e.G. TenantGraph
     * @return the optional of the id of the root instrument
     */
    Optional<Integer> getRootInstrument(int instrumentId, EdgeType edgeType);
    /** calls getRootInstrument(int instrumentId, EdgeType edgeType) with EdgeType =  TenantGraph */
    Optional<Integer> getRootInstrument(int instrumentId);

    /**
     * get the childs of an instrument with an spezific pathlegth in the graph
     * @param instrumentId - the id of the instrument for which the childs are requested
     * @param edgeType - the edgetype that describes the relation between parent and child e.G. TenantGraph
     * @param pathlength - the pathlength
     * @return the childs of the instrument with an spezific pathlegth in the graph
     */
    List<Instrument> getInstrumentChilds(int instrumentId, EdgeType edgeType, int pathlength);
    /** calls getInstrumentChilds(int instrumentId, EdgeType edgeType, int pathlength) with pathleght = 1 */
    List<Instrument> getInstrumentFirstLevelChilds(int instrumentId, EdgeType edgeType);
    /**  calls getInstrumentChilds(int instrumentId, EdgeType edgeType, int pathlength) with pathleght = 1 and EdgeType =  TenantGraph */
    List<Instrument> getInstrumentFirstLevelChilds(int instrumentId);
    /** calls getInstrumentChilds(int instrumentId, EdgeType edgeType, int pathlength) EdgeType =  TenantGraph*/
    List<Instrument> getInstrumentChilds(int instrumentId, int pathlength);
    /** calls getInstrumentChilds(int instrumentId, EdgeType edgeType, int pathlength) with pathlegth = 1 edgetype=TenantGraph and filters for the instrumenttype*/
    Stream<Instrument> getFirstLevelChildsPerType(int instrumentId, InstrumentType instrumentType);
    /** calls getFirstLevelChildsPerType and returns the first match*/
    Optional<Instrument> getFirstLevelChildsPerTypeFirstmatch(int instrumentId, InstrumentType instrumentType);

    /**
     * get all childs of an instrument no matter the pathlegth in the graph
     * @param instrumentId - the id of the instrument for which the childs are requested
     * @param edgeType - the edgetype that describes the relation between parent and child e.G. TenantGraph
     * @return all childs of the instrument 
     */
    List<Instrument> getAllInstrumentChilds(int instrumentId, EdgeType edgeType);
    /** calls  getAllInstrumentChilds(int instrumentId, EdgeType edgeType) and filters for active instruments in case onlyActive=true */
    List<Instrument> getAllInstrumentChilds(int instrumentId, EdgeType edgeType, boolean onlyActive);
    /** calls  getAllInstrumentChilds(int instrumentId, EdgeType edgeType) with EdgeType = TenantGraph and filters for active instruments in case onlyActive=true */
    List<Instrument> getAllInstrumentChilds(int instrumentId, boolean onlyActive);
    /**  calls getAllInstrumentChilds(int instrumentId, EdgeType edgeType) with EdgeType = TenantGraph */
    List<Instrument> getAllInstrumentChilds(int instrumentId);
    /** calls getAllInstrumentChilds(int instrumentId, EdgeType edgeType) with EdgeType = TenantGraph and filters for instrument with type=instrumentType and for active instruments in case onlyActive=true */
    List<Instrument> getAllInstrumentChilds(final int instrumentId, final InstrumentType instrumentType, final boolean onlyActive);
    /**  calls getAllInstrumentChilds(int instrumentId, EdgeType edgeType) with EdgeType = TenantGraph  and filters for instrument with type=instrumentType */
    List<Instrument> getAllInstrumentChildsWithType(int instrumentId, InstrumentType instrumentType);
    
    
    /** calls getFirstLevelChildsPerTypeFirstmatch(int instrumentId, InstrumentType instrumentType) 
     * with type AccountPortfolio  to get the Accountportfolio of the tenant*/
    Optional<Instrument> getAccountPortfolio(int tenantId);
    /** Returns all the Accounts of a tenant active and inactive
     * the type of the accounts doesn't matter. All childinstruments of the Accountportfolio will be returned
    */
    List<Instrument> getAccounts(int tenantId);
    
    /**
     * 
     * @param instrumentId - the id of the instrument for which the childs are requested
     * @param edgeType - the edgetype that describes the relation between parent and child e.G. TenantGraph
     * @return the id of the ancestor of the instrument
     */
    int getAncestorId(int instrumentId, EdgeType edgeType);
    /** calls getAncestorId(int instrumentId, EdgeType edgeType) with edgetype=TenantGraph */
    int getAncestorId(int instrumentId);
    
}