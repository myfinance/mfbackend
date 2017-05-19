import {Inject, Injectable} from "@angular/core";

import {Observable} from "rxjs";
import {Instrument} from "../entities/instrument";
import 'rxjs/add/operator/map';
/**
 * Created by xn01598 on 11.05.2017.
 */

@Injectable()
export class InstrumentDummyService{
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

}
