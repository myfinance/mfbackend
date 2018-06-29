import { BrowserModule } from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import {InstrumentSearchComponent} from "./instrument-search/instrument-search.component";
import {BASE_URL} from "./app.tokens";
import {InstrumentCardComponent} from "./instrument-search/instrument-card.component";
import {AppRoutesModule} from "./app.routes";
import {NotFoundViewComponent} from "./views/not-found-view/not-found-view.component";
import {ErrorViewComponent} from "./views/error-view/error-view.component";
import {HomeComponent} from "./views/home/home.component";
import {TopNavigationComponent} from "./shared/components/top-navigation/top-navigation.component";
import {BasicLayoutComponent} from "./shared/components/basic-layout/basic-layout.component";
import { BarchartexpComponent } from './views/examples/barchartexp/barchartexp.component';
import {WidgetModule} from "./modules/widget/widget.module";

// ngx-bootstrap
import { CollapseModule } from 'ngx-bootstrap/collapse';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import {MyFinanceCommonModule} from "./modules/myfinance-common/myfinance-common.module";
import { LinechartexpComponent } from './views/examples/linechartexp/linechartexp.component';
import { GridexpComponent } from './views/examples/gridexp/gridexp.component';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
import {HttpInterceptor} from "./http-interceptor";
import {ApiModule} from "./modules/myfinance-tsclient-generated/api.module";
import {MyFinanceService} from "./modules/myfinance-tsclient-generated/api/myFinance.service";
import {BASE_PATH} from "./modules/myfinance-tsclient-generated/variables";
import {MyFinanceDataService} from "./shared/services/myfinance-data.service";
import {ConfigService} from "./shared/services/config.service";
import {MyFinanceWrapperService} from "./shared/services/my-finance-wrapper.service";

/**
 * Loads the configuration of the given configuration service.
 * @param configService The configuration service to be used to load the configuration.
 */
export function initConfiguration(configService: ConfigService): Function {
  return () => configService.load();
}



@NgModule({

  declarations: [
    AppComponent,
    NotFoundViewComponent,
    ErrorViewComponent,
    InstrumentSearchComponent,
    InstrumentCardComponent,
    HomeComponent,
    TopNavigationComponent,
    BasicLayoutComponent,
    BarchartexpComponent,
    LinechartexpComponent,
    GridexpComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutesModule,
    WidgetModule,
    MyFinanceCommonModule,
    ApiModule,
    CollapseModule.forRoot(),
    BsDropdownModule.forRoot()
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptor,
      multi: true
    },
    {
      provide: APP_INITIALIZER,
      useFactory: initConfiguration,
      deps: [ConfigService],
      multi: true
    },
    MyFinanceWrapperService,
    ConfigService,
    MyFinanceDataService
  ],
  bootstrap: [AppComponent]

})
export class AppModule { }
