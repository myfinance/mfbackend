import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Rx";
import 'rxjs/add/operator/map';
import 'rxjs/add/observable/of';
import {Instrument} from "../models/instrument";
import {Position} from "../models/position";

/**
 * Created by xn01598 on 11.05.2017.
 */

@Injectable()
export class MyfinanceDummyDataService{
  constructor(
  ) {
    console.debug('Viele Grüße aus dem dummy Ctor!');
  }

  find(isin: string): Observable<Instrument[]> {

    let instrument : Instrument = { instrumentid: 1, isin: "isin00000001", description:"testinstrument1", treelastchanged: "2017-12-24T17:00:00.000+01:00" };
    let instrument2 : Instrument = { instrumentid: 2, isin: "isin00000002", description:"testinstrument2", treelastchanged: "2017-12-24T17:00:00.000+01:00" };
    let instruments: Instrument[]=[instrument, instrument2]
    let filteredinstruments: Instrument[]=instruments.filter(i=>i.isin.indexOf(isin)>=0);
    return Observable.of(filteredinstruments);
  }

  getPositions(): Observable<Position[]> {

    var pos1 = { isin: "isin00000001", desc:"testinstrument1", price: 1.1, amount: 20, valdate: "2017-01-01" };
    var pos2 = { isin: "isin00000001", desc:"testinstrument1", price: 1.2, amount: 20, valdate: "2017-01-02" };
    var pos3 = { isin: "isin00000001", desc:"testinstrument1", price: 1.3, amount: 20, valdate: "2017-01-03" };
    var pos4 = { isin: "isin00000001", desc:"testinstrument1", price: 1.25, amount: 20, valdate: "2017-01-04" };
    var pos5 = { isin: "isin00000001", desc:"testinstrument1", price: 1.27, amount: 20, valdate: "2017-01-05" };
    var pos6 = { isin: "isin00000001", desc:"testinstrument1", price: 1.31, amount: 20, valdate: "2017-01-06" };
    var pos7 = { isin: "isin00000001", desc:"testinstrument1", price: 1.26, amount: 20, valdate: "2017-01-07" };
    var pos8 = { isin: "isin00000001", desc:"testinstrument1", price: 1.24, amount: 18, valdate: "2017-01-08" };
    var pos9 = { isin: "isin00000001", desc:"testinstrument1", price: 1.28, amount: 18, valdate: "2017-01-09" };
    var pos10 = { isin: "isin00000002", desc:"testinstrument2", price: 8.1, amount: 10, valdate: "2017-01-01" };
    var pos11 = { isin: "isin00000002", desc:"testinstrument2", price: 7.5, amount: 10, valdate: "2017-01-02" };
    var pos12 = { isin: "isin00000002", desc:"testinstrument2", price: 7.4, amount: 10, valdate: "2017-01-03" };
    var pos13 = { isin: "isin00000002", desc:"testinstrument2", price: 7.2, amount: 15, valdate: "2017-01-04" };
    var pos14 = { isin: "isin00000002", desc:"testinstrument2", price: 7.5, amount: 15, valdate: "2017-01-050" };
    var pos15 = { isin: "isin00000002", desc:"testinstrument2", price: 7.6, amount: 15, valdate: "2017-01-06" };
    var pos16 = { isin: "isin00000002", desc:"testinstrument2", price: 7.8, amount: 15, valdate: "2017-01-07" };
    var pos17 = { isin: "isin00000002", desc:"testinstrument2", price: 7.7, amount: 15, valdate: "2017-01-08" };
    var pos18 = { isin: "isin00000002", desc:"testinstrument2", price: 7.9, amount: 15, valdate: "2017-01-09" };
    var pos19 = { isin: "isin00000003", desc:"testinstrument3", price: 10.1, amount: 5, valdate: "2017-01-01" };
    var pos20 = { isin: "isin00000003", desc:"testinstrument3", price: 10.25, amount: 5, valdate: "2017-01-02" };
    var pos21 = { isin: "isin00000003", desc:"testinstrument3", price: 10.31, amount: 5, valdate: "2017-01-03" };
    var pos22 = { isin: "isin00000003", desc:"testinstrument3", price: 10.28, amount: 5, valdate: "2017-01-04" };
    var pos23 = { isin: "isin00000003", desc:"testinstrument3", price: 10.27, amount: 5, valdate: "2017-01-05" };
    var pos24 = { isin: "isin00000003", desc:"testinstrument3", price: 10.31, amount: 5, valdate: "2017-01-06" };
    var pos25 = { isin: "isin00000003", desc:"testinstrument3", price: 10.26, amount: 5, valdate: "2017-01-07" };
    var pos26 = { isin: "isin00000003", desc:"testinstrument3", price: 10.24, amount: 5, valdate: "2017-01-08" };
    var pos27 = { isin: "isin00000003", desc:"testinstrument3", price: 10.28, amount: 5, valdate: "2017-01-09" };
    var pos28 = { isin: "isin00000004", desc:"testinstrument4", price: 8.1, amount: 10, valdate: "2017-01-01" };
    var pos29 = { isin: "isin00000004", desc:"testinstrument4", price: 8.85, amount: 10, valdate: "2017-01-02" };
    var pos30 = { isin: "isin00000004", desc:"testinstrument4", price: 9.31, amount: 10, valdate: "2017-01-03" };
    var pos31 = { isin: "isin00000004", desc:"testinstrument4", price: 9.28, amount: 10, valdate: "2017-01-04" };
    var pos32 = { isin: "isin00000004", desc:"testinstrument4", price: 9.45, amount: 10, valdate: "2017-01-05" };
    var pos33 = { isin: "isin00000004", desc:"testinstrument4", price: 9.31, amount: 10, valdate: "2017-01-06" };
    var pos34 = { isin: "isin00000004", desc:"testinstrument4", price: 9.5, amount: 10, valdate: "2017-01-07" };
    var pos35 = { isin: "isin00000004", desc:"testinstrument4", price: 10.24, amount: 0, valdate: "2017-01-08" };
    var pos36 = { isin: "isin00000004", desc:"testinstrument4", price: 9.28, amount: 0, valdate: "2017-01-09" };
    let positions: Position[]=[pos1, pos2, pos3, pos4, pos5, pos6, pos7,pos8, pos9, pos10, pos11,
        pos12, pos13, pos14, pos15, pos16, pos17, pos18, pos19, pos20,pos21, pos22, pos23, pos24, pos25, pos27, pos28,
        pos29, pos30, pos31, pos32, pos33, pos34, pos35, pos36]
    return Observable.of(positions);

  }

}
