/**
 * Created by xn01598 on 19.05.2017.
 */
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import {InstrumentSearchComponent} from "./instrument-search/instrument-search.component";
import {ChartComponent} from "./charts/chart.component";
import {RichGridComponent} from "./charts/rich-grid.component";

const APP_ROUTES: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
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
    path: 'grid',
    component: RichGridComponent
  },
  {
    path: '**',
    redirectTo: 'home'
  }
];


export const AppRoutesModule = RouterModule.forRoot(APP_ROUTES);
