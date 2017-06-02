import { BrowserModule } from '@angular/platform-browser';
import {NgModule, ReflectiveInjector} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {Http, HttpModule} from '@angular/http';

import { AppComponent } from './app.component';
import {InstrumentSearchComponent} from "./instrument-search/instrument-search.component";
import {InstrumentService} from "./instrument-search/instrument.service";
import {BASE_URL} from "./app.tokens";
import {InstrumentDummyService} from "./instrument-search/dummy-instrument.service";
import {InstrumentCardComponent} from "./instrument-search/instrument-card.component";
import {AppRoutesModule} from "./app.routes";
import {HomeComponent} from "./home/home.component";
import {ChartComponent} from "./charts/chart.component";
import {RichGridComponent} from "./charts/rich-grid.component";
import {HeaderGroupComponent} from "./charts/header-group.component";
import {HeaderComponent} from "./charts/header.component";
import {DateComponent} from "./charts/date.component";
import {AgGridModule} from "ag-grid-angular/main";

const DEBUG=false;
const BASEURL='http://localhost:8181/dac/rest/marketdata/environments/dev';


export let baseURLProvider =
  { provide: BASE_URL, useValue: BASEURL
  };

let injector = ReflectiveInjector.resolveAndCreate([baseURLProvider]);

export function instrumentServiceFactory(http:Http){
  if(DEBUG) {
    return new InstrumentDummyService();
  }
  else {
    return new InstrumentService(http, injector.get(BASE_URL));
  }
}

export let instrumentServiceProvider =
  { provide: InstrumentService,
    useFactory: instrumentServiceFactory,
    deps: [Http, BASE_URL]
  };

@NgModule({

  declarations: [
    AppComponent,
    InstrumentSearchComponent,
    InstrumentCardComponent,
    HomeComponent,
    ChartComponent,
    RichGridComponent,
    DateComponent,
    HeaderComponent,
    HeaderGroupComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutesModule,
    AgGridModule.withComponents(
      [
        DateComponent,
        HeaderComponent,
        HeaderGroupComponent
      ]
    )
  ],
  providers: [
    baseURLProvider,
    instrumentServiceProvider
  ],
  bootstrap: [AppComponent]

})
export class AppModule { }
