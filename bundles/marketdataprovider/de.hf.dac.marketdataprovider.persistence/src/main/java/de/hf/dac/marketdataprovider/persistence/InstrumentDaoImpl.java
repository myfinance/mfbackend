/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : InstrumentDaoImpl.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 28.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.persistence;

import de.hf.dac.marketdataprovider.api.application.EnvTarget;
import de.hf.dac.marketdataprovider.api.domain.Instrument;
import de.hf.dac.marketdataprovider.api.persistence.dao.InstrumentDao;
import de.hf.dac.marketdataprovider.api.persistence.repositories.InstrumentRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class InstrumentDaoImpl  extends BaseDao implements InstrumentDao {

    private InstrumentRepository instrumentRepository;

    @Inject
    public InstrumentDaoImpl(@Named(EnvTarget.MDB) EntityManagerFactory marketDataEmf) {
        super(marketDataEmf);
        instrumentRepository = repositoryService.buildRepository(InstrumentRepository.class, marketDataEm);
    }

    @Override
    public List<Instrument> listInstruments() {
        return instrumentRepository.findAll();
    }
}
