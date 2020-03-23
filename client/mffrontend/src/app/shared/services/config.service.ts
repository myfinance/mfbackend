import { Injectable } from '@angular/core';
import {ConfigModel} from '../models/config.model';
import {HttpClient} from '@angular/common/http';
import {Instrument, InstrumentListModel, StringListModel} from '../../modules/myfinance-tsclient-generated';
import {MyFinanceWrapperService} from './my-finance-wrapper.service';
import {Observable, Subject} from '../../../../node_modules/rxjs';

@Injectable()
export class ConfigService {

  private _config: ConfigModel;
  environments: string[]
  private currentEnv: string
  configLoaded: Subject<any> = new Subject<any>()
  private isInit = false
  tenants: Instrument[]
  currentTenant: Instrument

  constructor(private _http: HttpClient, private myfinanceService: MyFinanceWrapperService) {  }

  /**
   * Loads the configuration.
   */
  load(): void {

    this._config = null;

    this._http
      .get('assets/config.json')
      .subscribe((data: ConfigModel) => {
        this._config = data;

        // Check if zone is saved in local storage.
        // Set the current zone to the saved zone or else
        // set it to the default zone in the configuration.
        const zone = localStorage.getItem('mfzone');
        if (zone) {
          this.setCurrentZone(zone);
        } else {
          this.setCurrentZone(data.defaultZone);
        }
        this.isInit = true;
      });
  }

  getIsInit(): boolean {
    return this.isInit;
  }

  /**
   * deprecated because due to async load function it is not sure the _config is already initialized. better return an observable
   * @param property
   */
  get(property: string): any {
    let value = this._config;
    for (const p of property.split('.')) {
      value = value[p];
    }
    return value;
  }

  getCurrentEnv() {
    return this.currentEnv
  }

  getCurrentTenant() {
    return this.currentTenant
  }

  setCurrentTenant(tenant: Instrument): void {
    this.currentTenant = tenant;
    // Additionally save the zone in the local storage.
    localStorage.setItem('tenant', tenant.instrumentid.toString());
    console.info('set tenant');
    this.configLoaded.next(true);
  }

  setCurrentZone(identifier: string): void {
    for (const zone of this._config.zones) {
      if (zone.identifier === identifier) {
        this._config.currentZone = zone;
        // Additionally save the zone in the local storage.
        localStorage.setItem('mfzone', identifier);
        this.loadEnvironments()
      }
    }
  }

  setCurrentEnv(env: string): void {
    this.currentEnv = env;
    // Additionally save the zone in the local storage.
    localStorage.setItem('env', env);
    this.loadTenants();
  }

  getDefaultEnv(): string {
    const currentZone = localStorage.getItem('mfzone');
    for (const zone of this._config.zones) {
      if (zone.identifier === currentZone) {
        return zone.defaultEnvironment;
      }
    }
  }

  private getMockEnvironments(): Observable<StringListModel> {
    const envs: string[] = ['enva', 'envb'];
    const envList: StringListModel = {values: envs, url: 'mock', id: 'mockid'};
    return Observable.of(envList);
  }

  /**
   * to avoid circuöar dependency the environment request can not be made via dataservice
   * @returns {Observable<StringListModel>}
   */
  private getEnvironmentProvider(): Observable<StringListModel> {

    if (this.get('currentZone').identifier.match('mock')) {
      return this.getMockEnvironments()
    }
    this.myfinanceService.setBasePath(this.get('currentZone').url)
    return this.myfinanceService.getEnvironmentList();

  }

  private getMockTenants(): Observable<InstrumentListModel> {
    const instrument: Instrument = {
      instrumentid: 1,
      description: 'tenant1',
      isactive: true,
      instrumentType: Instrument.InstrumentTypeEnum.Tenant,
      treelastchanged: new Date() };
    const instrument2: Instrument = {
      instrumentid: 2,
      description: 'tenant2',
      isactive: true,
      instrumentType: Instrument.InstrumentTypeEnum.Tenant,
      treelastchanged: new Date() };
    const tenants: Instrument[] = [instrument, instrument2];
    const envList: InstrumentListModel = {values: tenants, url: 'mock', id: 'mockid'};
    return Observable.of(envList);
  }

  /**
   * to avoid circuöar dependency the environment request can not be made via dataservice
   * @returns {Observable<StringListModel>}
   */
  private getTenantProvider(): Observable<InstrumentListModel> {

    if (this.get('currentZone').identifier.match('mock')) {
      return this.getMockTenants()
    }
    this.myfinanceService.setBasePath(this.get('currentZone').url)
    return this.myfinanceService.getTenantList_envID(this.getCurrentEnv());

  }

  private loadEnvironments() {
    this.getEnvironmentProvider().subscribe(
      (environments: StringListModel) => {
        this.environments = environments.values;
        // Check if environment is saved in local storage.
        // Set the current environment to the saved env or else
        // set it to the default environment in the configuration.
        const env = localStorage.getItem('env');
        if (env && this.environments.includes(env)) {
          this.setCurrentEnv(env);
        } else if (this.environments.includes(this.getDefaultEnv())) {
          this.setCurrentEnv(this.getDefaultEnv());
        } else {
          this.setCurrentEnv(this.environments[0])
        }
      },
      (errResp) => {
        console.error('error', errResp);
      }
    );
  }

  loadTenants() {
    this.getTenantProvider().subscribe(
      (tenants: InstrumentListModel) => {
        this.tenants = tenants.values.filter(i => i.isactive);

        const tenant = localStorage.getItem('tenant');
        if (tenant) {
          const savedTenant = this.tenants.filter(i => i.instrumentid.toString() === tenant)
          if (savedTenant && savedTenant.length > 0) {
          this.setCurrentTenant(savedTenant[0]);
          }
        } else {
          this.setCurrentTenant(this.tenants[0])
        }
      },
      (errResp) => {
        console.error('error', errResp);
      }
    );
  }
}
