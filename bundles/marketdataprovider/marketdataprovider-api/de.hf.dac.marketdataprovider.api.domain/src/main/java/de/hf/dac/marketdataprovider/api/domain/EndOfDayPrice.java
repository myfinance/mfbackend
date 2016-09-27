/** ----------------------------------------------------------------------------
 *
 * ---                                 ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : EndOfDayPrice.java
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
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Data;

@Entity
@Data
@RequiredArgsConstructor(staticName = "of",access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class EndOfDayPrice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @NonNull
    private Instrument instrument;
    @NonNull
    private double endOfDayPrice;
    @NonNull
    private LocalDate dayOfPrice;
    @NonNull
    private int currencyId;
    private Instant lastChanged;

    public EndOfDayPrice(Instrument instrument, double endOfDayPrice, LocalDate dayOfPrice) {
        this(instrument,endOfDayPrice,dayOfPrice,1);
    }
}
