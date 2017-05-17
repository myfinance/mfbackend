import { BrowserModule } from '@angular/platform-browser';
import {NgModule, ReflectiveInjector} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {Http, HttpModule} from '@angular/http';

import { AppComponent } from './app.component';
import {InstrumentSearchComponent} from "./instrument-search/instrument-search.component";
import {InstrumentService} from "./instrument-search/instrument.service";
import {BASE_URL} from "./app.tokens";
import {InstrumentDummyService} from "./instrument-search/dummy.instrument.service";

const DEBUG=false;
const BASEURL='http://localhost:8181/dac/rest/marketdata/environments/dev';


export let baseURLProvider =
  { provide: BASE_URL, useValue: BASEURL
  };

let injector = ReflectiveInjector.resolveAndCreate([baseURLProvider]);

let instrumentServiceFactory = (http:Http) => {
  if(DEBUG) {
    return new InstrumentDummyService(http, injector.get(BASE_URL));
  }
  else {
    return new InstrumentService(http, injector.get(BASE_URL));
  }
};

export let instrumentServiceProvider =
  { provide: InstrumentService,
    useFactory: instrumentServiceFactory,
    deps: [Http, BASE_URL]
  };

@NgModule({

  declarations: [
    AppComponent, InstrumentSearchComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [
    baseURLProvider,
    instrumentServiceProvider
  ],
  bootstrap: [AppComponent]

})
export class AppModule { }
