/**
 * Created by hf on 19.05.2017.
 */
import { Routes, RouterModule } from '@angular/router';
import {InstrumentSearchComponent} from "./instrument-search/instrument-search.component";
import {NotFoundViewComponent} from "./views/not-found-view/not-found-view.component";
import {ErrorViewComponent} from "./views/error-view/error-view.component";
import {HomeComponent} from "./views/home/home.component";
import {BasicLayoutComponent} from "./shared/components/basic-layout/basic-layout.component";
import {BarchartexpComponent} from "./views/examples/barchartexp/barchartexp.component";
import {LinechartexpComponent} from "./views/examples/linechartexp/linechartexp.component";
import {GridexpComponent} from "./views/examples/gridexp/gridexp.component";

const APP_ROUTES: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  { path: '', component: BasicLayoutComponent,
    children: [
      {
        path: 'not_found',
        component: NotFoundViewComponent
      },
      {
        path: 'error',
        component: ErrorViewComponent
      },
      {
        path: 'home',
        component: HomeComponent
      },
      {
        path: 'instrument-search',
        component: InstrumentSearchComponent
      },
      {
        path: 'barchart',
        component: BarchartexpComponent
      },
      {
        path: 'linechart',
        component: LinechartexpComponent
      },
      {
        path: 'gridexp',
        component: GridexpComponent
      },
      {
        path: 'dashboards', loadChildren: './modules/dashboard/dashboard.module#DashboardModule'
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'not_found'
  }
];


export const AppRoutesModule = RouterModule.forRoot(APP_ROUTES);
