/** ----------------------------------------------------------------------------
 *
 * ---                                ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : Instrument.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 27.05.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.api.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.Instant;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Instrument implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NonNull
    private int instrumentTypeId;
    @NonNull
    private int sourceId;
    @NonNull
    private String isin;
    private String wkn;
    private String ticker;
    private String description;
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentInstantAsTimestamp")
    private Instant treeLastChanged;
    private int homeTradingCurrencyId;
}
