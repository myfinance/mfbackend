import { Injectable, OnDestroy } from '@angular/core';

import { ToastrService } from 'ngx-toastr';


/**
 * The service provides some basic functionality for a dashboard and
 * holds the current state of the dashboard.
 *
 * Every dashboard should provide an instance of this service.
 */
@Injectable()
export class DashboardService implements OnDestroy {


  /**
   * True if the dashboard is loading, else false.
   */
  loading = false;

  /**
   * True if data was loaded, else false.
   */
  dataLoaded = false;

  /**
   * True if the dashboard is e.g. rendering items.
   */
  preparing = false;

  /**
   * True if the dashboard is completely loaded and ready to display.
   */
  loaded = false;

  constructor(
    public toastr: ToastrService) { }




  /**
   * Resets the current state of the dashboard.
   */
  reset(): void {
    this.loaded = false;
    this.loading = false;
    this.dataLoaded = false;
  }

  ngOnDestroy() {
    this.reset();
  }
}
