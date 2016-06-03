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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import de.hf.marketdataprovider.domain.EndOfDayPrice;
import de.hf.marketdataprovider.domain.Instrument;
import de.hf.marketdataprovider.persistence.repositories.EndOfDayPriceRepository;
import de.hf.marketdataprovider.persistence.repositories.InstrumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
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

import javax.persistence.Embedded;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @RequestMapping(value = "/{isin}/Prices/{priceId}", method = RequestMethod.GET)
    PriceResource readPrice(@PathVariable String isin, @PathVariable Integer priceId) {
        this.validateIsin(isin);
        return new PriceResource(this.priceRepository.findOne(priceId));
    }

    @RequestMapping(value = "/{isin}/Prices", method = RequestMethod.GET)
    Resources<PriceResource> readPrices(@PathVariable String isin) {
        this.validateIsin(isin);
        List<PriceResource> priceResourceList = priceRepository.findByInstrumentIsin(isin)
            .stream()
            .map(PriceResource::new)
            .collect(Collectors.toList());
        return new Resources<>(priceResourceList);
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
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class IsinNotFoundException extends RuntimeException {

    public IsinNotFoundException(String isin) {
        super("could not find Instrument with ISIN '" + isin + "'.");
    }
}

class PriceResource extends ResourceSupport {

    private final EndOfDayPrice price;

    public PriceResource(EndOfDayPrice price) {
        String isin = price.getInstrument().getIsin();
        this.price = price;
        this.add(new Link(price.getDayOfPrice().toString(), "day of Price"));
        this.add(linkTo(InstrumentController.class, isin).withRel("prices"));
        this.add(linkTo(methodOn(InstrumentController.class, isin).readPrice(isin, price.getId())).withSelfRel());
    }

    public EndOfDayPrice getPrice() {
        return price;
    }
}


class InstrumentResource extends ResourceSupport {

    private final Instrument instrument;

    @JsonCreator
    public InstrumentResource(@JsonProperty("content") Instrument instrument) {
        this.instrument = instrument;
    }

    public Instrument getInstrument() {
        return instrument;
    }
}

class InstrumentsResource extends HALResource {

    private final int numberOfInstruments;
    //private List<Resource<InstrumentResource>> embendedResources;
    /*@JsonUnwrapped
    private final List<EmbeddedWrapper> embeddeds;*/

    public InstrumentsResource(List<Instrument> instruments) {
        this.numberOfInstruments = instruments.size();
        Link link = new Link("http://example.com/products/");

        List<InstrumentResource> instrumentResources = new ArrayList<>();
        instruments.forEach(i->instrumentResources.add(new InstrumentResource(i)));

        //resources = new Resources<>(instrumentResources, link);
        //embedResource("instuments", new Resources<>(instrumentResources, link));
        embedResource("instuments", instrumentResources);


        /*EmbeddedWrappers wrappers = new EmbeddedWrappers(true);
        embeddeds = new ArrayList<>();
        instruments.forEach(i->embeddeds.add(wrappers.wrap(new InstrumentResource(i))));*/

        /*embendedResources = new ArrayList<>();
        instruments.forEach(i->embendedResources.add(new Resource<>(new InstrumentResource(i))));*/
    }

    public int getNumberOfInstruments() {
        return numberOfInstruments;
    }

    /*public List<EmbeddedWrapper> getEmbeddeds() {
        return embeddeds;
    }*/

    /*public List<Resource<InstrumentResource>> getInstruments() {
        return embendedResources;
    }*/
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
