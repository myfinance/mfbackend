import {Injectable, OnDestroy, OnInit} from "@angular/core";
import {DashboardService} from "../../../../dashboard/services/dashboard.service";
import {MyFinanceDataService} from "../../../../../shared/services/myfinance-data.service";
import {Instrument, InstrumentListModel, Transaction, TransactionListModel} from "../../../../myfinance-tsclient-generated";
import {Subject} from "rxjs";
import * as moment from 'moment';
import InstrumentTypeEnum = Instrument.InstrumentTypeEnum;

@Injectable()
export class TransactionService {

  transactions: Array<Transaction> = new Array<Transaction>();
  instruments: Array<Instrument> = new Array<Instrument>();
  transactionSubject:Subject<any>= new Subject<any>();
  instrumentSubject:Subject<any>= new Subject<any>();
  private isInit:boolean = false;
  private isInstrumentLoaded:boolean = false;
  private isTransactionLoaded:boolean = false;
  start = new Date(new Date().getFullYear(), new Date().getMonth()-6, new Date().getDate());
  end = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
  daterange:  Array<Date> = new Array<Date>();



  constructor(private myFinanceService: MyFinanceDataService, public dashboardService: DashboardService) {
    this.daterange[0] =this.start;
    this.daterange[1] =this.end;
    this.dashboardService.handleLoading();
    this.loadDataCall();
  }

  private loadDataCall(){
    if(this.myFinanceService.getIsInit()){
      this.loadData();
    } else {
      this.myFinanceService.configSubject.subscribe(
        () => {
          this.loadData()
        }
      )
    }
  }

  private loadData(): void {
    this.dashboardService.handleDataPreparing();

    this.myFinanceService.getTransactions(this.daterange[0], this.daterange[1])
      .subscribe(
        (transactions: TransactionListModel) => {
          this.transactions = transactions.values;
          this.transactionSubject.next();
          this.isInit = true;
          this.isTransactionLoaded = true;
          this.checkDataLoadStatus();
        },
        (errResp) => {
          console.error('error', errResp);
          this.dashboardService.handleDataNotLoaded(errResp);

        }), (errResp) => {
      console.error('error', errResp);
      this.dashboardService.handleDataNotLoaded(errResp);
    }

    this.myFinanceService.getInstruments()
      .subscribe(
        (instruments: InstrumentListModel) => {
          this.instruments = instruments.values;
          this.instrumentSubject.next();
          this.isInit = true;
          this.isInstrumentLoaded = true;
          this.checkDataLoadStatus();
        },
        (errResp) => {
          console.error('error', errResp);
          this.dashboardService.handleDataNotLoaded(errResp);

        }), (errResp) => {
      console.error('error', errResp);
      this.dashboardService.handleDataNotLoaded(errResp);
    }
  }

  private checkDataLoadStatus(){
    if(this.isInstrumentLoaded && this.isTransactionLoaded){
      this.dashboardService.handleDataLoaded();
    }
  }

  getIsInit(): boolean{
    return this.isInit;
  }

  getTransactions(): Array<Transaction>{
    return this.transactions.filter(i => moment(i.transactiondate, 'YYYY-MM-DD').isSameOrAfter(this.daterange[0]) &&
      moment(i.transactiondate, 'YYYY-MM-DD').isSameOrBefore(this.daterange[1]));
  }

  getInstruments(): Array<Instrument>{
    return this.instruments;
  }

  getGiros(): Array<Instrument>{
    return this.instruments.filter(i => i.instrumentType == InstrumentTypeEnum.Giro);
  }

  getBudgets(): Array<Instrument>{
    return this.instruments.filter(i => i.instrumentType == InstrumentTypeEnum.Budget);
  }

  setDaterange(daterange: Array<Date>){
    if(daterange != null){
      this.daterange=daterange;
      this.loadDataCall();
    }

    //this.configSubject.next();
  }

  getDaterange(): Array<Date>{
    return this.daterange;
  }

  saveIncomeExpenses(desc: string, srcInstrumentId: number, trgInstrumentId: number, value: number, transactionDate: Date) {
    this.myFinanceService.saveIncomeExpenses(desc, srcInstrumentId, trgInstrumentId, value, transactionDate);
  }
}
