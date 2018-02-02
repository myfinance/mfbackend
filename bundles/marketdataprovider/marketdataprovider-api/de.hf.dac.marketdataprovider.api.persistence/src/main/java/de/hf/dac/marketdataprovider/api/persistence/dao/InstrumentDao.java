/** ----------------------------------------------------------------------------
 *
 * ---          DZ Bank FfM - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : InstrumentDao.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 28.11.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.persistence.dao;


import de.hf.dac.marketdataprovider.api.domain.Instrument;
import de.hf.dac.marketdataprovider.api.domain.Security;

import java.util.List;

public interface InstrumentDao {
    List<Instrument> listInstruments();
    void saveSecurity(Security instrument);
}
