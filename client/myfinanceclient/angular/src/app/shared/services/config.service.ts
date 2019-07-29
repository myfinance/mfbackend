import { Injectable } from '@angular/core';
import {ConfigModel} from "../models/config.model";
import {HttpClient} from "@angular/common/http";
import {Instrument, InstrumentListModel, StringListModel} from "../../modules/myfinance-tsclient-generated";
import {MyFinanceWrapperService} from "./my-finance-wrapper.service";
import {Observable, Subject} from "../../../../node_modules/rxjs";
import {Moment} from "moment";

@Injectable()
export class ConfigService {

  private _config: ConfigModel;
  environments: string[]
  private currentEnv: string
  configLoaded: Subject<any> = new Subject<any>()
  private isInit:boolean = false
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
        let zone = localStorage.getItem('mfzone');
        if(zone) {
          this.setCurrentZone(zone);
        } else {
          this.setCurrentZone(data.defaultZone);
        }
        // Check if environment is saved in local storage.
        // Set the current environment to the saved env or else
        // set it to the default environment in the configuration.
        let env = localStorage.getItem('env');
        if(env) {
          this.setCurrentEnv(env);
        } else {
          this.setCurrentEnv(this.getDefaultEnv());
        }
        this.isInit=true;
        this.configLoaded.next(true);
      });
  }

  getIsInit(): boolean{
    return this.isInit;
  }

  /**
   * deprecated because due to async load function it is not sure the _config is already initialized. better return an observable
   * @param property
   */
  get(property: string): any {
    let value = this._config;
    for(let p of property.split('.')) {
      value = value[p];
    }
    return value;
  }

  getCurrentEnv(){
    return this.currentEnv
  }

  getCurrentTenant(){
    return this.currentTenant
  }

  setCurrentTenant(tenant: Instrument): void {
    this.currentTenant = tenant;
    // Additionally save the zone in the local storage.
  }

  setCurrentZone(identifier: string): void {
    for(let zone of this._config.zones) {
      if(zone.identifier == identifier) {
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
    let currentZone = localStorage.getItem('mfzone');
    for(let zone of this._config.zones) {
      if(zone.identifier == currentZone) {
        return zone.defaultEnvironment;
      }
    }
  }

  private getMockEnvironments(): Observable<StringListModel> {
    let envs: string[]=["enva", "envb"];
    let envList : StringListModel = {values: envs, url:"mock", id:"mockid"};
    return Observable.of(envList);
  }

  /**
   * to avoid circuöar dependency the environment request can not be made via dataservice
   * @returns {Observable<StringListModel>}
   */
  private getEnvironmentProvider(): Observable<StringListModel> {

    if(this.get('currentZone').identifier.match("mock")){
      return this.getMockEnvironments()
    }
    this.myfinanceService.setBasePath(this.get('currentZone').url)
    return this.myfinanceService.getEnvironmentList();

  }

  private getMockTenants(): Observable<InstrumentListModel> {
    let instrument: Instrument = { instrumentid: 1, description:"tenant1", isactive: true, instrumentType: Instrument.InstrumentTypeEnum.Tenant, treelastchanged: new Date() };
    let instrument2: Instrument = { instrumentid: 2, description:"tenant2", isactive: true, instrumentType: Instrument.InstrumentTypeEnum.Tenant, treelastchanged: new Date() };
    let tenants: Instrument[]=[instrument, instrument2];
    let envList : InstrumentListModel = {values: tenants, url:"mock", id:"mockid"};
    return Observable.of(envList);
  }

  /**
   * to avoid circuöar dependency the environment request can not be made via dataservice
   * @returns {Observable<StringListModel>}
   */
  private getTenantProvider(): Observable<InstrumentListModel> {

    if(this.get('currentZone').identifier.match("mock")){
      return this.getMockTenants()
    }
    this.myfinanceService.setBasePath(this.get('currentZone').url)
    return this.myfinanceService.getTenantList_envID(this.getCurrentEnv());

  }

  private loadEnvironments(){
    this.getEnvironmentProvider().subscribe(
      (environments: StringListModel) => {
        this.environments = environments.values;
        // Check if environment is saved in local storage.
        // Set the current environment to the saved env or else
        // set it to the default environment in the configuration.
        let env = localStorage.getItem('env');
        if(env && this.environments.includes(env)) {
          this.setCurrentEnv(env);
        } else if(this.environments.includes(this.getDefaultEnv())){
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

  private loadTenants(){
    this.getTenantProvider().subscribe(
      (tenents: InstrumentListModel) => {
        this.tenants = tenents.values;
        this.setCurrentTenant(this.tenants[0])
      },
      (errResp) => {
        console.error('error', errResp);
      }
    );
  }
}
