import { Injectable } from '@angular/core';
import {Observable} from "../../../../node_modules/rxjs";
import {
  Cashflow,
  Instrument,
  InstrumentListModel,
  StringListModel, Trade,
  Transaction,
  TransactionListModel
} from "../../modules/myfinance-tsclient-generated";


@Injectable()
export class MockDataProviderService {

  constructor() { }

  getInstruments(): Observable<InstrumentListModel> {

    const now:Date=new Date(Date.now());
    let instrument : Instrument = { instrumentid: 1, description:"testinstrument1", treelastchanged: now, isactive:true, instrumentType:'Equity' };
    let instrument2 : Instrument = { instrumentid: 2, description:"testinstrument2", treelastchanged: now, isactive:true, instrumentType:'Equity' };
    let instruments: Instrument[]=[instrument, instrument2];
    let instrumentList : InstrumentListModel = {values: instruments, url:"mock", id:"mockid"};
    return Observable.of(instrumentList);

  }

  getTransactions(): Observable<TransactionListModel> {

    const now:Date=new Date(Date.now());
    let instrument1 : Instrument = { instrumentid: 1, description:"giro", treelastchanged: now, isactive:true, instrumentType:Instrument.InstrumentTypeEnum.Giro };
    let instrument2 : Instrument = { instrumentid: 2, description:"budget", treelastchanged: now, isactive:true, instrumentType:Instrument.InstrumentTypeEnum.Budget };
    let cashflow1 : Cashflow = {cashflowid: 1, instrument: instrument1, value: 100 };
    let cashflow2 : Cashflow = {cashflowid: 2, instrument: instrument2, value: 100 };
    let cashflows: Cashflow[]=[cashflow1, cashflow2];
    let trades: Trade[]
    let transaction : Transaction = { transactionid: 1, description:"testtransaction1", transactiondate: "2019-01-01", lastchanged: now, transactionType:Transaction.TransactionTypeEnum.INCOMEEXPENSES,
      cashflows:cashflows, trades: trades };
    let transactions: Transaction[]=[transaction];
    let transactionList : TransactionListModel = {values: transactions, url:"mock", id:"mockid"};
    return Observable.of(transactionList);

  }
}



