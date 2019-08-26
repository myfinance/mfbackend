
import {Injectable, OnInit} from "@angular/core";
import {Position} from "../models/position";
import {Observable} from "rxjs/Rx";
import {ConfigService} from "./config.service";
import {MockDataProviderService} from "./mock-data-provider.service";
import {Instrument, InstrumentListModel, TransactionListModel} from "../../modules/myfinance-tsclient-generated";
import {MyFinanceWrapperService} from "./my-finance-wrapper.service";
import {Subject} from "rxjs";
import {DatePipe} from "@angular/common";
import * as moment from 'moment';
import InstrumentTypeEnum = Instrument.InstrumentTypeEnum;

@Injectable()
export class MyFinanceDataService {
  private mock:MockDataProviderService = new MockDataProviderService()
  private isMock:boolean = true
  private currentEnv:string
  private isInit:boolean = false
  configSubject:Subject<any>= new Subject<any>();
  instrumentSubject:Subject<any>= new Subject<any>();


  constructor(
    private myfinanceService: MyFinanceWrapperService,
    private configService: ConfigService
  ) {
    if(this.configService.getIsInit()){
      this.loadConfig();
    } else {
      this.configService.configLoaded.subscribe(
        () => {
          this.loadConfig();
        },
        (errResp) => {
          console.error('error', errResp);
        }
      );
    }
  }

  private loadConfig(){
    this.isInit = true;
    if(this.configService.get('currentZone').identifier.match("mock")){
      this.isMock = true
    } else {
      this.isMock = false
    }
    this.myfinanceService.setBasePath(this.configService.get('currentZone').url);
    this.currentEnv = this.configService.getCurrentEnv();
    this.configSubject.next();
  }

  getIsInit(): boolean{
    return this.isInit;
  }

  getTransactions(start:Date, end:Date): Observable<TransactionListModel> {
    if(!this.isInit) {
      return null;
    }
    else if(this.isMock){
      return this.mock.getTransactions()
    }
    return this.myfinanceService.getTransactionList_envID_startdate_enddate(this.currentEnv, this.getDateString(start), this.getDateString(end));

  }

  private getDateString(date:Date):string{

    return new DatePipe("de-De").transform(date, 'yyyy-MM-dd');
  }

  refreshInstruments(){
    this.instrumentSubject.next();
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
        console.info('saved');
      },
      (errResp) => {
        console.error('error', errResp);
      }
    );
  }

  saveTenant(desc: string): Observable<any>{
    return this.myfinanceService.addTenant_envID_description(this.currentEnv, desc);
  }

  updateTenant(id: number, desc: string, isActive: boolean): Observable<any>{
    return this.myfinanceService.updateInstrument_envID_id_description_isactive(this.currentEnv, id, desc, isActive);
  }

  saveGiro(desc: string){
    this.myfinanceService.addGiro_envID_description_tenantId(this.currentEnv, desc, this.configService.getCurrentTenant().instrumentid)
  }

  saveBudget(desc: string, budgetGroupId: number){
    this.myfinanceService.addBudget_envID_description_budgetGroupId(this.currentEnv, desc, budgetGroupId)
  }

  getInstruments(): Observable<InstrumentListModel> {

    if(this.configService.get('currentZone').identifier.match("mock")){
      return this.mock.getInstruments()
    }
    this.myfinanceService.setBasePath(this.configService.get('currentZone').url);

    return this.myfinanceService.getInstrumentList_envID(this.configService.getCurrentEnv());

  }



  getPositions(): Observable<Position[]> {

    let pos1 = { isin: "isin00000001", desc:"testinstrument1", price: 1.1, amount: 20, valdate: "2017-01-01" };
    let pos2 = { isin: "isin00000001", desc:"testinstrument1", price: 1.2, amount: 20, valdate: "2017-01-02" };
    let pos3 = { isin: "isin00000001", desc:"testinstrument1", price: 1.3, amount: 20, valdate: "2017-01-03" };
    let pos4 = { isin: "isin00000001", desc:"testinstrument1", price: 1.25, amount: 20, valdate: "2017-01-04" };
    let pos5 = { isin: "isin00000001", desc:"testinstrument1", price: 1.27, amount: 20, valdate: "2017-01-05" };
    let pos6 = { isin: "isin00000001", desc:"testinstrument1", price: 1.31, amount: 20, valdate: "2017-01-06" };
    let pos7 = { isin: "isin00000001", desc:"testinstrument1", price: 1.26, amount: 20, valdate: "2017-01-07" };
    let pos8 = { isin: "isin00000001", desc:"testinstrument1", price: 1.24, amount: 18, valdate: "2017-01-08" };
    let pos9 = { isin: "isin00000001", desc:"testinstrument1", price: 1.28, amount: 18, valdate: "2017-01-09" };
    let pos10 = { isin: "isin00000002", desc:"testinstrument2", price: 8.1, amount: 10, valdate: "2017-01-01" };
    let pos11 = { isin: "isin00000002", desc:"testinstrument2", price: 7.5, amount: 10, valdate: "2017-01-02" };
    let pos12 = { isin: "isin00000002", desc:"testinstrument2", price: 7.4, amount: 10, valdate: "2017-01-03" };
    let pos13 = { isin: "isin00000002", desc:"testinstrument2", price: 7.2, amount: 15, valdate: "2017-01-04" };
    let pos14 = { isin: "isin00000002", desc:"testinstrument2", price: 7.5, amount: 15, valdate: "2017-01-050" };
    let pos15 = { isin: "isin00000002", desc:"testinstrument2", price: 7.6, amount: 15, valdate: "2017-01-06" };
    let pos16 = { isin: "isin00000002", desc:"testinstrument2", price: 7.8, amount: 15, valdate: "2017-01-07" };
    let pos17 = { isin: "isin00000002", desc:"testinstrument2", price: 7.7, amount: 15, valdate: "2017-01-08" };
    let pos18 = { isin: "isin00000002", desc:"testinstrument2", price: 7.9, amount: 15, valdate: "2017-01-09" };
    let pos19 = { isin: "isin00000003", desc:"testinstrument3", price: 10.1, amount: 5, valdate: "2017-01-01" };
    let pos20 = { isin: "isin00000003", desc:"testinstrument3", price: 10.25, amount: 5, valdate: "2017-01-02" };
    let pos21 = { isin: "isin00000003", desc:"testinstrument3", price: 10.31, amount: 5, valdate: "2017-01-03" };
    let pos22 = { isin: "isin00000003", desc:"testinstrument3", price: 10.28, amount: 5, valdate: "2017-01-04" };
    let pos23 = { isin: "isin00000003", desc:"testinstrument3", price: 10.27, amount: 5, valdate: "2017-01-05" };
    let pos24 = { isin: "isin00000003", desc:"testinstrument3", price: 10.31, amount: 5, valdate: "2017-01-06" };
    let pos25 = { isin: "isin00000003", desc:"testinstrument3", price: 10.26, amount: 5, valdate: "2017-01-07" };
    let pos26 = { isin: "isin00000003", desc:"testinstrument3", price: 10.24, amount: 5, valdate: "2017-01-08" };
    let pos27 = { isin: "isin00000003", desc:"testinstrument3", price: 10.28, amount: 5, valdate: "2017-01-09" };
    let pos28 = { isin: "isin00000004", desc:"testinstrument4", price: 8.1, amount: 10, valdate: "2017-01-01" };
    let pos29 = { isin: "isin00000004", desc:"testinstrument4", price: 8.85, amount: 10, valdate: "2017-01-02" };
    let pos30 = { isin: "isin00000004", desc:"testinstrument4", price: 9.31, amount: 10, valdate: "2017-01-03" };
    let pos31 = { isin: "isin00000004", desc:"testinstrument4", price: 9.28, amount: 10, valdate: "2017-01-04" };
    let pos32 = { isin: "isin00000004", desc:"testinstrument4", price: 9.45, amount: 10, valdate: "2017-01-05" };
    let pos33 = { isin: "isin00000004", desc:"testinstrument4", price: 9.31, amount: 10, valdate: "2017-01-06" };
    let pos34 = { isin: "isin00000004", desc:"testinstrument4", price: 9.5, amount: 10, valdate: "2017-01-07" };
    let pos35 = { isin: "isin00000004", desc:"testinstrument4", price: 10.24, amount: 0, valdate: "2017-01-08" };
    let pos36 = { isin: "isin00000004", desc:"testinstrument4", price: 9.28, amount: 0, valdate: "2017-01-09" };
    let positions: Position[]=[pos1, pos2, pos3, pos4, pos5, pos6, pos7,pos8, pos9, pos10, pos11,
      pos12, pos13, pos14, pos15, pos16, pos17, pos18, pos19, pos20,pos21, pos22, pos23, pos24, pos25, pos27, pos28,
      pos29, pos30, pos31, pos32, pos33, pos34, pos35, pos36]
    return Observable.of(positions);

  }

}
