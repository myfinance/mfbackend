/** ----------------------------------------------------------------------------
 *
 * ---                             ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : marketDataProvider
 *
 *  File        : InstrumentController.java
 *
 *  Author(s)   : xn01598
 *
 *  Created     : 03.06.2016
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.marketdataprovider.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.hf.marketdataprovider.domain.EndOfDayPrice;
import de.hf.marketdataprovider.domain.Instrument;
import de.hf.marketdataprovider.persistence.repositories.EndOfDayPriceRepository;
import de.hf.marketdataprovider.persistence.repositories.InstrumentRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping(value = "/instruments", produces= MediaType.APPLICATION_JSON_VALUE)
public class InstrumentController {

    private final EndOfDayPriceRepository priceRepository;
    private final InstrumentRepository instrumentRepository;

    @RequestMapping(method = RequestMethod.GET)
    InstrumentsResource readInstruments() {

        List<Instrument> instruments = new ArrayList<Instrument>();
        instrumentRepository.findAll().forEach(i->instruments.add(i));


        return new InstrumentsResource(instruments);
    }

    @RequestMapping(value = "/{isin}", method = RequestMethod.GET)
    InstrumentResource readInstrument(@PathVariable String isin) {
        this.validateIsin(isin);
        return new InstrumentResource(this.instrumentRepository.findByIsin(isin).get());
    }

    @RequestMapping(value = "/{isin}/Prices", method = RequestMethod.POST)
    ResponseEntity<?> add(@PathVariable String isin, @RequestBody EndOfDayPrice input) {
        this.validateIsin(isin);
        return this.instrumentRepository
            .findByIsin(isin)
            .map(instrument -> {
                EndOfDayPrice price = priceRepository.save(new EndOfDayPrice(instrument, input.getEndOfDayPrice(), input.getDayOfPrice() ));

                HttpHeaders httpHeaders = new HttpHeaders();
                Link forOnePrice = new PriceResource(price).getLink("self");
                httpHeaders.setLocation(URI.create(forOnePrice.getHref()));
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
            }).get();

    }

    @RequestMapping(value = "/Prices/{priceId}", method = RequestMethod.GET)
    PriceResource readPrice(@PathVariable Integer priceId) {
        this.validatePriceId(priceId);
        return new PriceResource(this.priceRepository.findOne(priceId));
    }

    @RequestMapping(value = "/{isin}/Prices", method = RequestMethod.GET)
    PricesResource readPrices(@PathVariable String isin) {
        this.validateIsin(isin);
        return new PricesResource(priceRepository.findByInstrumentIsin(isin), isin);
    }

    @Autowired
    InstrumentController(EndOfDayPriceRepository priceRepository,
        InstrumentRepository instrumentRepository) {
        this.priceRepository = priceRepository;
        this.instrumentRepository = instrumentRepository;
    }

    private void validateIsin(String isin) {
        this.instrumentRepository.findByIsin(isin).orElseThrow(
            () -> new IsinNotFoundException(isin));
    }

    private void validatePriceId(int priceId) {
        if(this.priceRepository.findOne(priceId)==null)
            throw new PriceNotFoundException(priceId);
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class IsinNotFoundException extends RuntimeException {

    public IsinNotFoundException(String isin) {
        super("could not find Instrument with ISIN '" + isin + "'.");
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(int id) {
        super("could not find Price with Id '" + id + "'.");
    }
}

class PriceResource extends HALResource {

    @Getter
    private final EndOfDayPrice price;

    public PriceResource(EndOfDayPrice price) {
        this.price = price;

        //set links
        this.add(linkTo(methodOn(InstrumentController.class, price.getId()).readPrice(price.getId())).withSelfRel());
        this.add(linkTo(methodOn(InstrumentController.class, price.getInstrument().getIsin()).readInstrument(price.getInstrument().getIsin())).withRel("Instrument"));
    }
}


class InstrumentResource extends HALResource {

    @Getter
    private final Instrument instrument;

    public InstrumentResource(Instrument instrument) {
        this.instrument = instrument;

        //set links
        this.add(linkTo(methodOn(InstrumentController.class, instrument.getIsin()).readInstrument(instrument.getIsin())).withSelfRel());
        this.add(linkTo(methodOn(InstrumentController.class, instrument.getIsin()).readPrices(instrument.getIsin())).withRel("prices"));

    }
}

class PricesResource extends HALResource {

    @Getter
    private final int numberOfPrices;
    @Getter
    private final String isin;

    public PricesResource(List<EndOfDayPrice> prices, String isin) {
        //set Properties
        this.numberOfPrices = prices.size();
        this.isin=isin;

        //set links
        this.add(linkTo(methodOn(InstrumentController.class, isin).readPrices(isin)).withSelfRel());
        this.add(linkTo(methodOn(InstrumentController.class, isin).readInstrument(isin)).withRel("instrument"));
        prices.forEach(p->this.add(linkTo(methodOn(InstrumentController.class, p.getId()).readPrice(p.getId())).withRel("prices")));

        //set embeded Resources
        /*List<PriceResource> priceResources = new ArrayList<>();
        prices.forEach(i->priceResources.add(new PriceResource(i)));
        embedResource("prices", priceResources);*/


    }
}

class InstrumentsResource extends HALResource {

    @Getter
    private final int numberOfInstruments;

    public InstrumentsResource(List<Instrument> instruments) {
        //set Properties
        this.numberOfInstruments = instruments.size();

        //set links
        this.add(linkTo(InstrumentController.class).withSelfRel());
        instruments.forEach(i->this.add(linkTo(methodOn(InstrumentController.class, i.getIsin()).readInstrument(i.getIsin())).withRel("instruments")));

        //set embeded Resources
        List<InstrumentResource> instrumentResources = new ArrayList<>();
        instruments.forEach(i->instrumentResources.add(new InstrumentResource(i)));
        embedResource("instuments", instrumentResources);


    }
}

abstract class HALResource extends ResourceSupport {

    private final Map<String, List<InstrumentResource>> embedded = new HashMap<>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("_embedded")
    public Map<String, List<InstrumentResource>> getEmbeddedResources() {
        return embedded;
    }

    public void embedResource(String relationship, List<InstrumentResource> resources) {

        embedded.put(relationship, resources);
    }
}
