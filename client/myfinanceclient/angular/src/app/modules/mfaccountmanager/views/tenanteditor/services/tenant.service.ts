import {Injectable} from '@angular/core';
import {DashboardService} from '../../../../dashboard/services/dashboard.service';
import {MyFinanceDataService} from '../../../../../shared/services/myfinance-data.service';
import {Instrument, InstrumentListModel} from '../../../../myfinance-tsclient-generated';
import InstrumentTypeEnum = Instrument.InstrumentTypeEnum;
import {HttpErrorResponse} from '@angular/common/http';
import {Subject} from 'rxjs/Rx';

@Injectable()
export class TenantService {

  instruments: Array<Instrument> = new Array<Instrument>();
  instrumentSubject: Subject<any> = new Subject<any>();
  selectedinstrumentSubject: Subject<any> = new Subject<any>();
  private isInit = false;
  private isInstrumentLoaded = false;
  selectedTenant: Instrument

  constructor(private myFinanceService: MyFinanceDataService, public dashboardService: DashboardService) {
    this.dashboardService.handleLoading();
    this.loadDataCall();
  }

  private loadDataCall() {
    if (this.myFinanceService.getIsInit()) {
      this.loadData();
    } else {
      this.myFinanceService.configSubject.subscribe(
        () => {
          this.loadData()
        }
      )
    }
    // subscribe to all instrument updates
    this.myFinanceService.instrumentSubject.subscribe(
      () => {
        this.loadData()
      }
    )
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
          this.myFinanceService.printError(errResp);
          this.dashboardService.handleDataNotLoaded(errResp);
        })
  }

  private checkDataLoadStatus() {
    if (this.isInstrumentLoaded) {
      this.dashboardService.handleDataLoaded();
    }
  }

  getIsInit(): boolean {
    return this.isInit;
  }

  getTenants(): Array<Instrument> {
    return this.instruments.filter(i => i.instrumentType === InstrumentTypeEnum.Tenant);
  }

  saveTenant(desc: string) {
    this.myFinanceService.saveTenant(desc).subscribe(
      () => {
        this.myFinanceService.refreshInstruments();
        this.myFinanceService.refreshTenants();
        this.myFinanceService.printSuccess('Mandant gespeichert');
        },
      (errResp) => {
        this.myFinanceService.printError(errResp);
      })
  }

  updateTenant(instrumentId: number, desc: string, isActive: boolean) {
    this.myFinanceService.updateTenant(instrumentId, desc, isActive).subscribe(
      () => {
        this.myFinanceService.refreshInstruments();
        this.myFinanceService.refreshTenants();
        this.myFinanceService.printSuccess('Mandant gespeichert');
        },
      (errResp: HttpErrorResponse) => {
        this.myFinanceService.printError(errResp);
      })
  }


  setSelectedTenant(tenant: Instrument) {
    console.log(tenant.instrumentid)
    this.selectedTenant = tenant;
    this.selectedinstrumentSubject.next()
  }

  getSelectedTenant(): Instrument {
    return this.selectedTenant
  }
}
