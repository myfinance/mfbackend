
import {InstrumentSearchComponent} from './instrument-search/instrument-search.component';

import {InstrumentCardComponent} from './instrument-search/instrument-card.component';
import {AppRoutesModule} from './app.routes';
import {NotFoundViewComponent} from './views/not-found-view/not-found-view.component';
import {ErrorViewComponent} from './views/error-view/error-view.component';
import {HomeComponent} from './views/home/home.component';
import {TopNavigationComponent} from './shared/components/top-navigation/top-navigation.component';
import {BasicLayoutComponent} from './shared/components/basic-layout/basic-layout.component';
import { BarchartexpComponent } from './views/examples/barchartexp/barchartexp.component';
import {WidgetModule} from './modules/widget/widget.module';

// ngx-bootstrap
import { CollapseModule } from 'ngx-bootstrap/collapse';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import {MyFinanceCommonModule} from './modules/myfinance-common/myfinance-common.module';
import { LinechartexpComponent } from './views/examples/linechartexp/linechartexp.component';
import { GridexpComponent } from './views/examples/gridexp/gridexp.component';
import {HttpInterceptor} from './http-interceptor';
import {ApiModule} from './modules/myfinance-tsclient-generated/api.module';
import {MyFinanceDataService} from './shared/services/myfinance-data.service';
import {ConfigService} from './shared/services/config.service';
import {MyFinanceWrapperService} from './shared/services/my-finance-wrapper.service';
import {MfAccountManagerModule} from './modules/mfaccountmanager/mfaccountmanager.module';
import {BrowserModule} from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { LicenseManager } from 'ag-grid-enterprise';
import {DashboardService} from './modules/dashboard/services/dashboard.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {registerLocaleData} from '@angular/common';
import localeDe from '@angular/common/locales/de';

LicenseManager.setLicenseKey('Comparex_AG_on_behalf_of_DZ_BANK_AG_MultiApp_5Devs3_October_2020__MTYwMTY3OTYwMDAwMA==c1b2f2c21c08f4ac19ee3cf66789c865');
registerLocaleData(localeDe);
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
    MfAccountManagerModule,
    ApiModule,
    CollapseModule.forRoot(),
    BsDropdownModule.forRoot(),
    BrowserAnimationsModule
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
    MyFinanceDataService,
    DashboardService
  ],
  bootstrap: [AppComponent]

})
export class AppModule { }
