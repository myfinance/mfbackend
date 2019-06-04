import {Injectable, OnDestroy, OnInit} from "@angular/core";
import {DashboardService} from "../../../../dashboard/services/dashboard.service";
import {MyFinanceDataService} from "../../../../../shared/services/myfinance-data.service";
import {Transaction, TransactionListModel} from "../../../../myfinance-tsclient-generated";
import {Subject} from "rxjs";
import * as moment from 'moment';

@Injectable()
export class TransactionService {

  transactions: Array<Transaction> = new Array<Transaction>();
  transactionSubject:Subject<any>= new Subject<any>();
  private isInit:boolean = false;
  start = new Date(new Date().getFullYear(), new Date().getMonth()-6, new Date().getDate());
  end = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
  daterange:  Array<Date> = new Array<Date>();



  constructor(private myFinanceService: MyFinanceDataService, public dashboardService: DashboardService) {
    this.daterange[0] =this.start;
    this.daterange[1] =this.end;
    this.dashboardService.handleLoading();
    if(this.myFinanceService.getIsInit()){
      this.loadData();
    } else {
      this.myFinanceService.transactionSubject.subscribe(
        () => {
          this.loadData()
        }
      )
    }
  }


  private loadData(): void {
    this.myFinanceService.getTransactions(this.start, this.end)
      .subscribe(
        (transactions: TransactionListModel) => {
          this.dashboardService.handleDataPreparing();
          this.transactions = transactions.values;
          this.transactionSubject.next();
          this.dashboardService.handleDataLoaded();
          this.isInit = true;
        },
        (errResp) => {
          console.error('error', errResp);
          this.dashboardService.handleDataNotLoaded(errResp);

        }), (errResp) => {
      console.error('error', errResp);
      this.dashboardService.handleDataNotLoaded(errResp);
    }
  }

  getIsInit(): boolean{
    return this.isInit;
  }

  getTransactions(): Array<Transaction>{
    return this.transactions.filter(i => moment(i.transactiondate, 'YYYY-MM-DD').isSameOrAfter(this.daterange[0]) &&
      moment(i.transactiondate, 'YYYY-MM-DD').isSameOrBefore(this.daterange[1]));
  }

  setDaterange(daterange: Array<Date>){
    this.daterange=daterange;
    this.transactionSubject.next();
  }

  getDaterange(): Array<Date>{
    return this.daterange;
  }
}
