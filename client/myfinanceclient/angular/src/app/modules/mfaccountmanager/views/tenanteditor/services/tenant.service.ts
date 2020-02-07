import {Injectable} from '@angular/core';
import {DashboardService} from '../../../../dashboard/services/dashboard.service';
import {MyFinanceDataService} from '../../../../../shared/services/myfinance-data.service';
import {Instrument, InstrumentListModel} from '../../../../myfinance-tsclient-generated';
import InstrumentTypeEnum = Instrument.InstrumentTypeEnum;
import {HttpErrorResponse} from '@angular/common/http';
import {Subject} from 'rxjs/Rx';
import {AbstractDashboardDataService} from '../../../../../shared/services/abstract-dashboard-data.service';

@Injectable()
export class TenantService extends AbstractDashboardDataService {

  instruments: Array<Instrument> = new Array<Instrument>();
  instrumentSubject: Subject<any> = new Subject<any>();
  selectedinstrumentSubject: Subject<any> = new Subject<any>();
  selectedTenant: Instrument;

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
  }

  protected loadData(): void {
    this.dashboardService.handleDataPreparing();

    this.myFinanceService.getInstruments()
      .subscribe(
        (instruments: InstrumentListModel) => {
          this.instruments = instruments.values;
          this.instrumentSubject.next();
          this.isInstrumentLoaded = true;
          this.checkDataLoadStatus();
        },
        (errResp) => {
          this.myFinanceService.printError(errResp);
          this.dashboardService.handleDataNotLoaded(errResp);
        })
  }

  protected isDataLoadComplete(): boolean {
    if (this.isInstrumentLoaded) {
      return true;
    } else {
      return false;
    }
  }

  getTenants(): Array<Instrument> {
    return this.instruments.filter(i => i.instrumentType === InstrumentTypeEnum.Tenant);
  }

  saveTenant(desc: string) {
    this.myFinanceService.saveTenant(desc)
  }

  updateTenant(instrumentId: number, desc: string, isActive: boolean) {
    this.myFinanceService.updateInstrument(instrumentId, desc, isActive, 'Mandant aktualisiert')
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
