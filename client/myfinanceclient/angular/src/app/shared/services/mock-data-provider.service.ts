import { Injectable } from '@angular/core';
import {Observable} from "../../../../node_modules/rxjs";
import {Instrument, InstrumentListModel} from "../../modules/myfinance-tsclient-generated";


@Injectable()
export class MockDataProviderService {

  constructor() { }

  getInstruments(): Observable<InstrumentListModel> {

    const now:Date=new Date(Date.now());
    let instrument : Instrument = { instrumentid: 1, description:"testinstrument1", treelastchanged: now, isactive:true };
    let instrument2 : Instrument = { instrumentid: 2, description:"testinstrument2", treelastchanged: now, isactive:true };
    let instruments: Instrument[]=[instrument, instrument2];
    let instrumentList : InstrumentListModel = {values: instruments, url:"mock", id:"mockid"};
    return Observable.of(instrumentList);

  }

}
