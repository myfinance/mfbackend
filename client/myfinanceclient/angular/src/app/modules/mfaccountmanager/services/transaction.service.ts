import {Injectable} from "@angular/core";
import {TransactionListModel} from "../../myfinance-tsclient-generated";
import {MyFinanceDataService} from "../../../shared/services/myfinance-data.service";
import {Observable} from "rxjs";

@Injectable()
export class TransactionService{


  transactionData: any;

  constructor(private myFinanceService: MyFinanceDataService) { }

  search(): Observable<any>{
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
    return Observable.of(this.transactionData)
  }


}
