import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Configuration } from './configuration';

import { HelloService } from './api/hello.service';
import { JobsService } from './api/jobs.service';
import { MyFinanceService } from './api/myFinance.service';
import { MyFinanceRunnerService } from './api/myFinanceRunner.service';
import { UtilityResourcesService } from './api/utilityResources.service';

@NgModule({
  imports:      [ CommonModule, HttpClientModule ],
  declarations: [],
  exports:      [],
  providers: [
    HelloService,
    JobsService,
    MyFinanceService,
    MyFinanceRunnerService,
    UtilityResourcesService ]
})
export class ApiModule {
    public static forRoot(configurationFactory: () => Configuration): ModuleWithProviders {
        return {
            ngModule: ApiModule,
            providers: [ { provide: Configuration, useFactory: configurationFactory } ]
        }
    }

    constructor( @Optional() @SkipSelf() parentModule: ApiModule) {
        if (parentModule) {
            throw new Error('ApiModule is already loaded. Import your base AppModule only.');
        }
    }
}
