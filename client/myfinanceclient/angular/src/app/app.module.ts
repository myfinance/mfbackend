import { BrowserModule } from '@angular/platform-browser';
import {NgModule} from '@angular/core';
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
import {ApiModule} from "./shared/myfinance-tsclient-generated/api.module";
import {MyFinanceService} from "./shared/myfinance-tsclient-generated/api/myFinance.service";
import {BASE_PATH} from "./shared/myfinance-tsclient-generated/variables";

const DEBUG=false;
const BASEURL='https://localhost:8443/dac/rest';


export let baseURLProvider =
  { provide: BASE_PATH, useValue: BASEURL
  };


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
    baseURLProvider,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptor,
      multi: true
    },
    MyFinanceService
  ],
  bootstrap: [AppComponent]

})
export class AppModule { }
