import {Injectable} from "@angular/core";
import {DashboardService} from "../../../../dashboard/services/dashboard.service";
import {MyFinanceDataService} from "../../../../../shared/services/myfinance-data.service";
import {Instrument, InstrumentListModel} from "../../../../myfinance-tsclient-generated";
import {Subject} from "rxjs";

@Injectable()
export class InstrumentService {

  instruments: Array<Instrument> = new Array<Instrument>();
  instrumentSubject:Subject<any>= new Subject<any>();
  private isInit:boolean = false;
  private isInstrumentLoaded:boolean = false;

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

  getInstruments(): Array<Instrument>{
    return this.instruments;
  }
}
