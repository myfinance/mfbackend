import {Injectable} from '@angular/core';
import {DashboardService} from '../../../../dashboard/services/dashboard.service';
import {MyFinanceDataService} from '../../../../../shared/services/myfinance-data.service';
import {Instrument, InstrumentListModel, Transaction, TransactionListModel} from '../../../../myfinance-tsclient-generated';
import * as moment from 'moment';
import InstrumentTypeEnum = Instrument.InstrumentTypeEnum;
import {AbstractDashboardDataService} from '../../../../../shared/services/abstract-dashboard-data.service';
import {Subject} from 'rxjs/Rx';

@Injectable()
export class TransactionService extends AbstractDashboardDataService {

  transactions: Array<Transaction> = new Array<Transaction>();
  instruments: Array<Instrument> = new Array<Instrument>();
  transactionSubject: Subject<any> = new Subject<any>();
  instrumentSubject: Subject<any> = new Subject<any>();
  private isTransactionLoaded = false;
  daterange:  Array<Date>;
  private transactionfilter = -1;


  constructor(protected myFinanceService: MyFinanceDataService, public dashboardService: DashboardService) {
    super(myFinanceService, dashboardService);
  }

  protected loadDataCall() {
    if (this.myFinanceService.getIsInit()) {
      this.loadData();
    }
    this.myFinanceService.configSubject.subscribe(
      () => {
        this.loadData()
      }
    )
    // subscribe to all instrument updates
    this.myFinanceService.instrumentSubject.subscribe(
      () => {
        this.loadData()
      }
    )
    // subscribe to all transaction updates
    this.myFinanceService.transactionSubject.subscribe(
      () => {
        this.loadData()
      }
    )
  }

  protected loadData(): void {
    this.dashboardService.handleDataPreparing();

    this.daterange = new Array<Date>();
    this.daterange[0] = new Date(new Date().getFullYear(), new Date().getMonth() - 6, new Date().getDate());
    this.daterange[1] = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
    this.myFinanceService.getTransactions(this.daterange[0], this.daterange[1])
      .subscribe(
        (transactions: TransactionListModel) => {
          this.transactions = transactions.values;
          this.transactionSubject.next();
          this.isTransactionLoaded = true;
          this.checkDataLoadStatus();
        },
        (errResp) => {
          console.error('error', errResp);
          this.dashboardService.handleDataNotLoaded(errResp);

        })

    this.myFinanceService.getInstruments()
      .subscribe(
        (instruments: InstrumentListModel) => {
          this.instruments = instruments.values;
          this.instrumentSubject.next();
          this.isInstrumentLoaded = true;
          this.checkDataLoadStatus();
        },
        (errResp) => {
          console.error('error', errResp);
          this.dashboardService.handleDataNotLoaded(errResp);

        })
  }

  protected isDataLoadComplete(): boolean {
    if (this.isInstrumentLoaded && this.isTransactionLoaded) {
      return true;
    } else {
      return false;
    }
  }

  getTransactions(): Array<Transaction> {
    return this.transactions.filter(i => moment(i.transactiondate, 'YYYY-MM-DD').isSameOrAfter(this.daterange[0]) &&
      moment(i.transactiondate, 'YYYY-MM-DD').isSameOrBefore(this.daterange[1]));
  }

  getInstruments(): Array<Instrument> {
    return this.instruments;
  }

  getGiros(): Array<Instrument> {
    return this.instruments.filter(i => i.instrumentType === InstrumentTypeEnum.Giro);
  }

  getBudgets(): Array<Instrument> {
    return this.instruments.filter(i => i.instrumentType === InstrumentTypeEnum.Budget);
  }

  setDaterange(daterange: Array<Date>) {
    if (daterange != null) {
      this.daterange = daterange;
      // this.loadDataCall();
    }

    // this.configSubject.next();
  }

  getDaterange(): Array<Date> {
    return this.daterange;
  }

  getTransactionfilter(): number {
    return this.transactionfilter;
  }
  setTransactionfilter(transactionfilter: number) {
    this.transactionfilter = transactionfilter;
    this.dataLoadedSubject.next();
  }

  saveIncomeExpenses(desc: string, srcInstrumentId: number, trgInstrumentId: number, value: number, transactionDate: Date) {
    this.myFinanceService.saveIncomeExpenses(desc, srcInstrumentId, trgInstrumentId, value, transactionDate);
  }
}
