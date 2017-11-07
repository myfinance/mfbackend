/**
 * Created by hf on 19.05.2017.
 */
import { Routes, RouterModule } from '@angular/router';
import {InstrumentSearchComponent} from "./instrument-search/instrument-search.component";
import {ChartComponent} from "./charts/chart.component";
import {RichGridComponent} from "./charts/rich-grid.component";
import {NotFoundViewComponent} from "./views/not-found-view/not-found-view.component";
import {ErrorViewComponent} from "./views/error-view/error-view.component";
import {HomeComponent} from "./views/home/home.component";
import {BasicLayoutComponent} from "./shared/components/basic-layout/basic-layout.component";

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
        path: 'charts',
        component: ChartComponent
      },
      {
        path: 'dashboards', loadChildren: './modules/dashboard/dashboard.module#DashboardModule'
      },
      {
        path: 'grid',
        component: RichGridComponent
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'not_found'
  }
];


export const AppRoutesModule = RouterModule.forRoot(APP_ROUTES);
