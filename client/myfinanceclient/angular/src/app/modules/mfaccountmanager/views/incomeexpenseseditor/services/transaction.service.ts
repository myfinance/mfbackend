import {Injectable, OnDestroy, OnInit} from "@angular/core";
import {DashboardService} from "../../../../dashboard/services/dashboard.service";
import {MyFinanceDataService} from "../../../../../shared/services/myfinance-data.service";
import {Transaction, TransactionListModel} from "../../../../myfinance-tsclient-generated";
import {Subject} from "rxjs";

@Injectable()
export class TransactionService {

  transactions: Array<Transaction>;
  transactionSubject:Subject<any>= new Subject<any>();
  private isInit:boolean = false

  constructor(private myFinanceService: MyFinanceDataService, public dashboardService: DashboardService) {
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
    this.myFinanceService.getTransactions()
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
    return this.transactions;
  }
}
