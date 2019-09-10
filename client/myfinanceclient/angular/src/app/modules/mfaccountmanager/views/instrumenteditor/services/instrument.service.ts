import {Injectable} from '@angular/core';
import {DashboardService} from '../../../../dashboard/services/dashboard.service';
import {MyFinanceDataService} from '../../../../../shared/services/myfinance-data.service';
import {Instrument, InstrumentListModel} from '../../../../myfinance-tsclient-generated';
import InstrumentTypeEnum = Instrument.InstrumentTypeEnum;
import {Subject} from 'rxjs/Rx';

@Injectable()
export class InstrumentService {

  instruments: Array<Instrument> = new Array<Instrument>();
  instrumentSubject: Subject<any> = new Subject<any>();
  selectedinstrumentSubject: Subject<any> = new Subject<any>();
  private isInit = false;
  private isInstrumentLoaded = false;
  selectedInstrument: Instrument;

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

    this.myFinanceService.getInstrumentsForTenant()
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

  getInstruments(): Array<Instrument> {
    return this.instruments;
  }

  private saveGiro(desc: string) {
    this.myFinanceService.saveGiro(desc)
  }

  private saveBudget(desc: string, budgetGroupId: number) {
    this.myFinanceService.saveBudget(desc, budgetGroupId)
  }

  saveInstrument(instrument: Instrument) {
    if (instrument.instrumentType === InstrumentTypeEnum.Giro) {
      this.myFinanceService.saveGiro(instrument.description)
    } else if (instrument.instrumentType === InstrumentTypeEnum.Budget) {
      // this.myFinanceService.saveBudget(instrument.description)
    }
  }

  setSelectedInstrument(instrument: Instrument) {
    console.log(instrument.instrumentid)
    this.selectedInstrument = instrument;
    this.selectedinstrumentSubject.next()
  }

  getSelectedInstrument(): Instrument {
    return this.selectedInstrument
  }
}
