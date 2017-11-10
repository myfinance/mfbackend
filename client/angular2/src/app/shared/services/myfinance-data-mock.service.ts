import {Inject, Injectable} from "@angular/core";

import {Observable} from "rxjs";
import 'rxjs/add/operator/map';
import {Instrument} from "../models/instrument";
import {Position} from "../models/Position";
/**
 * Created by xn01598 on 11.05.2017.
 */

@Injectable()
export class myfinanceDummyDataService{
  constructor(
  ) {
    console.debug('Viele Grüße aus dem dummy Ctor!');
  }

  find(isin: string): Observable<Instrument[]> {

    var instrument = { id: 1, isin: "isin00000001", desc:"testinstrument1", lastUpdate: "2017-12-24T17:00:00.000+01:00" };
    var instrument2 = { id: 2, isin: "isin00000002", desc:"testinstrument2", lastUpdate: "2017-12-24T17:00:00.000+01:00" };
    let instruments: Instrument[]=[instrument, instrument2]
    let filteredinstruments: Instrument[]=instruments.filter(i=>i.isin.indexOf(isin)>=0);
    return Observable.of(filteredinstruments);
  }

  getPositions(): Observable<Position[]> {

    var pos1 = { isin: "isin00000001", desc:"testinstrument1", price: 1.1, amount: 20, valdate: "2017-01-01T17:00:00.000+01:00" };
    var pos2 = { isin: "isin00000001", desc:"testinstrument1", price: 1.2, amount: 20, valdate: "2017-01-02T17:00:00.000+01:00" };
    var pos3 = { isin: "isin00000001", desc:"testinstrument1", price: 1.3, amount: 20, valdate: "2017-01-03T17:00:00.000+01:00" };
    var pos4 = { isin: "isin00000001", desc:"testinstrument1", price: 1.25, amount: 20, valdate: "2017-01-04T17:00:00.000+01:00" };
    var pos5 = { isin: "isin00000001", desc:"testinstrument1", price: 1.27, amount: 20, valdate: "2017-01-05T17:00:00.000+01:00" };
    var pos6 = { isin: "isin00000001", desc:"testinstrument1", price: 1.31, amount: 20, valdate: "2017-01-06T17:00:00.000+01:00" };
    var pos7 = { isin: "isin00000001", desc:"testinstrument1", price: 1.26, amount: 20, valdate: "2017-01-07T17:00:00.000+01:00" };
    var pos8 = { isin: "isin00000001", desc:"testinstrument1", price: 1.24, amount: 18, valdate: "2017-01-08T17:00:00.000+01:00" };
    var pos9 = { isin: "isin00000001", desc:"testinstrument1", price: 1.28, amount: 18, valdate: "2017-01-09T17:00:00.000+01:00" };
    var pos10 = { isin: "isin00000002", desc:"testinstrument2", price: 8.1, amount: 10, valdate: "2017-01-01T17:00:00.000+01:00" };
    var pos11 = { isin: "isin00000002", desc:"testinstrument2", price: 7.5, amount: 10, valdate: "2017-01-02T17:00:00.000+01:00" };
    var pos12 = { isin: "isin00000002", desc:"testinstrument2", price: 7.4, amount: 10, valdate: "2017-01-03T17:00:00.000+01:00" };
    var pos13 = { isin: "isin00000002", desc:"testinstrument2", price: 7.2, amount: 15, valdate: "2017-01-04T17:00:00.000+01:00" };
    var pos14 = { isin: "isin00000002", desc:"testinstrument2", price: 7.5, amount: 15, valdate: "2017-01-05T17:00:00.000+01:00" };
    var pos15 = { isin: "isin00000002", desc:"testinstrument2", price: 7.6, amount: 15, valdate: "2017-01-06T17:00:00.000+01:00" };
    var pos16 = { isin: "isin00000002", desc:"testinstrument2", price: 7.8, amount: 15, valdate: "2017-01-07T17:00:00.000+01:00" };
    var pos17 = { isin: "isin00000002", desc:"testinstrument2", price: 7.7, amount: 15, valdate: "2017-01-08T17:00:00.000+01:00" };
    var pos18 = { isin: "isin00000002", desc:"testinstrument2", price: 7.9, amount: 15, valdate: "2017-01-09T17:00:00.000+01:00" };
    var pos19 = { isin: "isin00000003", desc:"testinstrument3", price: 10.1, amount: 5, valdate: "2017-01-01T17:00:00.000+01:00" };
    var pos20 = { isin: "isin00000003", desc:"testinstrument3", price: 10.25, amount: 5, valdate: "2017-01-02T17:00:00.000+01:00" };
    var pos21 = { isin: "isin00000003", desc:"testinstrument3", price: 10.31, amount: 5, valdate: "2017-01-03T17:00:00.000+01:00" };
    var pos22 = { isin: "isin00000003", desc:"testinstrument3", price: 10.28, amount: 5, valdate: "2017-01-04T17:00:00.000+01:00" };
    var pos23 = { isin: "isin00000003", desc:"testinstrument3", price: 10.27, amount: 5, valdate: "2017-01-05T17:00:00.000+01:00" };
    var pos24 = { isin: "isin00000003", desc:"testinstrument3", price: 10.31, amount: 5, valdate: "2017-01-06T17:00:00.000+01:00" };
    var pos25 = { isin: "isin00000003", desc:"testinstrument3", price: 10.26, amount: 5, valdate: "2017-01-07T17:00:00.000+01:00" };
    var pos26 = { isin: "isin00000003", desc:"testinstrument3", price: 10.24, amount: 5, valdate: "2017-01-08T17:00:00.000+01:00" };
    var pos27 = { isin: "isin00000003", desc:"testinstrument3", price: 10.28, amount: 5, valdate: "2017-01-09T17:00:00.000+01:00" };
    var pos28 = { isin: "isin00000004", desc:"testinstrument4", price: 8.1, amount: 10, valdate: "2017-01-01T17:00:00.000+01:00" };
    var pos29 = { isin: "isin00000004", desc:"testinstrument4", price: 8.85, amount: 10, valdate: "2017-01-02T17:00:00.000+01:00" };
    var pos30 = { isin: "isin00000004", desc:"testinstrument4", price: 9.31, amount: 10, valdate: "2017-01-03T17:00:00.000+01:00" };
    var pos31 = { isin: "isin00000004", desc:"testinstrument4", price: 9.28, amount: 10, valdate: "2017-01-04T17:00:00.000+01:00" };
    var pos32 = { isin: "isin00000004", desc:"testinstrument4", price: 9.45, amount: 10, valdate: "2017-01-05T17:00:00.000+01:00" };
    var pos33 = { isin: "isin00000004", desc:"testinstrument4", price: 9.31, amount: 10, valdate: "2017-01-06T17:00:00.000+01:00" };
    var pos34 = { isin: "isin00000004", desc:"testinstrument4", price: 9.5, amount: 10, valdate: "2017-01-07T17:00:00.000+01:00" };
    var pos35 = { isin: "isin00000004", desc:"testinstrument4", price: 10.24, amount: 0, valdate: "2017-01-08T17:00:00.000+01:00" };
    var pos36 = { isin: "isin00000004", desc:"testinstrument4", price: 9.28, amount: 0, valdate: "2017-01-09T17:00:00.000+01:00" };
    let positions: Position[]=[pos1, pos2, pos3, pos4, pos5, pos6, pos7,pos8, pos9, pos10, pos11,
        pos12, pos13, pos14, pos15, pos16, pos17, pos18, pos19, pos20,pos21, pos22, pos23, pos24, pos25, pos27, pos28,
        pos29, pos30, pos31, pos32, pos33, pos34, pos35, pos36]
    return Observable.of(positions);


  }

}
