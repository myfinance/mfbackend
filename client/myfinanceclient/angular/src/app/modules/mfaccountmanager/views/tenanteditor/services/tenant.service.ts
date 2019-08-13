import {Injectable} from "@angular/core";
import {DashboardService} from "../../../../dashboard/services/dashboard.service";
import {MyFinanceDataService} from "../../../../../shared/services/myfinance-data.service";
import {Instrument, InstrumentListModel} from "../../../../myfinance-tsclient-generated";
import {Subject} from "rxjs";
import InstrumentTypeEnum = Instrument.InstrumentTypeEnum;

@Injectable()
export class TenantService {

  instruments: Array<Instrument> = new Array<Instrument>();
  instrumentSubject:Subject<any>= new Subject<any>();
  selectedinstrumentSubject:Subject<any>= new Subject<any>();
  private isInit:boolean = false;
  private isInstrumentLoaded:boolean = false;
  selectedTenant: Instrument

  constructor(private myFinanceService: MyFinanceDataService, public dashboardService: DashboardService) {
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
    if(this.isInstrumentLoaded){
      this.dashboardService.handleDataLoaded();
    }
  }

  getIsInit(): boolean{
    return this.isInit;
  }

  getTenants(): Array<Instrument>{
    return this.instruments.filter(i=>i.instrumentType==InstrumentTypeEnum.Tenant);
  }

  saveTenant(desc: string){
    this.myFinanceService.saveTenant(desc).subscribe(
      ()=>{console.info('success');},
      (errResp) => {
        console.error('error', errResp);

      })
  }

  updateTenant(instrumentId:number, desc: string, isActive: boolean){
    this.myFinanceService.updateTenant(instrumentId, desc, isActive).subscribe(
      ()=>{console.info('success');},
      (errResp) => {
        console.error('error', errResp);

      })
  }


  setSelectedTenant(tenant: Instrument){
    console.log(tenant.instrumentid)
    this.selectedTenant = tenant;
    this.selectedinstrumentSubject.next()
  }

  getSelectedTenant(): Instrument {
    return this.selectedTenant
  }
}
