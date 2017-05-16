import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import {InstrumentSearchComponent} from "./instrument-search/instrument-search.component";
import {InstrumentService} from "./instrument-search/InstrumentService";
import {BASE_URL} from "./app.tokens";

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
    { provide: BASE_URL, useValue: 'http://karaf:karaf@localhost:8181/dac/rest/marketdata/environments/dev'},
    InstrumentService
  ],
  bootstrap: [AppComponent]

})
export class AppModule { }
