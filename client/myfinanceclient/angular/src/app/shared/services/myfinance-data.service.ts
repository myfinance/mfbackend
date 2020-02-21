import {Injectable, OnInit} from '@angular/core';
import {Position} from '../models/position';
import {Observable, Subject} from 'rxjs/Rx';
import {ConfigService} from './config.service';
import {MockDataProviderService} from './mock-data-provider.service';
import {Instrument, InstrumentListModel, TransactionListModel} from '../../modules/myfinance-tsclient-generated';
import {MyFinanceWrapperService} from './my-finance-wrapper.service';
import {DatePipe} from '@angular/common';
import * as moment from 'moment';
import InstrumentTypeEnum = Instrument.InstrumentTypeEnum;
import {ToastrService} from 'ngx-toastr';
import {HttpErrorResponse} from '@angular/common/http';

@Injectable()
export class MyFinanceDataService {
  private mock: MockDataProviderService = new MockDataProviderService()
  private isMock = true
  private currentEnv: string
  private isInit = false
  configSubject: Subject<any> = new Subject<any>();
  instrumentSubject: Subject<any> = new Subject<any>();
  transactionSubject: Subject<any> = new Subject<any>();


  constructor(
    private toastr: ToastrService,
    private myfinanceService: MyFinanceWrapperService,
    private configService: ConfigService
  ) {
    if (this.configService.getIsInit()) {
      this.loadConfig();
    }
    this.configService.configLoaded.subscribe(
      () => {
        this.loadConfig();
      },
      (errResp) => {
        console.error('error', errResp);
      }
    );
  }

  private loadConfig() {
    this.isInit = true;
    if (this.configService.get('currentZone').identifier.match('mock')) {
      this.isMock = true
    } else {
      this.isMock = false
    }
    this.myfinanceService.setBasePath(this.configService.get('currentZone').url);
    this.currentEnv = this.configService.getCurrentEnv();
    this.configSubject.next();
  }

  getIsInit(): boolean {
    return this.isInit;
  }

  getTransactions(start: Date, end: Date): Observable<TransactionListModel> {
    if (!this.isInit) {
      return null;
    } else if (this.isMock) {
      return this.mock.getTransactions()
    }
    return this.myfinanceService.getTransactionList_envID_startdate_enddate(
      this.currentEnv, this.getDateString(start), this.getDateString(end));
  }

  private getDateString(date: Date): string {

    return new DatePipe('de-De').transform(date, 'yyyy-MM-dd');
  }

  saveIncomeExpenses(desc: string, srcInstrumentId: number, trgInstrumentId: number, value: number, transactionDate: Date) {

    this.myfinanceService.addIncomeExpense_envID_description_accId_budgetId_value_transactiondate(
      this.currentEnv,
      desc,
      srcInstrumentId,
      trgInstrumentId,
      value,
      moment(transactionDate).format('YYYY-MM-DD')).subscribe(
      () => {
        this.transactionSubject.next();
        this.printSuccess('Transaktion gespeichert');
      },
      (errResp) => {
        this.printError(errResp);
      }
    );
  }

  saveTenant(desc: string) {
    this.myfinanceService.addTenant_envID_description(this.currentEnv, desc).subscribe(
      () => {
        this.instrumentSubject.next();
        this.configService.loadTenants();
        this.printSuccess('Mandant gespeichert');
      },
      (errResp) => {
        this.printError(errResp);
      });
  }

  updateInstrument(id: number, desc: string, isActive: boolean, msg: string) {
    this.myfinanceService.updateInstrument_envID_id_description_isactive(this.currentEnv, id, desc, isActive).subscribe(
      () => {
        this.instrumentSubject.next();
        this.configService.loadTenants();
        this.printSuccess(msg);
      },
      (errResp: HttpErrorResponse) => {
        this.printError(errResp);
      });
  }

  saveGiro(desc: string) {
    this.myfinanceService.addGiro_envID_description_tenantId(
      this.currentEnv, desc, this.configService.getCurrentTenant().instrumentid).subscribe(
      () => {
        this.instrumentSubject.next();
        this.printSuccess('Girokonto gespeichert');
      },
      (errResp) => {
        this.printError(errResp);
      })
  }

  saveBudget(desc: string, budgetGroupId: number){
    this.myfinanceService.addBudget_envID_description_budgetGroupId(
      this.currentEnv, desc, budgetGroupId).subscribe(
      () => {
        this.instrumentSubject.next();
        this.printSuccess('Budget gespeichert');
      },
      (errResp) => {
        this.printError(errResp);
      })
  }

  getInstruments(): Observable<InstrumentListModel> {

    if (this.configService.get('currentZone').identifier.match('mock')) {
      return this.mock.getInstruments()
    }
    this.myfinanceService.setBasePath(this.configService.get('currentZone').url);

    return this.myfinanceService.getInstrumentList_envID(this.configService.getCurrentEnv());

  }

  getInstrumentsForTenant(): Observable<InstrumentListModel> {
    if (this.configService.get('currentZone').identifier.match('mock')) {
      return this.mock.getInstruments()
    }
    this.myfinanceService.setBasePath(this.configService.get('currentZone').url);

    return this.myfinanceService.getInstrumentForTenantList_envID_tenant(this.configService.getCurrentEnv(),
      this.configService.getCurrentTenant().instrumentid);
  }

  getInstrumentsPerType(instrumentType: InstrumentTypeEnum): Observable<InstrumentListModel> {
    if (this.configService.get('currentZone').identifier.match('mock')) {
      return this.mock.getInstruments()
    }
    this.myfinanceService.setBasePath(this.configService.get('currentZone').url);

    return this.myfinanceService.getInstrumentPerTypeList_envID_tenant_instrumenttype(this.configService.getCurrentEnv(),
      this.configService.getCurrentTenant().instrumentid, instrumentType);
  }

  printError(errResp: HttpErrorResponse) {
    console.error('error', errResp);
    let errrormsg: string = errResp.error.toString();
    if (errrormsg.length > 100) {
      errrormsg = errrormsg.substring(0, 100)
    }
    this.toastr.warning(errrormsg, 'Error', {timeOut: 15000 });
  }

  printInfo(title: string, msg: string) {
    this.toastr.success(msg, title, {timeOut: 2000});
  }

  printSuccess(msg: string) {
    this.printInfo('Success', msg);
  }

  getPositions(): Observable<Position[]> {

    const pos1 = { isin: 'isin00000001', desc: 'testinstrument1', price: 1.1, amount: 20, valdate: '2017-01-01' };
    const pos2 = { isin: 'isin00000001', desc: 'testinstrument1', price: 1.2, amount: 20, valdate: '2017-01-02' };
    const pos3 = { isin: 'isin00000001', desc: 'testinstrument1', price: 1.3, amount: 20, valdate: '2017-01-03' };
    const pos4 = { isin: 'isin00000001', desc: 'testinstrument1', price: 1.25, amount: 20, valdate: '2017-01-04' };
    const pos5 = { isin: 'isin00000001', desc: 'testinstrument1', price: 1.27, amount: 20, valdate: '2017-01-05' };
    const pos6 = { isin: 'isin00000001', desc: 'testinstrument1', price: 1.31, amount: 20, valdate: '2017-01-06' };
    const pos7 = { isin: 'isin00000001', desc: 'testinstrument1', price: 1.26, amount: 20, valdate: '2017-01-07' };
    const pos8 = { isin: 'isin00000001', desc: 'testinstrument1', price: 1.24, amount: 18, valdate: '2017-01-08' };
    const pos9 = { isin: 'isin00000001', desc: 'testinstrument1', price: 1.28, amount: 18, valdate: '2017-01-09' };
    const pos10 = { isin: 'isin00000002', desc: 'testinstrument2', price: 8.1, amount: 10, valdate: '2017-01-01' };
    const pos11 = { isin: 'isin00000002', desc: 'testinstrument2', price: 7.5, amount: 10, valdate: '2017-01-02' };
    const pos12 = { isin: 'isin00000002', desc: 'testinstrument2', price: 7.4, amount: 10, valdate: '2017-01-03' };
    const pos13 = { isin: 'isin00000002', desc: 'testinstrument2', price: 7.2, amount: 15, valdate: '2017-01-04' };
    const pos14 = { isin: 'isin00000002', desc: 'testinstrument2', price: 7.5, amount: 15, valdate: '2017-01-050' };
    const pos15 = { isin: 'isin00000002', desc: 'testinstrument2', price: 7.6, amount: 15, valdate: '2017-01-06' };
    const pos16 = { isin: 'isin00000002', desc: 'testinstrument2', price: 7.8, amount: 15, valdate: '2017-01-07' };
    const pos17 = { isin: 'isin00000002', desc: 'testinstrument2', price: 7.7, amount: 15, valdate: '2017-01-08' };
    const pos18 = { isin: 'isin00000002', desc: 'testinstrument2', price: 7.9, amount: 15, valdate: '2017-01-09' };
    const pos19 = { isin: 'isin00000003', desc: 'testinstrument3', price: 10.1, amount: 5, valdate: '2017-01-01' };
    const pos20 = { isin: 'isin00000003', desc: 'testinstrument3', price: 10.25, amount: 5, valdate: '2017-01-02' };
    const pos21 = { isin: 'isin00000003', desc: 'testinstrument3', price: 10.31, amount: 5, valdate: '2017-01-03' };
    const pos22 = { isin: 'isin00000003', desc: 'testinstrument3', price: 10.28, amount: 5, valdate: '2017-01-04' };
    const pos23 = { isin: 'isin00000003', desc: 'testinstrument3', price: 10.27, amount: 5, valdate: '2017-01-05' };
    const pos24 = { isin: 'isin00000003', desc: 'testinstrument3', price: 10.31, amount: 5, valdate: '2017-01-06' };
    const pos25 = { isin: 'isin00000003', desc: 'testinstrument3', price: 10.26, amount: 5, valdate: '2017-01-07' };
    const pos26 = { isin: 'isin00000003', desc: 'testinstrument3', price: 10.24, amount: 5, valdate: '2017-01-08' };
    const pos27 = { isin: 'isin00000003', desc: 'testinstrument3', price: 10.28, amount: 5, valdate: '2017-01-09' };
    const pos28 = { isin: 'isin00000004', desc: 'testinstrument4', price: 8.1, amount: 10, valdate: '2017-01-01' };
    const pos29 = { isin: 'isin00000004', desc: 'testinstrument4', price: 8.85, amount: 10, valdate: '2017-01-02' };
    const pos30 = { isin: 'isin00000004', desc: 'testinstrument4', price: 9.31, amount: 10, valdate: '2017-01-03' };
    const pos31 = { isin: 'isin00000004', desc: 'testinstrument4', price: 9.28, amount: 10, valdate: '2017-01-04' };
    const pos32 = { isin: 'isin00000004', desc: 'testinstrument4', price: 9.45, amount: 10, valdate: '2017-01-05' };
    const pos33 = { isin: 'isin00000004', desc: 'testinstrument4', price: 9.31, amount: 10, valdate: '2017-01-06' };
    const pos34 = { isin: 'isin00000004', desc: 'testinstrument4', price: 9.5, amount: 10, valdate: '2017-01-07' };
    const pos35 = { isin: 'isin00000004', desc: 'testinstrument4', price: 10.24, amount: 0, valdate: '2017-01-08' };
    const pos36 = { isin: 'isin00000004', desc: 'testinstrument4', price: 9.28, amount: 0, valdate: '2017-01-09' };
    const positions: Position[] = [pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9, pos10, pos11,
      pos12, pos13, pos14, pos15, pos16, pos17, pos18, pos19, pos20, pos21, pos22, pos23, pos24, pos25, pos27, pos28,
      pos29, pos30, pos31, pos32, pos33, pos34, pos35, pos36]
    return Observable.of(positions);

  }



}
