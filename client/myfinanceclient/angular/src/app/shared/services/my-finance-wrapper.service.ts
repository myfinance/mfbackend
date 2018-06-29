import {Inject, Injectable, Optional} from '@angular/core';
import {MyFinanceService} from "../../modules/myfinance-tsclient-generated";
import {Configuration} from "../../modules/myfinance-tsclient-generated/configuration";
import {HttpClient} from "@angular/common/http";
import {BASE_PATH} from "../../modules/myfinance-tsclient-generated/variables";

@Injectable()
export class MyFinanceWrapperService extends MyFinanceService{

  constructor(protected httpClient: HttpClient, @Optional()@Inject(BASE_PATH) basePath: string, @Optional() configuration: Configuration) {
    super(httpClient, basePath, configuration)
  }

  public setBasePath(basePath:string){
    this.basePath=basePath
  }

}
