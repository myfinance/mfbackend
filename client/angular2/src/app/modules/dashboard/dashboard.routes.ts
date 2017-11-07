import { Routes } from '@angular/router';

import { GenericDashboardViewComponent } from './views/generic-dashboard-view/generic-dashboard-view.component';

/**
 * Dashboard module routes definition.
 */
export const ROUTES: Routes = [

  { path: '',
    children: [
      { path: 'instrument-overview', component: GenericDashboardViewComponent,
      }
    ]
  }

];
