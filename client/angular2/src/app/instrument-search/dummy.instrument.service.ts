import {Inject, Injectable} from "@angular/core";

import {Observable} from "rxjs";
import {Instrument} from "../entities/instrument";
import 'rxjs/add/operator/map';
import {Http} from "@angular/http";
import {BASE_URL} from "../app.tokens";
/**
 * Created by xn01598 on 11.05.2017.
 */

@Injectable()
export class InstrumentDummyService{
  constructor(
    private http: Http,
    @Inject(BASE_URL) private baseUrl: string
  ) {
    console.debug('Viele Grüße aus dem dummy Ctor!');
  }

  find(): Observable<Instrument[]> {

    var instrument = { id: 1, isin: "isin00000001", desc:"testinstrument1", lastUpdate: "2017-12-24T17:00:00.000+01:00" };
    var instrument2 = { id: 2, isin: "isin00000002", desc:"testinstrument2", lastUpdate: "2017-12-24T17:00:00.000+01:00" };
    let instruments: Instrument[]=[instrument, instrument2]
    return Observable.of(instruments);
  }

}
