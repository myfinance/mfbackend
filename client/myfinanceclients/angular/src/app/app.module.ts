import { BrowserModule } from '@angular/platform-browser';
import {NgModule, ReflectiveInjector} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {Http, HttpModule} from '@angular/http';

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
import {MyFinanceDataService} from "./shared/services/myfinance-data.service";
import {MyfinanceDummyDataService} from "./shared/services/myfinance-data-mock.service";

// ngx-bootstrap
import { CollapseModule } from 'ngx-bootstrap/collapse';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import {MyFinanceCommonModule} from "./modules/myfinance-common/myfinance-common.module";
import { LinechartexpComponent } from './views/examples/linechartexp/linechartexp.component';
import { GridexpComponent } from './views/examples/gridexp/gridexp.component';

const DEBUG=true;
const BASEURL='http://localhost:8181/dac/rest/marketdata/environments/dev';


export let baseURLProvider =
  { provide: BASE_URL, useValue: BASEURL
  };

let injector = ReflectiveInjector.resolveAndCreate([baseURLProvider]);

export function myfinanceDataServiceFactory(http:Http){
  if(DEBUG) {
    return new MyfinanceDummyDataService();
  }
  else {
    return new MyFinanceDataService(http, injector.get(BASE_URL));
  }
}

export let myFinanceDataServiceProvider =
  { provide: MyFinanceDataService,
    useFactory: myfinanceDataServiceFactory,
    deps: [Http, BASE_URL]
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
    HttpModule,
    AppRoutesModule,
    WidgetModule,
    MyFinanceCommonModule,
    CollapseModule.forRoot(),
    BsDropdownModule.forRoot()
  ],
  providers: [
    baseURLProvider,
    myFinanceDataServiceProvider
  ],
  bootstrap: [AppComponent]

})
export class AppModule { }
