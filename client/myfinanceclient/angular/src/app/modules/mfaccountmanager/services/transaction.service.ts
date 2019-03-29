import {Injectable, OnDestroy} from "@angular/core";
import {Subscription} from "rxjs";
import {InstrumentListModel, MyFinanceService, TransactionListModel} from "../../myfinance-tsclient-generated";
import {ConfigService} from "../../../shared/services/config.service";
import {MyFinanceDataService} from "../../../shared/services/myfinance-data.service";

@Injectable()
export class TransactionService implements OnDestroy {

  private _alive = true;

  private _subscriptions: Subscription[] = [];


  transactionData: any;

  constructor(
    private myFinanceService: MyFinanceDataService) { }

  search(): void{
    this
      .myFinanceService.getTransactions()
      .subscribe(
        (transactions: TransactionListModel) => {
          this.transactionData = transactions.values;
        },
        (errResp) => {
          console.error('error', errResp);

        }
      );
  }

  refreshTransactionData(): void {

    // Cancel all pending requests.
    this._clearSubscriptions();

    this._subscriptions.push(
      this.myFinanceService.getTransactions()
        .takeWhile(() => this._alive)
        .subscribe(
          result => {
            this.transactionData = result;
          },
          error => {
            this.transactionData = null;
          }
        )
    );
  }


  private _clearSubscriptions(): void {
    this._subscriptions.forEach(sub => sub.unsubscribe());
    this._subscriptions = [];
  }

  ngOnDestroy() {
    this._alive = false;
  }

  private _selectedTransaction: any;
  set selectedTransaction(selectedTransaction: any) {
    this._selectedTransaction = selectedTransaction;
    if (selectedTransaction) {
      this.refreshTransactionData();
    } else {
      this.transactionData = null;
    }
  }
  get selectedTransaction(): any {
    return this._selectedTransaction;
  }

}
