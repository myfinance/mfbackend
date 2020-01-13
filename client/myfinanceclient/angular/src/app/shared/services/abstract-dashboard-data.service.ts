import {Instrument, InstrumentListModel} from "../../modules/myfinance-tsclient-generated";
import {Subject} from "rxjs";
import {MyFinanceDataService} from "./myfinance-data.service";
import {DashboardService} from "../../modules/dashboard/services/dashboard.service";

export abstract class AbstractDashboardDataService {

  instruments: Array<Instrument> = new Array<Instrument>();
  instrumentSubject: Subject<any> = new Subject<any>();
  protected isInstrumentLoaded = false;
  dataLoadedSubject: Subject<any>;

  constructor(protected myFinanceService: MyFinanceDataService, public dashboardService: DashboardService) {
    this.dataLoadedSubject = new Subject<any>();
    this.dashboardService.handleLoading();
    this.loadDataCall();
  }

  protected abstract loadDataCall();

  protected abstract loadData(): void;

  protected checkDataLoadStatus() {
    if (this.isDataLoadComplete()) {
      this.dashboardService.handleDataLoaded();
      this.dataLoadedSubject.next();
    }
  }

  protected abstract isDataLoadComplete(): boolean;

  getIsInit(): boolean {
    return this.isDataLoadComplete();
  }
}
