
import {Inject, Injectable} from "@angular/core";
import {BASE_URL} from "../../app.tokens";
import {Instrument} from "../models/instrument";
import {Position} from "../models/position";
import {HttpClient} from "@angular/common/http";
import {BASE_PATH} from "../../modules/myfinance-tsclient-generated/variables";
import {Observable} from "rxjs/Rx";

@Injectable()
export class MyFinanceDataService{
  constructor(
    private http: HttpClient,
    @Inject(BASE_PATH) private baseUrl: string
  ) {
  }

  find(isin: string): Observable<Instrument[]> {


    //return this.http.get<Instrument[]>(this.baseUrl + '/instruments');
    let instrument : Instrument = { instrumentid: 1, isin: "isin00000001", description:"testinstrument1", treelastchanged: "2017-12-24T17:00:00.000+01:00" };
    let instrument2 : Instrument = { instrumentid: 2, isin: "isin00000002", description:"testinstrument2", treelastchanged: "2017-12-24T17:00:00.000+01:00" };
    let instruments: Instrument[]=[instrument, instrument2]
    let filteredinstruments: Instrument[]=instruments.filter(i=>i.isin.indexOf(isin)>=0);
    return Observable.of(filteredinstruments);

  }

  getPositions(): Observable<Position[]> {

    let pos1 = { isin: "isin00000001", desc:"testinstrument1", price: 1.1, amount: 20, valdate: "2017-01-01" };
    let pos2 = { isin: "isin00000001", desc:"testinstrument1", price: 1.2, amount: 20, valdate: "2017-01-02" };
    let pos3 = { isin: "isin00000001", desc:"testinstrument1", price: 1.3, amount: 20, valdate: "2017-01-03" };
    let pos4 = { isin: "isin00000001", desc:"testinstrument1", price: 1.25, amount: 20, valdate: "2017-01-04" };
    let pos5 = { isin: "isin00000001", desc:"testinstrument1", price: 1.27, amount: 20, valdate: "2017-01-05" };
    let pos6 = { isin: "isin00000001", desc:"testinstrument1", price: 1.31, amount: 20, valdate: "2017-01-06" };
    let pos7 = { isin: "isin00000001", desc:"testinstrument1", price: 1.26, amount: 20, valdate: "2017-01-07" };
    let pos8 = { isin: "isin00000001", desc:"testinstrument1", price: 1.24, amount: 18, valdate: "2017-01-08" };
    let pos9 = { isin: "isin00000001", desc:"testinstrument1", price: 1.28, amount: 18, valdate: "2017-01-09" };
    let pos10 = { isin: "isin00000002", desc:"testinstrument2", price: 8.1, amount: 10, valdate: "2017-01-01" };
    let pos11 = { isin: "isin00000002", desc:"testinstrument2", price: 7.5, amount: 10, valdate: "2017-01-02" };
    let pos12 = { isin: "isin00000002", desc:"testinstrument2", price: 7.4, amount: 10, valdate: "2017-01-03" };
    let pos13 = { isin: "isin00000002", desc:"testinstrument2", price: 7.2, amount: 15, valdate: "2017-01-04" };
    let pos14 = { isin: "isin00000002", desc:"testinstrument2", price: 7.5, amount: 15, valdate: "2017-01-050" };
    let pos15 = { isin: "isin00000002", desc:"testinstrument2", price: 7.6, amount: 15, valdate: "2017-01-06" };
    let pos16 = { isin: "isin00000002", desc:"testinstrument2", price: 7.8, amount: 15, valdate: "2017-01-07" };
    let pos17 = { isin: "isin00000002", desc:"testinstrument2", price: 7.7, amount: 15, valdate: "2017-01-08" };
    let pos18 = { isin: "isin00000002", desc:"testinstrument2", price: 7.9, amount: 15, valdate: "2017-01-09" };
    let pos19 = { isin: "isin00000003", desc:"testinstrument3", price: 10.1, amount: 5, valdate: "2017-01-01" };
    let pos20 = { isin: "isin00000003", desc:"testinstrument3", price: 10.25, amount: 5, valdate: "2017-01-02" };
    let pos21 = { isin: "isin00000003", desc:"testinstrument3", price: 10.31, amount: 5, valdate: "2017-01-03" };
    let pos22 = { isin: "isin00000003", desc:"testinstrument3", price: 10.28, amount: 5, valdate: "2017-01-04" };
    let pos23 = { isin: "isin00000003", desc:"testinstrument3", price: 10.27, amount: 5, valdate: "2017-01-05" };
    let pos24 = { isin: "isin00000003", desc:"testinstrument3", price: 10.31, amount: 5, valdate: "2017-01-06" };
    let pos25 = { isin: "isin00000003", desc:"testinstrument3", price: 10.26, amount: 5, valdate: "2017-01-07" };
    let pos26 = { isin: "isin00000003", desc:"testinstrument3", price: 10.24, amount: 5, valdate: "2017-01-08" };
    let pos27 = { isin: "isin00000003", desc:"testinstrument3", price: 10.28, amount: 5, valdate: "2017-01-09" };
    let pos28 = { isin: "isin00000004", desc:"testinstrument4", price: 8.1, amount: 10, valdate: "2017-01-01" };
    let pos29 = { isin: "isin00000004", desc:"testinstrument4", price: 8.85, amount: 10, valdate: "2017-01-02" };
    let pos30 = { isin: "isin00000004", desc:"testinstrument4", price: 9.31, amount: 10, valdate: "2017-01-03" };
    let pos31 = { isin: "isin00000004", desc:"testinstrument4", price: 9.28, amount: 10, valdate: "2017-01-04" };
    let pos32 = { isin: "isin00000004", desc:"testinstrument4", price: 9.45, amount: 10, valdate: "2017-01-05" };
    let pos33 = { isin: "isin00000004", desc:"testinstrument4", price: 9.31, amount: 10, valdate: "2017-01-06" };
    let pos34 = { isin: "isin00000004", desc:"testinstrument4", price: 9.5, amount: 10, valdate: "2017-01-07" };
    let pos35 = { isin: "isin00000004", desc:"testinstrument4", price: 10.24, amount: 0, valdate: "2017-01-08" };
    let pos36 = { isin: "isin00000004", desc:"testinstrument4", price: 9.28, amount: 0, valdate: "2017-01-09" };
    let positions: Position[]=[pos1, pos2, pos3, pos4, pos5, pos6, pos7,pos8, pos9, pos10, pos11,
      pos12, pos13, pos14, pos15, pos16, pos17, pos18, pos19, pos20,pos21, pos22, pos23, pos24, pos25, pos27, pos28,
      pos29, pos30, pos31, pos32, pos33, pos34, pos35, pos36]
    return Observable.of(positions);

  }

}
